package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Omega;

import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.FitnessEngine;
import com.mygdx.game.bots.FitnessGroupBot;
import com.mygdx.game.bots.MCST.MCST;
import com.mygdx.game.bots.MCST.Node_MCST;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;

import com.mygdx.game.buttons.ConfirmButton;
import com.mygdx.game.buttons.UndoButton;
import com.mygdx.game.buttons.PieButton;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;

//TODO: Bug where the confirm button is not being pressed by the bot, you have to press it yourself (if you press undo for the bot move then the game crashes)
/**
 * The GameScreen class creates and renders all the different elements on the
 * screen will playing the game
 * GameLogic is also active in this file
 */

public class GameScreen implements Screen {

    public enum state {
        P1P1,
        P1P2,
        P1P3,
        P2P1,
        P2P2,
        P2P3
    }

    private state STATE = state.P1P1;
    private int round;
    private boolean gamefinished;

    /**
     * The information about the screen size
     */
    public static int SCREENWIDTH;
    public static int SCREENHEIGHT;
    private boolean firstColor = true;
    /**
     * The different hexagon that can be placed and the game logic behind it
     */
    private ArrayList<Hexagon> field;
    private ScoringEngine SEngine;
    private FitnessEngine SFitness;
    public BitmapFont font;
    private boolean arrowPlayerOne;
    private Texture blueTileTexture;
    private Texture redTileTexture;
    private Omega game;

    private ConfirmButton confirmButton;
	  private UndoButton undoButton;
    private ConfirmButton backToMenu;

    private Node_MCST bestMove;
    private boolean pieRuleActive;


    public Hexagon undoHexagon;
    public Hexagon undoHexagon2;
    public Hexagon undoHexagonPie;
    public Hexagon undoHexagonPie2;
    private boolean ai, ai2;

    private Bot bot;
    private Bot bot2;
    private MCST botMCST;

    private PieButton pieButton;

    /**
     *
     * @param game communicates with the main class to switch between classes
     * @param ai   to see if we are playing against an AI or not
     * @param ai2  to see if we are playing bot vs bot
     */
    public GameScreen(Omega game, boolean ai, boolean ai2) {
        this.game = game;
        this.ai = ai;
        this.ai2 = ai2;

    }

    @Override
    /**
     * the show method select based on the users input with map to display
     */
    public void show() {
        round = 1;
        gamefinished = false;
        field = new ArrayList<>();

        switch (MenuScreen.mapChoice) {
            case (0):
                createHexagonFieldDefault();
                break;
            case (1):
                createHexagonFieldSnowFlake();
                break;
            case (2):
                createHexagonFieldSimple();
                break;
            case (3):
                createHexagonFieldBug();
                break;
        }


        SCREENWIDTH = Gdx.graphics.getWidth();
        SCREENHEIGHT = Gdx.graphics.getHeight();
        SFitness = new FitnessEngine(Hexagon.state.RED, Hexagon.state.BLUE,false);
        SEngine = new ScoringEngine();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        arrowPlayerOne = true;
        redTileTexture = new Texture(Gdx.files.internal("HexRed.png"));
        blueTileTexture = new Texture(Gdx.files.internal("HexBlue.png"));
        confirmButton = new ConfirmButton(100, 60, game.mainBatch);
        undoButton = new UndoButton(1000, 60, game.mainBatch, false);
        backToMenu = new ConfirmButton(1000, 600, game.mainBatch);
        pieButton = new PieButton(1000, 120, game.mainBatch);

        // Choose any bot here that extends Bot abstract class
        bot2 = new MaxN_Paranoid_Bot(Hexagon.state.BLUE,Hexagon.state.RED);
        bot = new FitnessGroupBot(Hexagon.state.RED,Hexagon.state.BLUE,false);
        botMCST = new MCST();
        //bot2 = new TreeBot(Hexagon.state.BLUE,Hexagon.state.RED);
        pieRuleActive = false;
    }

