package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Omega;

public class menuScreen implements Screen {

    Omega game;

    SpriteBatch mainBatch;
    private Stage stage;
    private Skin menuSkin;
    private TextButton PVP;

    public menuScreen(Omega game) {
        super();
        this.game = game;
//        menuSkin = new Skin(Gdx.files.internal("Skin/skin/vhs-ui.json"));
//
//        stage = new Stage(new FitViewport(1280, 720));
//        Gdx.input.setInputProcessor(stage);
//
//        PVP = new TextButton("Player vs Player", menuSkin);
//        PVP.setPosition(220, 50);
//        PVP.setSize(200, 100);
//
//        stage.addActor(PVP);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.sr.rect(40,40,40,40);
        game.sr.end();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            System.out.println("test");
            this.dispose();
            game.setScreen(new MainGameScreen(game));
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

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

}
