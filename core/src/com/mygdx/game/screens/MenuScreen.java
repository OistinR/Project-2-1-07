package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.Omega;

import java.time.format.FormatStyle;
import java.util.ArrayList;

/**
 * MenuScreen is the first screen the user will see when starting the game,in
 * this file he can choose between multiples
 * maps and options before starting the game
 */
public class MenuScreen implements Screen {

    private Omega game;
    private TextButton BVB;
    private boolean clickPvP = false;
    private boolean clickBVB = false;
    private boolean clickPvB = false;
    private boolean clickInstruction = false;
    private Stage stage;
    private SpriteBatch mainBatch;
    private Texture omegaSymbol;
    private Skin menuSkin;
    private TextButton PVP;
    private TextButton PVAI;
    private TextButton mapDefault;
    private TextButton mapSnowflake;
    private TextButton mapSimple;
    private TextButton mapBug;
    private TextButton instructionButton;
    public static int mapChoice = 0;
    private ArrayList<TextButton> listOfMapButtons;

    /**
     * @param game the class that connect all the classes between each other
     */
    public MenuScreen(Omega game) {
        super();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
        menuSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        omegaSymbol = new Texture(Gdx.files.internal("omegaSymbol.png"));
        PVP = new TextButton("Play: Player vs Player", menuSkin);
        PVP.setColor(Color.BLACK);
        PVP.setPosition(325, 270);
        PVP.setSize(200, 100);

        PVAI = new TextButton("Play: Player vs Bot", menuSkin);
        PVAI.setColor(Color.BLACK);
        PVAI.setPosition(525, 270);
        PVAI.setSize(200, 100);

        listOfMapButtons = new ArrayList<>();

        BVB = new TextButton("Play: Bot vs Bot", menuSkin);
        BVB.setColor(Color.BLACK);
        BVB.setPosition(725, 270);
        BVB.setSize(200, 100);

        mapDefault = new TextButton("Default", menuSkin);
        mapDefault.setColor(Color.BLACK);
        mapDefault.setPosition(445, 100);
        mapDefault.setSize(200, 100);
        mapDefault.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));

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

        instructionButton = new TextButton("Game Instructions", menuSkin);
        instructionButton.setColor(Color.RED);
        instructionButton.setPosition(45, 25);
        instructionButton.setSize(150, 50);

        stage.addActor(BVB);
        stage.addActor(PVP);
        stage.addActor(PVAI);
        stage.addActor(mapDefault);
        stage.addActor(mapBug);
        stage.addActor(mapSimple);
        stage.addActor(mapSnowflake);
        stage.addActor(instructionButton);
    }

    @Override
    /**
     * Render method render the screen every x times to put new information on the
     * screen when action occur
     */
    public void render(float delta) {
        ScreenUtils.clear(0.90f, 1.00f, 1.00f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        PVP.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                clickPvP = true;
            }
        });

        // 2 player gamemodes
        if (clickPvP) { // Player vs Player
            this.dispose();
            game.setScreen(new LoadingScreen(game, false, false));
        }

        PVAI.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                clickPvB = true;
            }
        });

        if (clickBVB) { // Bot vs Bot
            this.dispose();
            game.setScreen(new LoadingScreen(game, true, true));
        }

        BVB.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                clickBVB = true;
            }
        });

        if (clickPvB) { // Player vs Bot
            this.dispose();
            game.setScreen(new LoadingScreen(game, true, false));
        }

        mapSnowflake.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice = 1;
                resetButtons();
                mapSnowflake.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));
            }
        });
        mapSimple.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice = 2;
                resetButtons();
                mapSimple.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));
            }
        });
        mapDefault.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice = 0;
                resetButtons();
                mapDefault.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));
            }
        });
        mapBug.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                mapChoice = 3;
                resetButtons();
                mapBug.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));
            }
        });

        instructionButton.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                clickInstruction = true;
            }
        });

        if (clickInstruction) {
            // System.out.println("BUtton clicked");
            this.dispose();
            game.setScreen(new InstructionScreen(game));
        }

        stage.act(delta);
        stage.draw();

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.mainBatch.begin();

        game.font.setColor(Color.BLACK);
        game.font.getData().setScale(3, 3);

        game.font.getData().setScale(3);
        game.font.draw(game.mainBatch, "OMEGA : Java edition", 430, 450);
        game.font.draw(game.mainBatch, "Map Selection", 500, 250);
        game.mainBatch.draw(omegaSymbol, 525, 475, 225f, 225f);

        game.mainBatch.end();
        game.sr.end();
    }

    /**
     * the method put all the different buttons in the same organized colour
     */
    public void resetButtons() {
        for (TextButton tb : listOfMapButtons) {
            tb.setColor(new Color(0f, 0f, 0f, 1f));
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