    @Override
    /**
     * Render method render the screen every x times to put new information on the
     * screen when action occur
     */
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            MenuScreen menuScreen = new MenuScreen(game);
            menuScreen.updateMapChoice();
            game.setScreen(menuScreen);
        }

        // Reset screen after every render tick
        ScreenUtils.clear(0.90f, 1.00f, 1.00f, 1);
        // start sprite batch
        game.mainBatch.begin();

        // update hex field check below for info.
        updateHexField();
        updateScore();
        
        updateState();
        // System.out.println(STATE);

        if(!(ai&&ai2)) {

            if (backToMenu.mouseDown()) {
                this.dispose();
                MenuScreen menuScreen = new MenuScreen(game);
                menuScreen.updateMapChoice();
                game.setScreen(menuScreen);
            }

            font.draw(game.mainBatch, "Back to menu", 1005, 630);
            if (!gamefinished && (STATE == state.P1P3 || STATE == state.P2P3)) {
                confirmButton.update();
                font.draw(game.mainBatch, "Confirm move", 105, 90);
            }

            if (!(round == 1) && !gamefinished && (STATE == state.P1P3 || STATE == state.P2P3)) {
                undoButton.update();
                undoButton.setActivated(true);
                font.draw(game.mainBatch, "Undo move", 1013, 90);
            }
            if (round == 1 && STATE == state.P2P1) {
                pieButton.update();
                font.draw(game.mainBatch, "Switch?", 1025, 152);
            }

            if (undoButton.mouseDown() && (STATE == state.P1P3 || STATE == state.P2P3)) { // undo IFF p1 or p2 turn// is over
                undo();
                undoHexagon = null;
                undoHexagon2 = null;
            }

            if (firstColor && !gamefinished) {
                game.mainBatch.draw(redTileTexture, 700, 70);
                font.draw(game.mainBatch, "The next colour is : ", 550, 100);
            } else if (!gamefinished) {
                game.mainBatch.draw(blueTileTexture, 700, 70);
                font.draw(game.mainBatch, "The next colour is : ", 550, 100);
            }
            backToMenu.update();
            font.draw(game.mainBatch, "Back to menu", 1005, 630);

        }

        // Draw text on screen
        font.draw(game.mainBatch, "Score Of Player Two (Blue): " + SEngine.getBlueScore(), 1000, 700);
        font.draw(game.mainBatch, "Score Of Player One (Pink): " + SEngine.getRedScore(), 100, 700);
        font.draw(game.mainBatch, "Round " + round, 640, 690);

        if (arrowPlayerOne)
            font.draw(game.mainBatch, "Player One's Turn", 610, 670);
        else
            font.draw(game.mainBatch, "Player Two's Turn", 610, 670);

        font.draw(game.mainBatch, "Press ESC to return to main menu", 5, 16);

        game.mainBatch.end();
    }

    public void updateState() {

        int numhex = numHex() - 4 * (round - 1);
        if(pieRuleActive)
            numhex = numHex() - 4 * (round - 1) + 2;


        // check if game is done
        if (field.size() - (numhex + (4 * (round - 1))) < 4 && STATE == state.P1P1) {
            gameFinish();
        }
        if (ai2 && ai && (!gamefinished)) {
            //botmove();
            //bot2move();
            MCSTmove(state.P1P1,true,round);
            MCSTmove(state.P1P2,true,round);

            MCSTmove(state.P2P1,false,round);
            MCSTmove(state.P2P2,false,round);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            round++;
            return;
        }
        // starting moves done by P1

        if ((numhex == 1 && STATE == state.P1P1))  {
            STATE = state.P1P2;
            arrowPlayerOne = true;

        }
        // intermediate move (2nd move for P1)
        else if ((numhex == 2 && (STATE == state.P1P2 || STATE == state.P1P1))) {
            STATE = state.P1P3;
            arrowPlayerOne = true;
        }
        // starting moves done by P2
        else if ((numhex == 3 && STATE == state.P2P1)) {
            STATE = state.P2P2;
            arrowPlayerOne = false;
        }
        // intermediate move (2nd move for P2)
        else if ((numhex == 4 && (STATE == state.P2P2 || STATE == state.P2P1))) {
            STATE = state.P2P3;
            arrowPlayerOne = false;
        }
        // general moves
        if (STATE == state.P1P3 && confirmButton.mouseDown()) {
            STATE = state.P2P1;
            arrowPlayerOne = false;
            if (round == 1 && !ai2) {
                undoHexagonPie = undoHexagon;
                undoHexagonPie2 = undoHexagon2;
            } else if (ai2){
                //bot2move();
                MCSTmove(STATE,false,round);
                updateState();
                if(bestMove.move_played != -2){
                    MCSTmove(STATE,false,round);}
                else
                    pieRuleActive = true ;
                STATE = state.P1P1;
                arrowPlayerOne = true;
                round++;
            }
            //
            undoHexagon = null;
            undoHexagon2 = null;
        }
        if (STATE == state.P2P3 && confirmButton.mouseDown()) {
            STATE = state.P1P1;
            arrowPlayerOne = true;
            undoHexagon = null;
            undoHexagon2 = null;
            round++;
        }



    }

    public int numHex() {
        int num = 0;
        for (Hexagon h : field) {
            if ((h.getMyState() != Hexagon.state.BLANK) && (h.getMyState() != Hexagon.state.HOVER)) {
                num++;
            }
        }
        return num;
    }

    @Override
    /**
     * resize the size of the screen
     */
    public void resize(int width, int height) {

    }

    @Override
    /**
     * pause the screen
     */
    public void pause() {

    }

    @Override
    /**
     * when the game is in pause we can resume back to the game
     */
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /**
     * Creating the objects hexagon to create the default map
     */
    public void createHexagonFieldDefault() {
        int s;
        int fieldsize = 2;
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch,0,0));
                }
            }
        }
    }

    /**
     * When field bug occur the method will reform some parts of the map to make it
     * fit
     */
    public void createHexagonFieldBug() {

        int s;
        int fieldsize = 5;
            for (int r = fieldsize; r >= -fieldsize; r--) {
                if(r>3||r<-3) {
                    field.add(new Hexagon(0, r, 50, game.mainBatch,0,0));
                    field.add(new Hexagon(r, 0, 50, game.mainBatch,0,0));
                }

            }


        field.add(new Hexagon(-4, 4, 50, game.mainBatch,0,0));
        field.add(new Hexagon(-5, 5, 50, game.mainBatch,0,0));

        field.add(new Hexagon(4, -4, 50, game.mainBatch,0,0));
        field.add(new Hexagon(5, -5, 50, game.mainBatch,0,0));

        field.add(new Hexagon(-5, -1, 50, game.mainBatch,0,0));
        field.add(new Hexagon(-1, -5, 50, game.mainBatch,0,0));

        field.add(new Hexagon(6, 0, 50, game.mainBatch,0,0));
        field.add(new Hexagon(0, 6, 50, game.mainBatch,0,0));

        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize&&r<4&&r>-4&&q<4&&q>-4) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch,0,0));
                }
            }
        }
    }

    /**
     * Creating the objects hexagon to create the simple map
     */
    public void createHexagonFieldSimple() {
        int s;
        int fieldsize = 5;
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = 2; r >= -2; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch,0,0));
                }
            }
        }
    }

    /**
     * Creating the objects hexagon to create the SnowFlake map
     */
    public void createHexagonFieldSnowFlake() {
        int s;
        int fieldsize = 7;
        for (int q = -fieldsize - 3; q <= fieldsize + 3; q++) {
            for (int r = fieldsize - 1; r >= -fieldsize + 1; r--) {
                s = -q - r;
                if (s <= fieldsize+3 && s >= -fieldsize-3 && s!=3&& s!=-3&&r!=3&& r!=-3&&q!=3&& q!=-3) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch,0,0));

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
        SFitness.update(field);
        for (Hexagon h : field) {// for each tile in the field array
            if (!gamefinished) {
                // check if any tiles have the hover sprite while not being hovered over
                if (!h.mouseHover() && h.getMyState() == Hexagon.state.HOVER) {
                    h.setMyState(Hexagon.state.BLANK);
                }

            // add the hover sprite to the currently hovered over tile
            if (h.mouseHover() && STATE!=state.P1P3 && STATE!=state.P2P3) {
                if (h.getMyState() == Hexagon.state.BLANK) {
                    h.setMyState(Hexagon.state.HOVER);
                }

                if (h.mouseDown() && STATE != state.P1P3 && STATE != state.P2P3) {// check if mouse is clicking current
                                                                                  // tile
                    if (h.getMyState() == Hexagon.state.HOVER) {
                        updateColor(h);
                        if (undoHexagon == null) {
                            undoHexagon = h;
                        } else {
                            undoHexagon2 = h;
                        }
                    } else {
                        System.out.println("you cannot colour that hexagon");
                    }
                }
            }
                if (pieButton.mouseDown() && round == 1 && STATE == state.P2P1) {
                    for (Hexagon a : field) {
                        if (undoHexagonPie.equals(a)) {
                            a.setMyState(Hexagon.state.BLUE);
                        }
                        if (undoHexagonPie2.equals(a)) {
                            a.setMyState(Hexagon.state.RED);
                        }
                    }
                }

            }
            h.update(STATE);

        }
    }

    /**
     * All the logic behind the undo button
     */
    public void undo() {

        for (Hexagon h : field) {
            if (undoHexagon.equals(h)) {
                h.setMyState(Hexagon.state.BLANK);
            }
            if (undoHexagon2.equals(h)) {
                h.setMyState(Hexagon.state.BLANK);
            }
            if (STATE == state.P1P3) {
                STATE = state.P1P1; // set the tracker to the right value depending on who is playing
            }
            if (STATE == state.P2P3) {
                STATE = state.P2P1;
            }
        }
    }

    /**
     *
     * @param h the hexagon that was select will be coloured in a colour based on
     *          the players turn
     */
    public void updateColor(Hexagon h) {
        if (firstColor) {
            h.setMyState(Hexagon.state.RED);
            firstColor = false;
        } else {
            h.setMyState(Hexagon.state.BLUE);
            firstColor = true;
        }
    }

    /**
     * update the score of each player
     */
    public void updateScore() {
        SEngine.calculate(field);
    }
    public void updateFitness(){
        SFitness.update(field);
    }

    /**
     * check the mouvement of the bot and the time the bot took to place the hexagon
     */
    private void botmove() {
        bot.execMove(field);
        System.out.println("Bot move took a runtime of: " + bot.getRuntime() + " micro seconds");

    }

    /**
     * check the mouvement of the bot and the time of bot2 took to place the hexagon
     */
    private void bot2move() {
        bot2.execMove(field);
        System.out.println("Bot2 move took a runtime of: " + bot2.getRuntime() + " micro seconds");

    }
    private void MCSTmove(state STATE, boolean player1, int round){

        ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                copy_field.add(h.clone());
            }
        } catch (Exception e) {}

        bestMove = botMCST.runMCST(copy_field,STATE,player1,round);
        System.out.println("the best move " + bestMove.move_played);
        System.out.println("after the move the MCST is that % sure to win " + bestMove.returnWinrate());

        if(bestMove.move_played == -2){
            try{
                for(Hexagon h:field){
                    if(h.getMyState() == Hexagon.state.BLUE)
                        h.setMyState(Hexagon.state.RED);
                    else if(h.getMyState() == Hexagon.state.RED)
                        h.setMyState(Hexagon.state.BLUE);
                    copy_field.add(h.clone());
                }
            } catch (Exception e) {}

        }
        else if(bestMove.phase==GameScreen.state.P1P1 || bestMove.phase==GameScreen.state.P2P1)
            field.get(bestMove.move_played).setMyState(Hexagon.state.RED);
        else if(bestMove.phase==GameScreen.state.P1P2 || bestMove.phase==GameScreen.state.P2P2){
            field.get(bestMove.move_played).setMyState(Hexagon.state.BLUE);
        }
        else{
            throw new IllegalStateException("The children phase is not assign correctly: ");
        }
    }


    /**
     * Method executed when the game is finished
     */
    public void gameFinish() {
        gamefinished = true;
        font.draw(game.mainBatch, " Game has ended ", 610, 600);
    }

}
