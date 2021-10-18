package com.star.game.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.game.helpers.Poolable;
import com.star.game.screen.ScreenManager;

public class Bullet implements Poolable {
    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private Ship owner;

    public Ship getOwner() {
        return owner;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Bullet(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void deactivate() {
        active = false;
    }

    public void activate(Ship owner, float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        this.owner = owner;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        float bx = position.x;
        float by = position.y;
        gc.getParticleController().getEffectBuilder()
                .createBulletTrace(owner.currentWeapon.getTitle(), position, velocity);


        if (position.x < -20 || position.x > ScreenManager.SCREEN_WIDTH + 20 ||
                position.y < -20 || position.y > ScreenManager.SCREEN_HEIGHT + 20) {
            deactivate();
        }
    }

}

