package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.Omega;

import java.util.ArrayList;

/**
 * MenuScreen is the first screen the user will see when starting the game,in
 * this file he can choose between multiples
 * maps and options before starting the game
 */
public class MenuScreen implements Screen {

    private Omega game;
    private boolean click = false, clickAI = false, clickInstruction = false, clickBVB = false, click3P = false, click4P = false;
    private Stage stage;
    private Texture omegaSymbol;
    private Skin menuSkin;
    private TextButton PVP, PVP3, PVP4, PVAI, BVB, mapDefault, mapSnowflake, mapSimple, mapBug, instructionButton;
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

        PVP = new TextButton("Play: 2 Player", menuSkin);
        PVP.setColor(Color.BLACK);
        PVP.setPosition(125, 270);
        PVP.setSize(200, 100);

        PVP3 = new TextButton("Play: 3 Players", menuSkin);
        PVP3.setColor(Color.BLACK);
        PVP3.setPosition(325, 270);
        PVP3.setSize(200, 100);

        PVP4 = new TextButton("Play: 4 Players", menuSkin);
        PVP4.setColor(Color.BLACK);
        PVP4.setPosition(525, 270);
        PVP4.setSize(200, 100);

        PVAI = new TextButton("Play: AI", menuSkin);
        PVAI.setColor(Color.BLACK);
        PVAI.setPosition(725, 270);
        PVAI.setSize(200, 100);

        BVB = new TextButton("Play: Bot vs Bot", menuSkin);
        BVB.setColor(Color.BLACK);
        BVB.setPosition(925, 270);
        BVB.setSize(200, 100);

        listOfMapButtons = new ArrayList<>();

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
        stage.addActor(PVP3);
        stage.addActor(PVP4);
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
                click = true;
            }
        });

        if (click) {
            this.dispose();
            game.setScreen(new LoadingScreen(game, false, false,2));
        }

        PVP3.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                click3P = true;
            }
        });

        if (click3P) {
            this.dispose();
            game.setScreen(new LoadingScreen(game, false, false,3));
        }

        PVP4.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                click4P = true;
            }
        });

        if (click4P) {
            this.dispose();
            game.setScreen(new LoadingScreen(game, false, false,4));
        }

        PVAI.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                clickAI = true;
            }
        });

        if (clickAI) {
            this.dispose();
            game.setScreen(new LoadingScreen(game, true, false,2));
        }

        BVB.addListener(new ClickListener() {
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                clickBVB = true;
            }
        });

        if (clickBVB) { // Bot vs Bot
            this.dispose();
            game.setScreen(new LoadingScreen(game, true, true,2));
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

    public void resetMapChoice(){
        mapChoice = 0;
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
