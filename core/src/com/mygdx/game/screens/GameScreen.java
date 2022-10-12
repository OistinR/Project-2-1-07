package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GifDecoder;
import com.mygdx.game.Omega;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.OLABot;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.buttons.ConfirmButton;
import com.mygdx.game.buttons.UndoButton;
import com.mygdx.game.buttons.PieButton;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {

    public static int SCREENWIDTH;
    private MenuScreen menu;
    public static int SCREENHEIGHT;
    private boolean firstColor = true;

    private ArrayList<Hexagon> field;
    private ScoringEngine SEngine;
    public BitmapFont font;
    private int numberOfHex;
    private boolean stopGame;
    private int hexPlaced;
    private boolean arrowPlayerOne;
    private int round;
    private Texture blueTileTexture;
    private Texture redTileTexture;
    private Omega game;

    private ConfirmButton confirmButton;
	private UndoButton undoButton;

	public Hexagon undoHexagon;
	public Hexagon undoHexagon2;
    public Hexagon undoHexagonPie;
    public Hexagon undoHexagonPie2;
	public int turnTracker = 0;
    private boolean ai;

    private Bot bot;

    private PieButton pieButton;

    public GameScreen(Omega game,boolean ai){
        this.game = game;
        this.ai = ai;
        System.out.println(ai);
    }

    @Override
    public void show() {
		numberOfHex = 0;
		round = 1;
		field = new ArrayList<>();

        switch (MenuScreen.mapChoice){
            case (0):createHexagonFieldDefault();break;
            case (1):createHexagonFieldSnowFlake();break;
            case (2):createHexagonFieldSimple();break;
            case (3):createHexagonFieldBug();break;
        }

		SCREENWIDTH = Gdx.graphics.getWidth();
		SCREENHEIGHT = Gdx.graphics.getHeight();
		SEngine = new ScoringEngine();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		stopGame = false;
        hexPlaced = 0;
		arrowPlayerOne = true;
		redTileTexture = new Texture(Gdx.files.internal("HexRed.png"));
		blueTileTexture = new Texture(Gdx.files.internal("HexBlue.png"));
        confirmButton = new ConfirmButton(100,60, game.mainBatch);
		undoButton = new UndoButton(1000, 60, game.mainBatch, false);
        pieButton = new PieButton(1000, 120, game.mainBatch);

        //Choose any bot here that extends Bot abstract class
        bot = new OLABot();
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            game.setScreen(new MenuScreen(game));
        }

		// Reset screen after every render tick
		ScreenUtils.clear(0.90f,1.00f,1.00f, 1);
		// start sprite batch
		game.mainBatch.begin();

		// update hex field check below for info.
		updateHexField();
		updateScore();
        if(ai)
		    whoIsPlayingAI();
        else
            whoIsPlaying();
        confirmButton.update();
        if(!(round==1)){
            undoButton.update();
            undoButton.setActivated(true);
            font.draw(game.mainBatch, "Undo move", 1013, 90);
        }
        if (round == 1 && turnTracker == 3) {
            pieButton.update();
            font.draw(game.mainBatch, "Switch?", 1025, 152);
        }

		// Draw text on screen
		font.draw(game.mainBatch, "Score Of Player Two (Blue): " + SEngine.getBlueScore(), 1000, 700);
		font.draw(game.mainBatch, "Score Of Player One (Pink): " + SEngine.getRedScore(), 100, 700);
		font.draw(game.mainBatch, "Round " + round, 640, 690);

        if(arrowPlayerOne)
            font.draw(game.mainBatch, "Player One's Turn", 610, 670);
        else
            font.draw(game.mainBatch, "Player Two's Turn", 610, 670);

        font.draw(game.mainBatch, "Confirm move", 105, 90);
        font.draw(game.mainBatch, "Press ESC to return to main menu", 5, 16);

        if (firstColor) {
			game.mainBatch.draw(redTileTexture, 700, 70);
			font.draw(game.mainBatch, "The next colour is : ", 550, 100);
		} else {
            game.mainBatch.draw(blueTileTexture, 700, 70);
            font.draw(game.mainBatch, "The next colour is : ", 550, 100);
        }

		game.mainBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void createHexagonFieldDefault() {
        int s;
        int fieldsize = 5;
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
                    numberOfHex += 1;
                }
            }
        }
        System.out.println(numberOfHex);
    }

    public void createHexagonFieldBug() {

        int s;
        int fieldsize = 5;
            for (int r = fieldsize; r >= -fieldsize; r--) {
                if(r>3||r<-3) {
                    field.add(new Hexagon(0, r, 50, game.mainBatch));
                    field.add(new Hexagon(r, 0, 50, game.mainBatch));
                    numberOfHex += 1;
                }
            }

        //legs

        field.add(new Hexagon(-4, 4, 50, game.mainBatch));
        field.add(new Hexagon(-5, 5, 50, game.mainBatch));

        field.add(new Hexagon(4, -4, 50, game.mainBatch));
        field.add(new Hexagon(5, -5, 50, game.mainBatch));

        field.add(new Hexagon(-5, -1, 50, game.mainBatch));
        field.add(new Hexagon(-1, -5, 50, game.mainBatch));

        field.add(new Hexagon(6, 0, 50, game.mainBatch));
        field.add(new Hexagon(0, 6, 50, game.mainBatch));

        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize&&r<4&&r>-4&&q<4&&q>-4) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
                    numberOfHex += 1;
                }
            }
        }
    }

    public void createHexagonFieldSimple() {
        int s;
        int fieldsize = 5;
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = 2; r >= -2; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
                    numberOfHex += 1;
                }
            }
        }
    }

    public void createHexagonFieldSnowFlake() {
        int s;
        int fieldsize = 7;
        for (int q = -fieldsize-3; q <= fieldsize+3; q++) {
            for (int r = fieldsize-1; r >= -fieldsize+1; r--) {
                s = -q - r;
                if (s <= fieldsize+3 && s >= -fieldsize-3 && s!=3&& s!=-3&&r!=3&& r!=-3&&q!=3&& q!=-3) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
                    numberOfHex += 1;
                }
            }
        }
    }

    /**
     * Loops through the field arraylist and updates each tile.
     * checks if the mouse is pressing the current tile "h" - if so
     * sets the current tiles state to "RED".
     **/
    public void updateHexField() {
        for (Hexagon h : field) {// for each tile in the field array

            // check if any tiles have the hover sprite while not being hovered over
            if (!h.mouseHover() && h.getMyState() == Hexagon.state.HOVER) {
                h.setMyState(Hexagon.state.BLANK);
            }

            // add the hover sprite to the currently hovered over tile
            if (h.mouseHover() & !stopGame) {
                if (h.getMyState() == Hexagon.state.BLANK) {
                    h.setMyState(Hexagon.state.HOVER);

                }
            }

            if (h.mouseDown() && !stopGame) {// check if mouse is clicking current tile
                if (h.getMyState() == Hexagon.state.HOVER) {
                    updateColor(h);
                    if(undoHexagon == null){
						undoHexagon = h;
					}
					else{
						undoHexagon2 = h;
					}
                    numberOfHex--;
                    hexPlaced++;
					turnTracker++;
					}
				else{
                    System.out.println("you cannot colour that hexagon");
                }
            }

			if(undoButton.mouseDown() && turnTracker == 2){ // undo IFF p1 or p2 turn is over
				undo();
				undoHexagon = null;
				undoHexagon2 = null;
			}
			if(undoButton.mouseDown() && turnTracker == 5){
				undo();
				undoHexagon = null;
				undoHexagon2 = null;
			}
            if(pieButton.mouseDown() && round == 1 && turnTracker == 3){
                for (Hexagon a:field){
                    if(undoHexagonPie.equals(a)){
                        a.setMyState(Hexagon.state.BLUE);
                    }
                    if(undoHexagonPie2.equals(a)){
                        a.setMyState(Hexagon.state.RED);
                    }
                }
                turnTracker=5;


            }
            h.update();// this redraws the tile updating its position and texture.
        }
    }

    public void undo(){

        for (Hexagon h:field){
            if(undoHexagon.equals(h)){
                h.setMyState(Hexagon.state.BLANK);
            }
            if(undoHexagon2.equals(h)){
                h.setMyState(Hexagon.state.BLANK);
            }
            if(turnTracker == 2){              // set the tracker to the right value depending on who is playing
                numberOfHex = numberOfHex + 2; // not great code but works around the if-statements
                turnTracker = 0;
            }
            if(turnTracker == 5){
                numberOfHex = numberOfHex + 2;
                turnTracker = 3;
            }

            stopGame = false;
        }
	}

    public void updateColor(Hexagon h) {
        if (firstColor) {
            h.setMyState(Hexagon.state.RED);
            firstColor = false;
        } else if (!firstColor) {
            h.setMyState(Hexagon.state.BLUE);
            firstColor = true;
        }
    }

    public void updateScore() {
        SEngine.calculate(field);
    }

    public void whoIsPlaying() {
        if(hexPlaced>=2){        // turn tracker: 0 = p1 first stone, 1 = p1 second stone, 3 & 4 = same for p2
			if(turnTracker > 2){ //               2 = end of p1 turn, 5 = end of p2 turn
				arrowPlayerOne = false;         //6 = end of the round
			}
			if(turnTracker == 2){
				stopGame = true;
			}
			if(turnTracker == 5){
				stopGame = true;
			}
			if(confirmButton.mouseDown() && (turnTracker == 2 || turnTracker == 5) ){ //added the condition that you can only press it when the 2 hexs are placed
				stopGame = false;
				turnTracker++;
                if(turnTracker==3 && round == 1){
                    undoHexagonPie = undoHexagon;
                    undoHexagonPie2 = undoHexagon2;
                    undoHexagon = null;
                    undoHexagon2 = null; // reset the undo temp variables after confirm
                }
                else{
                    undoHexagon = null;
                    undoHexagon2 = null; // reset the undo temp variables after confirm
                }

			}
			if(turnTracker==6){
                arrowPlayerOne = true;
                turnTracker = 0; // reset the tracker
                hexPlaced = 0;
                round++;
                if (numberOfHex < 4) {
                    gameFinish();
                    System.out.println(numberOfHex);
                }
            }
        }
    }

    public void whoIsPlayingAI() {
        if(hexPlaced>=2){        // turn tracker: 0 = p1 first stone, 1 = p1 second stone, 3 & 4 = same for p2
            if(turnTracker > 2){ //               2 = end of p1 turn, 5 = end of p2 turn
                arrowPlayerOne = false;         //6 = end of the round
            }
            if(turnTracker == 2){
                stopGame = true;
            }
            if(confirmButton.mouseDown() && (turnTracker == 2 || turnTracker == 5) ){ //added the condition that you can only press it when the 2 hexs are placed
                stopGame = false;
                turnTracker++;
                undoHexagon = null;
                undoHexagon2 = null; // reset the undo temp variables after confirm

            }
            if(turnTracker==3){
                botmove();
            }
            if(turnTracker==6){
                arrowPlayerOne = true;
                turnTracker = 0; // reset the tracker
                hexPlaced = 0;
                round++;
                if (numberOfHex < 4) {
                    gameFinish();
                    System.out.println(numberOfHex);
                }
            }
        }
    }

    private void botmove(){
        bot.execMove(field);
        System.out.println("Bot move took a runtime of: " + bot.getRuntime() + " micro seconds");
        numberOfHex=numberOfHex-2;
        hexPlaced=hexPlaced+2;
        turnTracker=turnTracker+3;
        stopGame = false;
        undoHexagon = null;
        undoHexagon2 = null;
    }

    public void gameFinish() {
        font.draw(game.mainBatch, " Game has ended ", 600, 800);
        stopGame = true;
    }

}
