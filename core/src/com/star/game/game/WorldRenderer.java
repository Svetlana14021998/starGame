package com.star.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.game.screen.ScreenManager;
import com.star.game.screen.utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;
    private BitmapFont font72;
    private StringBuilder stringBuilder;

    private FrameBuffer frameBuffer;
    private TextureRegion frameBufferRegion;
    private ShaderProgram shaderProgram;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf", BitmapFont.class);
        this.stringBuilder = new StringBuilder();

        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, ScreenManager.SCREEN_WIDTH,
                ScreenManager.SCREEN_HEIGHT, false);
        this.frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.frameBufferRegion.flip(false, true);
        this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/vertex.glsl").readString(),
                Gdx.files.internal("shaders/fragment.glsl").readString());
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader:" + shaderProgram.getLog());
        }
    }

    public void render() {
        frameBuffer.begin();

        ScreenUtils.clear(0, 0.2f, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getPowerUpsController().render(batch);
        gc.getParticleController().render(batch);
        batch.end();
        frameBuffer.end();

        batch.begin();
        batch.setShader(shaderProgram);
        shaderProgram.setUniformf("px", gc.getHero().getPosition().x / ScreenManager.SCREEN_WIDTH);
        shaderProgram.setUniformf("py", gc.getHero().getPosition().y / ScreenManager.SCREEN_HEIGHT);
        batch.draw(frameBufferRegion, 0, 0);

        batch.setShader(null);

        gc.getHero().renderGUI(batch, font32);
        if (gc.getRoundTimer() <= 3.0f) {
            stringBuilder.clear();
            stringBuilder.append("Level ").append(gc.getLevel());
            font72.draw(batch, stringBuilder, 0, ScreenManager.HALF_SCREEN_HEIGHT,
                    ScreenManager.SCREEN_WIDTH, Align.center, false);
        }
        batch.end();

        gc.getStage().draw();
    }
}
