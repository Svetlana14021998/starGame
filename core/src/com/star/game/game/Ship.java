package com.star.game.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.game.screen.ScreenManager;


public class Ship implements Pushable {
    protected GameController gc;
    protected TextureRegion texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected float angle;
    protected float enginePower;
    protected float fireTimer;
    protected int hpMax;
    protected int hp;
    protected Weapon currentWeapon;
    protected Circle hitArea;
    protected Circle searchArea;
    protected Weapon[] weapons;
    protected int weaponNum;
    protected OwnerType ownerType;
    protected WeaponType weaponType;

    public void resurrection(){
        hp = hpMax;
    }

    public int getHpMax() {
        return hpMax;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public int getWeaponNum() {
        return weaponNum;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public Weapon[] getWeapon() {
        return weapons;
    }

    public Circle getSearchArea() {
        return searchArea;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Ship(GameController gc, int hpMax,float x, float y ) {
        this.gc = gc;
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.position = new Vector2(x, y);
        this.hitArea = new Circle(position, 32);
        this.searchArea = new Circle(position, 100);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
    }

    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        searchArea.setPosition(position);
        float stopKoef = 1.0f - 1.0f * dt;
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);
        checkSpaceBorders();
    }

    public void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }

    private void checkSpaceBorders() {
        if (position.x < 32f) {
            position.x = 32f;
            velocity.x *= -1;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32f) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -1;
        }
        if (position.y < 32f) {
            position.y = 32f;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32f) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y *= -1;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1,
                angle);
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public void accelerate(float dt) {//ускорение
        velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
        velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
    }

    public void brake(float dt) {//торможение
        velocity.x -= MathUtils.cosDeg(angle) * enginePower * dt / 2;
        velocity.y -= MathUtils.sinDeg(angle) * enginePower * dt / 2;
    }

    public void rotate(float rotationSpeed, float dt){
        angle += rotationSpeed * dt;
    }//поворот
}
