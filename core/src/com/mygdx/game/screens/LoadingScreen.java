package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.Omega;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LoadingScreen implements Screen {

    private Omega game;
    private Stage stage;
    private int close;
    private boolean ai;
    public LoadingScreen(Omega game, boolean ai) {
        super();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
        close = 0;
        this.ai = ai;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.90f,1.00f,1.00f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.mainBatch.begin();

        game.font.setColor(Color.BLACK);
        game.font.getData().setScale(3, 3);

        game.font.draw(game.mainBatch, "Loading ...", 560, 475);

        game.mainBatch.end();
        game.sr.end();
        

        if(close==6) {
            this.dispose();
            game.setScreen(new GameScreen(game,ai));
        } else {
            close++;
        }
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