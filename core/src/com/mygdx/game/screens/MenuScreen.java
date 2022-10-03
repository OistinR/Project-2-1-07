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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Omega;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;

public class MenuScreen implements Screen {

    private Omega game;
    private boolean click = false;
    private Stage stage;
    private SpriteBatch mainBatch;
    private Texture omegaSymbol;
    private Skin menuSkin;
    private TextButton PVP;
    private TextButton mapDefault;
    private TextButton mapSnowflake;
    private TextButton mapSimple;
    private TextButton mapBug;
    public static int mapChoice =0 ;
    private ArrayList<TextButton> listOfMapButtons;

    public MenuScreen(Omega game) {
        super();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
        menuSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        omegaSymbol = new Texture(Gdx.files.internal("omegaSymbol.png"));
        PVP = new TextButton("Play: 2 Player", menuSkin);
        PVP.setColor(Color.BLACK);
        PVP.setPosition(545, 270);
        PVP.setSize(200, 100);

        listOfMapButtons = new ArrayList<>();

        mapDefault = new TextButton("Default", menuSkin);
        mapDefault.setColor(Color.BLACK);
        mapDefault.setPosition(445, 100);
        mapDefault.setSize(200, 100);
        mapDefault.setColor(new Color(0.5f,0.5f,0.5f,1f));

        mapSnowflake = new TextButton("Snowflake", menuSkin);
        mapSnowflake.setColor(Color.BLACK);
        mapSnowflake.setPosition(245, 100);
        mapSnowflake.setSize(200, 100);

        mapSimple = new TextButton("Simple", menuSkin);
        mapSimple.setColor(Color.BLACK);
        mapSimple.setPosition(645, 100);
        mapSimple.setSize(200, 100);

        mapBug = new TextButton("Bug", menuSkin);
        mapBug.setColor(Color.BLACK);
        mapBug.setPosition(845, 100);
        mapBug.setSize(200, 100);

        listOfMapButtons.add(mapDefault);
        listOfMapButtons.add(mapBug);
        listOfMapButtons.add(mapSimple);
        listOfMapButtons.add(mapSnowflake);

        stage.addActor(PVP);
        stage.addActor(mapDefault);
        stage.addActor(mapBug);
        stage.addActor(mapSimple);
        stage.addActor(mapSnowflake);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.90f,1.00f,1.00f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        PVP.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                click = true;
            }
        });

        if (click) {
            this.dispose();
            game.setScreen(new LoadingScreen(game));
        }

        mapSnowflake.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice =1;
                resetButtons();
                mapSnowflake.setColor(new Color(0.5f,0.5f,0.5f,1f));
            }
        });
        mapSimple.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice =2;
                resetButtons();
                mapSimple.setColor(new Color(0.5f,0.5f,0.5f,1f));
            }
        });
        mapDefault.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice =0;
                resetButtons();
                mapDefault.setColor(new Color(0.5f,0.5f,0.5f,1f));
            }
        });
        mapBug.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice =3;
                resetButtons();
                mapBug.setColor(new Color(0.5f,0.5f,0.5f,1f));
            }
        });

        stage.act(delta);
        stage.draw();

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.mainBatch.begin();

        game.font.setColor(Color.BLACK);
        game.font.getData().setScale(3, 3);

        game.font.getData().setScale(3);
        game.font.draw(game.mainBatch, "OMEGA : Java edition", 430, 450);
        game.font.draw(game.mainBatch, "Map Selection", 500, 250);
        game.mainBatch.draw(omegaSymbol, 525, 475,225f,225f);

        game.mainBatch.end();
        game.sr.end();

    }

    public void resetButtons(){
        for (TextButton tb:listOfMapButtons){
            tb.setColor(new Color(0f,0f,0f,1f));
        }
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
