package com.star.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.game.screen.ScreenManager;
import com.star.game.screen.utils.Assets;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BotHelper botHelper;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private Hero hero;
    private Vector2 tmpVec;
    private Stage stage;
    private boolean pause;
    private int level;
    private float roundTimer;
    private float helpTimer;
    private Music music;
    private Sound powerupsound;//звук для подбора powerups
    private Sound nextlevelsound;//звук перехода на новый уровень

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
        this.hero = new Hero(this,botHelper);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.botHelper = new BotHelper(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        this.level = 1;
        Gdx.input.setInputProcessor(stage);
        this.tmpVec = new Vector2(0.0f, 0.0f);
        this.roundTimer = 0.0f;
        this.helpTimer = 0.0f;
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
        asteroidController.update(dt);
        bulletController.update(dt);
        powerUpsController.update(dt);
        particleController.update(dt);
        checkCollisions();
        createHelp();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        if (getAsteroidController().getActiveList().size() == 0) {
            level++;
            nextlevelsound.play();
            createAsteroids();
            roundTimer = 0.0f;
        }
        stage.act(dt);
    }

    public void createHelp() {

        if (tmpVec.set(hero.getPosition()).sub(botHelper.getPosition()).len() < botHelper.getHelpArea() && helpTimer > botHelper.getMaxHelpTimer()) {
            powerUpsController.create(botHelper.getPosition().x, botHelper.getPosition().y);
            helpTimer = 0.0f;
        }
    }

    public void checkCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tmpVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tmpVec, halfOverLen);
                a.getPosition().mulAdd(tmpVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;

                hero.getVelocity().mulAdd(tmpVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tmpVec, -200.0f * hero.getHitArea().radius / sumScl);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 20);
                }
                hero.takeDamage(2 * level);//зависимоть урона от уровня игры
            }
        }


        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4),
                            b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.3f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );

                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() / 4.0f);
                        }
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getSearchArea().contains(p.getPosition())) {
                tmpVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tmpVec, 100.0f);
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

    public void dispose() {
        background.dispose();
    }


}
