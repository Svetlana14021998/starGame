package com.star.game.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.game.screen.ScreenManager;

//добавить выпадающие powerUps при нанесении урона возможно кидается астероидами
public class BotBoss extends Ship implements Pushable,IsBot{
    private Vector2 tempVector;
    private StringBuilder strBuilder;
    private Texture texture;
    private int probably;

    public int getProbably() {
        return probably;
    }

    public BotBoss(GameController gc) {
        super(gc, 500,
                MathUtils.random(ScreenManager.SCREEN_WIDTH - 200, ScreenManager.SCREEN_WIDTH - 100),
                MathUtils.random(100, ScreenManager.SCREEN_HEIGHT - 100));
        this.hitArea =new Circle(position, 115);
        this.texture = new Texture("images/sokol.png");
        this.enginePower = 100.0f;
        this.ownerType = OwnerType.BOT;
        this.tempVector = new Vector2();
        this.probably = 7;//вероятность выпадения powerUps
        this.weaponType = WeaponType.SUPER_LASER;
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
        this.strBuilder = new StringBuilder();
    }

    public void update(float dt) {
        super.update(dt);
        tempVector.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tempVector.angleDeg();
        if (gc.getHero().getPosition().dst(position) > 200) {
            accelerate(dt);
        }
        if (gc.getHero().getPosition().dst(position) < 300) {
            tryToFire();
        }

    }
    public void render(SpriteBatch batch) {
        batch.draw(texture,position.x - 125, position.y - 100, 125, 100,
                250, 200,1,1,0,0,0,250,200,false,false);
        }


    public void renderHP(SpriteBatch batch, BitmapFont font){
        strBuilder.setLength(0);
        strBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        font.draw(batch, strBuilder, 1020, 700);
    }
    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(
                        gc, this, weaponType, 0.3f, 3, 700, 30000,
                        new Vector3[]{
                                new Vector3(125, 0, 0),
                                new Vector3(125, 100, 0),
                                new Vector3(125, -100, 0),

                        }),
        };
    }
}
