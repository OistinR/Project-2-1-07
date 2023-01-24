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
public class InstructionScreen5 implements Screen {

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
    public InstructionScreen5(Omega game) {
        super();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
        menuSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        next = new TextButton("Back to Main Menu",menuSkin);
        next.setPosition(1000, 10);
        next.setSize(100, 50);
        prev = new TextButton("Previous",menuSkin);
        prev.setPosition(20, 10);
        prev.setSize(50, 50);
        image = new Texture("final.png");
        
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
                game.setScreen(new MenuScreen(game));
            }
        });

        prev.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new InstructionScreen4(game));
            }
        });

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.mainBatch.begin();

        Gdx.input.setInputProcessor(stage);
        game.font.setColor(Color.BLACK);

        game.font.getData().setScale(3, 3);
        game.font.draw(game.mainBatch, "Game Instructions 5", 450, 675);
        game.font.getData().setScale(2,2);


        game.font.draw(game.mainBatch, "The game ends when there are not enough free spaces left to play a full turn", 200, 620);
        game.font.draw(game.mainBatch, "Enjoy playing! We hope you have a blast with Omega! HexaGo!", 220, 590);

        game.mainBatch.draw(image, 180, 40,800,450);

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
