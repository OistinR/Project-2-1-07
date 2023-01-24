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
public class InstructionScreen4 implements Screen {

    private Omega game;
    private Stage stage;
    public BitmapFont font;
    private TextButton next,prev;
    private Skin menuSkin;
    private Texture confirm, undo, confirm_image, undo_image;

    /**
     *
     * @param game the class that connects all the parts of the game
     */
    public InstructionScreen4(Omega game) {
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

        confirm = new Texture("confirm_tutorial.png");
        undo = new Texture("undo_tutorial.png");
        confirm_image = new Texture("confirm_field.png");
        undo_image = new Texture("undo_field.png");
        
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
                game.setScreen(new InstructionScreen5(game));
            }
        });

        prev.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new InstructionScreen3(game));
            }
        });

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        game.mainBatch.begin();

        Gdx.input.setInputProcessor(stage);
        game.font.setColor(Color.BLACK);

        game.font.getData().setScale(3, 3);
        game.font.draw(game.mainBatch, "Game Instructions 4", 450, 675);
        game.font.getData().setScale(2,2);


        game.font.draw(game.mainBatch, "After placing a stone of every colour you have two options", 170, 600);
        game.font.draw(game.mainBatch, "You can either confirm or undo your moves made in this turn.", 160, 570);

        game.mainBatch.draw(confirm, 200, 370,240,160);
        game.mainBatch.draw(undo, 800,370,240, 160);
        game.mainBatch.draw(confirm_image, 170, 100,350,200);
        game.mainBatch.draw(undo_image, 770,100, 350,200);

        font.draw(game.mainBatch,"Continue will finalise the move & end your turn",150,350);
        font.draw(game.mainBatch,"Undo will remove the stones you placed and you can change your moves",750,350);

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
