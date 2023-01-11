package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.Omega;

public class CustomizeDefaultScreen implements Screen {
    Stage stage;
    Omega game;
    boolean ai, ai2;
    TextButton confirm;
    SelectBox<String> size;
    Skin menuSkin;
    int index, index2;
    public static int mapIndex;
    public boolean openable;

    public CustomizeDefaultScreen(Omega game, boolean ai, boolean ai2, int index, int index2) {
        super();
        stage = new Stage(new FillViewport(1280, 720));
        menuSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.game = game;
        this.ai = ai;
        this.ai2 = ai2;
        openable = true;

        size = new SelectBox<String>(menuSkin);
        size.setItems("Size 3", "Size 4", "Size 5", "Size 6", "Size 7");
        size.setPosition(550, 350);
        size.setSize(200, 50);

        confirm = new TextButton("Confirm", menuSkin);
        confirm.setColor(Color.BLACK);
        confirm.setPosition(620, 300);

        stage.addActor(size);
        stage.addActor(confirm);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.90f, 1.00f, 1.00f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        size.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        confirm.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                if (openable) {
                    mapIndex = size.getSelectedIndex() + 2;
                    game.setScreen(new LoadingScreen(game, ai, ai2, index, index2));
                    openable = false;
                }
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
