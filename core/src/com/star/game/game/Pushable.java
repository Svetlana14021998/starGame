package com.star.game.game;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public interface Pushable {
    Vector2 getPosition();
    Circle getHitArea();
    }
