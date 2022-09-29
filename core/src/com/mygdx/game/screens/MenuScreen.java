package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Omega;
import com.mygdx.game.screens.GameScreen;

public class MenuScreen implements Screen {

    private Omega game;
    private boolean click = false;
    private Stage stage;
    private SpriteBatch mainBatch;
    private Texture omegaSymbol;
    private Skin menuSkin;
    private TextButton PVP;

    public MenuScreen(Omega game) {
        super();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
        menuSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        omegaSymbol = new Texture(Gdx.files.internal("omegaSymbol.png"));
        PVP = new TextButton("Player vs Player", menuSkin);
        PVP.setColor(Color.BLACK);
        PVP.setPosition(545, 200);
        PVP.setSize(200, 100);

        stage.addActor(PVP);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        PVP.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                click = true;
            }
        });

        if (click == true) {
            this.dispose();
            game.setScreen(new GameScreen(game));
        }

        stage.act(delta);
        stage.draw();

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.mainBatch.begin();

        game.font.setColor(Color.BLACK);
        game.font.getData().setScale(3, 3);

        game.font.draw(game.mainBatch, "OMEGA", 560, 475);

        game.mainBatch.draw(omegaSymbol, 525, 475);

        game.mainBatch.end();
        game.sr.end();

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

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
