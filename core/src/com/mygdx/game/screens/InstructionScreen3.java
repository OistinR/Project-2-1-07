package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.Omega;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *Class executed when the instruction button is pressed, explains the game
 */
public class InstructionScreen3 implements Screen {

    private Omega game;
    private Stage stage;
    public BitmapFont font;
    private TextButton next,prev;
    private Skin menuSkin;
    private Texture image;

    /**
     *
     * @param game the class that connects all the parts of the game
     */
    public InstructionScreen3(Omega game) {
        super();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
        menuSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        next = new TextButton("Next",menuSkin);
        next.setPosition(1200, 10);
        next.setSize(50, 50);
        prev = new TextButton("Previous",menuSkin);
        prev.setPosition(20, 10);
        prev.setSize(50, 50);
        image = new Texture("explain_screen.png");
        
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

        font = new BitmapFont();
		font.setColor(Color.BLACK);
    }

    @Override
    /**
     *Render method render the screen every x times to put new information on the screen when action occur
     */
    public void render(float delta) {
        ScreenUtils.clear(0.90f,1.00f,1.00f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        stage.addActor(next);
        stage.addActor(prev);

        next.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new InstructionScreen4(game));
            }
        });

        prev.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new InstructionScreen2(game));
            }
        });

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.mainBatch.begin();

        Gdx.input.setInputProcessor(stage);
        game.font.setColor(Color.BLACK);

        game.font.getData().setScale(3, 3);
        game.font.draw(game.mainBatch, "Game Instructions 3", 450, 675);
        game.font.getData().setScale(2,2);


        game.font.draw(game.mainBatch, "When the game is started each player gets assigned a color: player 1 gets pink and player 2", 50, 620);
        game.font.draw(game.mainBatch, "gets blue. Each turn, the current player must place one stone of each color on any of the", 78, 590);
        game.font.draw(game.mainBatch, "free spaces on the board, which are colored gray. You can always see which player's turn", 76, 560);
        game.font.draw(game.mainBatch, "it currently is at the top of the screen. At the bottom of the screen you can see which", 106, 530);
        game.font.draw(game.mainBatch, "color hexagon you have to place next.", 400, 500);

        game.mainBatch.draw(image, 300,50,700,400);

    

        game.font.draw(game.mainBatch, "See the scores here", 980, 450);
        game.font.draw(game.mainBatch, "Turn indicator", 550, 400);
        game.font.draw(game.mainBatch, "Next colour to place", 550, 90);

        font.draw(game.mainBatch, "Press ESC to return to main menu", 5, 16);

        game.mainBatch.end();
        game.sr.end();


        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    /**
     * resize the screen
     */
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    /**
     * pause the screen
     */
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    /**
     * resume the game if paused
     */
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
