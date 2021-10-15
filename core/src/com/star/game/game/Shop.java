package com.star.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.game.screen.utils.Assets;

public class Shop extends Group {
    private Hero hero;
    private BitmapFont font24;

    public Shop(final Hero hero) {
        this.hero = hero;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888);
        pixmap.setColor(Color.rgb888(0.0f, 0.0f, 0.5f));
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        final TextButton btnMagnet = new TextButton("Magnet", textButtonStyle);
        btnMagnet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.MAGNET.cost)) {
                    if (hero.upgrade(Hero.Skill.MAGNET)) {
                        hero.decreaseMoney(Hero.Skill.MAGNET.cost);
                    }
                }
            }
        });

        btnMagnet.setPosition(120, 120);
        this.addActor(btnMagnet);

        final TextButton btnWp = new TextButton("Weapon", textButtonStyle);
        btnWp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.WEAPON.cost)) {
                    if (hero.upgrade(Hero.Skill.WEAPON)) {
                        hero.decreaseMoney(Hero.Skill.WEAPON.cost);
                    }
                }
            }
        });

        btnWp.setPosition(20, 120);
        this.addActor(btnWp);


        final TextButton btnHp = new TextButton("HP", textButtonStyle);
        btnHp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP.cost)) {
                    if (hero.upgrade(Hero.Skill.HP)) {
                        hero.decreaseMoney(Hero.Skill.HP.cost);
                    }
                }
            }
        });

        btnHp.setPosition(20, 220);
        this.addActor(btnHp);


        final TextButton btnHpMax = new TextButton("HP MAX", textButtonStyle);
        btnHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP_MAX.cost)) {
                    if (hero.upgrade(Hero.Skill.HP_MAX)) {
                        hero.decreaseMoney(Hero.Skill.HP_MAX.cost);
                    }
                }
            }
        });

        btnHpMax.setPosition(20, 320);
        this.addActor(btnHpMax);


        final TextButton btnClose = new TextButton("X", textButtonStyle);
        final Shop thisShop = this;

        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
                hero.setPause(false);
            }
        });

        btnClose.setTransform(true);
        btnClose.setScale(0.5f);
        btnClose.setPosition(340, 340);
        this.addActor(btnClose);

        this.setPosition(20, 20);
        this.setVisible(false);
        skin.dispose();
    }
}

