package com.star.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.game.screen.utils.Assets;

public class PauseScreen extends AbstractScreen{
    private BitmapFont font24;
    private Stage stage;


    public PauseScreen(SpriteBatch batch) {
        super(batch);
    }
    @Override
    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnGoToMenu = new TextButton("Go To Menu", textButtonStyle);
        Button btnNewGame = new TextButton("New Game", textButtonStyle);
        Button btnBack = new TextButton("Back", textButtonStyle);
        btnGoToMenu.setPosition(480, 210);
        btnNewGame.setPosition(480, 110);
        btnBack.setPosition(480, 10);

        btnGoToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
          ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              //???
            }
        });
        stage.addActor(btnNewGame);
        stage.addActor(btnGoToMenu);
        stage.addActor(btnBack);
        skin.dispose();


    }

    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        font24.draw(batch, "Pause", 0, 600, 1280, 1, false);
        batch.end();
        stage.draw();
    }
    @Override
    public void dispose() {

    }
}

