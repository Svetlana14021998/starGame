package com.star.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.game.screen.ScreenManager;
import com.star.game.screen.utils.Assets;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BotHelper botHelper;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private Hero hero;
    private Bot bot;
    private BotBoss botBoss;
    private Vector2 tmpVec;
    private Stage stage;
    private boolean pause;
    private int level;
    private final int BOSS_LEVEL = 5;
    private float roundTimer;
    private float helpTimer;
    private float givePowerTimer;
    private Music music;
    private Sound powerupsound;//звук для подбора powerups
    private Sound nextlevelsound;//звук перехода на новый уровень
    private StringBuilder stringBuilder;

    public int getBOSS_LEVEL() {
        return BOSS_LEVEL;
    }

    public BotBoss getBotBoss() {
        return botBoss;
    }

    public Bot getBot() {
        return bot;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public BotHelper getBotHelper() {
        return botHelper;
    }

    public float getRoundTimer() {
        return roundTimer;
    }

    public int getLevel() {
        return level;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Stage getStage() {
        return stage;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }


    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.botHelper = new BotHelper(this);
        this.hero = new Hero(this, botHelper);
        this.bot = new Bot(this);
        this.botBoss = new BotBoss(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.botHelper = new BotHelper(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.infoController = new InfoController();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        this.stringBuilder = new StringBuilder();
        this.level = 1;
        Gdx.input.setInputProcessor(stage);
        this.tmpVec = new Vector2(0.0f, 0.0f);
        this.roundTimer = 0.0f;
        this.helpTimer = 0.0f;
        this.givePowerTimer = 0.0f;
        this.powerupsound = Assets.getInstance().getAssetManager().get("audio/money.mp3");
        this.nextlevelsound = Assets.getInstance().getAssetManager().get("audio/nextlevel.mp3");
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.play();
        createAsteroids();
    }

    public void createAsteroids() {
        for (int i = 0; i < (level <= 3 ? level : 3); i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        helpTimer += dt;
        roundTimer += dt;
        background.update(dt);
        hero.update(dt);
        botHelper.update(dt);
        bulletController.update(dt);
        powerUpsController.update(dt);
        particleController.update(dt);
        infoController.update(dt);
        createHelp();
        if (level != BOSS_LEVEL) {
            if (bot.isAlive()) {
                bot.update(dt);
            }
            asteroidController.update(dt);
            checkCollisions();
        }

        if (level == BOSS_LEVEL) {
            if (botBoss.isAlive()) {
                botBoss.update(dt);
            }
            checkInteractions();
            bulletHit(botBoss);
            takePowerUps();
        }


        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }

        if (getAsteroidController().getActiveList().size() == 0 && (level < BOSS_LEVEL - 1)) {
            nextLevel();
        } else if (getAsteroidController().getActiveList().size() == 0 && (level == BOSS_LEVEL - 1)) {
            level++;
        }

        if (level == BOSS_LEVEL && !botBoss.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.WIN, hero);//обавление экрана выигрыша
        }
        stage.act(dt);
    }


    public void nextLevel() {
        level++;
        hero.hp = hero.hpMax;
        bot.resurrection();
        nextlevelsound.play();
        createAsteroids();
        roundTimer = 0.0f;
    }

    public void createHelp() {

        if (tmpVec.set(hero.getPosition()).sub(botHelper.getPosition()).len() < botHelper.getHelpArea() && helpTimer > botHelper.getMaxHelpTimer()) {
            powerUpsController.create(botHelper.getPosition().x, botHelper.getPosition().y);
            helpTimer = 0.0f;
        }
    }

    public void checkCollisions() {
        boom(hero);
        boom(bot);

        for (int i = 0; i < bulletController.getActiveList().size(); i++) { //астероид и пуля
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.getEffectBuilder()
                            .bulletCollideWithAsteroid(b.getPosition(), b.getVelocity());

                    b.deactivate();
                    int damage = hero.getCurrentWeapon().getDamage();
                    if (MathUtils.random(0, 100) < hero.getCritical()) {
                        damage *= 3;
                        showDamage("-", damage, a.getPosition(), Color.PINK);
                    }
                    if (a.takeDamage(damage)) {
                        if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                            hero.addScore(a.getHpMax() * 100);
                            for (int k = 0; k < 3; k++) {
                                powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() / 4.0f);
                            }
                        }
                    }
                    break;
                }
            }
        }

        bulletHit(bot);
        takePowerUps();

 /*       for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {//подбор powerups
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getSearchArea().contains(p.getPosition())) {
                tmpVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tmpVec, 200.0f);
            }

            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(
                        p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
                powerupsound.play();
            }
        }*/

        if (hero.getHitArea().overlaps(bot.hitArea) && bot.isAlive()) {//герой и бот
            push(hero, bot);

        }


    }

    private void takePowerUps(){//подбор powerups
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getSearchArea().contains(p.getPosition())) {
                tmpVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tmpVec, 200.0f);
            }

            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(
                        p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
                powerupsound.play();
            }
        }
    }

    private void bulletHit(IsBot isBot) {//попадение пули
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            if (b.getOwner().getOwnerType() == OwnerType.PLAYER && isBot.isAlive()) {
                if (isBot.getHitArea().contains(b.getPosition())) {
                    if (isBot instanceof BotBoss) {
                        createPowerUps();
                    }
                    b.deactivate();
                    int damage = hero.getCurrentWeapon().getDamage();
                    if (MathUtils.random(0, 100) < hero.getCritical()) {
                        damage *= 3;
                    }

                    isBot.takeDamage(damage);
                    showDamage("HP -", damage, isBot.getPosition(), Color.CYAN);
                    hero.addScore(isBot.getHpMax() * 100);


                    if (!isBot.isAlive()) {
                        particleController.getEffectBuilder().botIsDead(isBot.getPosition().x, isBot.getPosition().y);
                    }
                }
            }

            if (b.getOwner().getOwnerType() == OwnerType.BOT) {
                if (hero.getHitArea().contains(b.getPosition())) {
                    hero.takeDamage(isBot.getCurrentWeapon().getDamage());
                    showDamage("HP -", isBot.getCurrentWeapon().getDamage(), hero.getPosition(), Color.RED);
                    b.deactivate();
                }
            }
        }
    }

    private void createPowerUps() {
        if (MathUtils.random(0, 100) < botBoss.getProbably()) {
            powerUpsController.create(botBoss.position.x, botBoss.position.y);
        }

    }


    private void showDamage(String str, int damage, Vector2 position, Color color) {
        stringBuilder.clear();
        stringBuilder.append(str).append(damage);
        infoController.setup(position.x, position.y,
                stringBuilder, color);
    }

    private void boom(Ship ship) {//столкновение астероида и корабля
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {    //астероид и корабль
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(ship.getHitArea()) && ship.isAlive()) {
                push(a, ship);

                float sumScl = ship.getHitArea().radius * 2 + a.getHitArea().radius;

                ship.getVelocity().mulAdd(tmpVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tmpVec, -200.0f * ship.getHitArea().radius / sumScl);

                a.takeDamage(2);
                if (ship instanceof Hero) {
                    int hurt = (int) (level * a.getScale() * 5);
                    hero.takeDamage(hurt);
                    showDamage("HP -", hurt, hero.getPosition(), Color.RED);
                    ((Hero) ship).addScore(a.getHpMax() * 20);
                }
            }
        }
    }


    private void push(Pushable somebody1, Pushable somebody2) {//отталкивание

        float dst = somebody1.getPosition().dst(somebody2.getPosition());
        float halfOverLen = (somebody1.getHitArea().radius + somebody2.getHitArea().radius - dst) / 2.0f;
        tmpVec.set(somebody2.getPosition()).sub(somebody1.getPosition()).nor();
        somebody2.getPosition().mulAdd(tmpVec, halfOverLen);
        somebody1.getPosition().mulAdd(tmpVec, -halfOverLen);

    }

    public void checkInteractions() {//столкновение босса и героя
        if (hero.getHitArea().overlaps(botBoss.getHitArea())) {
            push(hero, botBoss);
            hero.takeDamage(1);
            botBoss.takeDamage(1);
        }
    }

    public void dispose() {
        background.dispose();
    }


}
