package com.star.game.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.game.screen.utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;
    private StringBuilder stringBuilder;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.stringBuilder = new StringBuilder();
    }

    public void render() {
        ScreenUtils.clear(0, 0.2f, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getPowerUpsController().render(batch);
        gc.getParticleController().render(batch);
        gc.getHero().renderGUI(batch, font32);
        batch.end();
    }
}
