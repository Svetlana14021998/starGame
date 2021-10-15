package com.star.game.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.screen.ScreenManager;
import com.star.game.screen.utils.Assets;

public class BotHelper {
    //функционал - движется по верхней линии экрана и скидывает powerUps, если герой находится в определенном диапазоне
    //добавлены поля maxHelpTimer - время, через которое возможен сброс powerUps и helpArea - расстояние, в пределах
    // которого должен находиться корабль для сброса. В магазин добавлена возможность уменьшать таймер
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float maxHelpTimer;
    private int helpArea;


    public BotHelper(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("nlo");
        this.position = new Vector2(88, ScreenManager.SCREEN_HEIGHT - 52);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.maxHelpTimer = 20.0f;
        this.helpArea = 200;
    }

    public void setMaxHelpTimer(float count) {
        this.maxHelpTimer -= count;
    }

    public int getHelpArea() {
        return helpArea;
    }

    public float getMaxHelpTimer() {
        return maxHelpTimer;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 88, position.y - 52, 88, 52, 176, 104, 1, 1,
                angle);
    }

    public void update(float dt) {
        velocity.x = MathUtils.random(50, 100);
        velocity.y = 0;
        position.mulAdd(velocity, dt);
        if (position.x >= ScreenManager.SCREEN_WIDTH + 88) {
            position.x = 88;
        }

    }
}
