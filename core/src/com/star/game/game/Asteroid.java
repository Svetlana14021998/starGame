package com.star.game.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.game.helpers.Poolable;
import com.star.game.screen.ScreenManager;
import com.star.game.screen.utils.Assets;


public class Asteroid implements Poolable, Pushable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private int hp;
    private int hpMax;
    private float angle;//угол показа изображения
    private float rotationSpeed;//скорость вращения
    private float scale;//масштаб
    private boolean active;
    private Circle hitArea;
    private Sound explosionSound;//звук уничтожения астероида

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public float getScale() {
        return scale;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Asteroid(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
        this.explosionSound = Assets.getInstance().getAssetManager().get("audio/explosion.mp3");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, scale, scale, angle);
    }

    public void deactivate() {
        active = false;
        explosionSound.play();
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.hpMax = (int) ((7 + gc.getLevel() * 2) * scale);//зависомость живучести астероида от уровня игры
        this.hp = hpMax;
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        this.hitArea.setPosition(position);
        this.scale = scale;
        this.active = true;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale > 0.3) {
                gc.getAsteroidController().setup(position.x, position.y,
                        MathUtils.random(-200, 200), MathUtils.random(-200, 200), scale - 0.2f);

                gc.getAsteroidController().setup(position.x, position.y,
                        MathUtils.random(-200, 200), MathUtils.random(-200, 200), scale - 0.2f);

                gc.getAsteroidController().setup(position.x, position.y,
                        MathUtils.random(-200, 200), MathUtils.random(-200, 200), scale - 0.2f);
            }
            return true;
        } else {
            return false;
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;

        if (position.x < -hitArea.radius) {
            position.x = ScreenManager.SCREEN_WIDTH + hitArea.radius;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + hitArea.radius) {
            position.x = -hitArea.radius;
        }
        if (position.y < -hitArea.radius) {
            position.y = ScreenManager.SCREEN_HEIGHT + hitArea.radius;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + hitArea.radius) {
            position.y = -hitArea.radius;
        }
        hitArea.setPosition(position);
    }

}
