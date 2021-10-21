package com.star.game.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public interface IsBot {
    boolean isAlive();
    Circle getHitArea();
    void takeDamage( int damage);
    Vector2 getPosition();
     Weapon getCurrentWeapon();
     int getHpMax();
}
