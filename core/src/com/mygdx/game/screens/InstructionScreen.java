package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.Omega;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class InstructionScreen implements Screen {

    private Omega game;
    private Stage stage;
    public BitmapFont font;

    public InstructionScreen(Omega game) {
        super();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

        font = new BitmapFont();
		font.setColor(Color.BLACK);
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
        game.font.draw(game.mainBatch, "Game Instructions", 450, 675);
        game.font.getData().setScale(2,2);

        game.font.draw(game.mainBatch, "In Omega, players try to create groups of their color by placing hexagonal stones on a field", 60, 600);
        game.font.draw(game.mainBatch, "in order to score points. The final score is calculated by multiplying the sizes of ", 150, 570);
        game.font.draw(game.mainBatch, "all of the different groups of a specific color.", 370, 540);

        game.font.draw(game.mainBatch, "You can determine on which map you would like to play by selecting the map in the bottom row", 40, 500);
        game.font.draw(game.mainBatch, "of the menu screen. From there on, you can choose whether you want to play a 2 player game", 40, 470);
        game.font.draw(game.mainBatch, "or you want to play against an AI.", 428, 440);

        game.font.draw(game.mainBatch, "When the game is started each player gets assigned a color: player 1 gets pink and player 2", 50, 400);
        game.font.draw(game.mainBatch, "gets blue. Each turn, the current player must place one stone of each color on any of the", 78, 370);
        game.font.draw(game.mainBatch, "free spaces on the board, which are colored gray. You can always see which player's turn", 76, 340);
        game.font.draw(game.mainBatch, "it currently is at the top of the screen. At the bottom of the screen you can see which", 106, 310);
        game.font.draw(game.mainBatch, "color hexagon you have to place next.", 400, 280);

        game.font.draw(game.mainBatch, "After placing your 2 stones, you can choose to either confirm your move and pass the turn to", 52, 240);
        game.font.draw(game.mainBatch, "the next player, or you can choose to undo your moves by clicking on the 'Undo' button, which", 50, 210);
        game.font.draw(game.mainBatch, "will reset your moves and allow you to start your turn over. Each player total score is always", 68, 180);
        game.font.draw(game.mainBatch, "shown in the top corners of the screen, next to player number.", 249, 150);

        game.font.draw(game.mainBatch, "The game ends when it is impossible for both players to complete a full turn, meaning there", 56, 110);
        game.font.draw(game.mainBatch, "are not enough free spaces on the board left. The player who has the highest score then wins!", 49, 80);

        font.draw(game.mainBatch, "Press ESC to return to main menu", 5, 16);

        game.mainBatch.end();
        game.sr.end();


        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            game.setScreen(new MenuScreen(game));
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
