package com.star.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.game.game.Background;
import com.star.game.screen.utils.Assets;

public class MenuScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("New Game", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnNewGame.setPosition(480, 210);
        btnExitGame.setPosition(480, 110);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);
        skin.dispose();


    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "Star Game 2021", 0, 600, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
