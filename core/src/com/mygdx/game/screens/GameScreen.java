package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Omega;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.OLABot;
import com.mygdx.game.bots.RandomBot;
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
    private int round;
    private boolean gamefinished;

    private boolean DEBUG = true;

    private String[] stateArray;
    private int stateTracker;

    /**
     * The information about the screen size
     */
    public static int SCREENWIDTH;
    public static int SCREENHEIGHT;
    /**
     * The different hexagon that can be placed and the game logic behind it
     */
    private ArrayList<Hexagon> field;
    private ScoringEngine SEngine;
    public BitmapFont font;
    private Texture blueTileTexture;
    private Texture redTileTexture;
    private Texture greenTileTexture;
    private Texture yellowTileTexture;
    private Omega game;

    private ConfirmButton confirmButton;
    private UndoButton undoButton;

    public Hexagon undoHexagon;
    public Hexagon undoHexagon2;
    public Hexagon undoHexagon3;
    public Hexagon undoHexagon4;
    public Hexagon undoHexagonPie;
    public Hexagon undoHexagonPie2;
    private boolean ai, ai2;

    private Bot bot, bot2;

    private PieButton pieButton;
    private int playerCount;
    private int playerTurn;
    private int colorTurn;
    private int hexCounter;

    /**
     *
     * @param game communicates with the main class to switch between classes
     * @param ai   to see if we are playing against an AI or not
     * @param ai2  to see if we are playing bot vs bot
     */
    public GameScreen(Omega game, boolean ai, boolean ai2, int playerCount) {
        this.game = game;
        this.ai = ai;
        this.ai2 = ai2;
        this.playerCount = playerCount;
        createStateArray();
        stateTracker = 0;
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
        SEngine = new ScoringEngine();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        playerTurn = 1;
        colorTurn = 1;
        hexCounter = 1;
        redTileTexture = new Texture(Gdx.files.internal("HexRed.png"));
        blueTileTexture = new Texture(Gdx.files.internal("HexBlue.png"));
        greenTileTexture = new Texture(Gdx.files.internal("HexGreen.png"));
        yellowTileTexture = new Texture(Gdx.files.internal("HexYellow.png"));
        confirmButton = new ConfirmButton(100, 60, game.mainBatch);
        undoButton = new UndoButton(1000, 60, game.mainBatch, false);
        pieButton = new PieButton(1000, 120, game.mainBatch);

        // Choose any bot here that extends Bot abstract class
        bot = new OLABot();
        bot2 = new RandomBot();
    }

    @Override
    /**
     * Render method render the screen every x times to put new information on the
     * screen when action occur
     */
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            MenuScreen newScreen = new MenuScreen(game);
            newScreen.resetMapChoice();
            game.setScreen(newScreen);
        }

        // Reset screen after every render tick
        ScreenUtils.clear(0.90f, 1.00f, 1.00f, 1);
        // start sprite batch
        game.mainBatch.begin();

        // update hex field check below for info.
        updateHexField();
        updateScore();
        updateState2();
        // System.out.println(STATE);

        // TODO: make a boolean so that it doesnt draw the buttons when BVB
        if (!gamefinished) {
            confirmButton.update();
            font.draw(game.mainBatch, "Confirm move", 105, 90);
        }
        if (!(round == 1) && !gamefinished && playerCount == 2) {
            undoButton.update();
            undoButton.setActivated(true);
            font.draw(game.mainBatch, "Undo move", 1013, 90);
        }
        if (!gamefinished && playerCount != 2){
            undoButton.update();
            undoButton.setActivated(true);
            font.draw(game.mainBatch, "Undo move", 1013, 90);
        }
        if (round == 1 && stateArray[stateTracker].equals("P2P1") && playerCount == 2) {
            pieButton.update();
            font.draw(game.mainBatch, "Switch?", 1025, 152);
        }

        // Draw text on screen
        font.draw(game.mainBatch, "Score Of Player Two (Blue): " + SEngine.getBlueScore(), 1000, 700);
        font.draw(game.mainBatch, "Score Of Player One (Pink): " + SEngine.getRedScore(), 100, 700);
        if (playerCount > 2){
            font.draw(game.mainBatch, "Score Of Player Three (Green): " + SEngine.getGreenScore(), 100, 150);
        }
        if (playerCount > 3){
            font.draw(game.mainBatch, "Score Of Player Four (Yellow): " + SEngine.getYellowScore(), 1000, 150);
        }
        font.draw(game.mainBatch, "Round " + round, 640, 690);

        if (playerTurn == 1)
            font.draw(game.mainBatch, "Player One's Turn", 610, 670);
        else if (playerTurn == 2)
            font.draw(game.mainBatch, "Player Two's Turn", 610, 670);
        else if (playerTurn == 3)
            font.draw(game.mainBatch, "Player Three's Turn", 610,670);
        else if (playerTurn == 4)
            font.draw(game.mainBatch, "Player Four's Turn", 610,670);

        font.draw(game.mainBatch, "Press ESC to return to main menu", 5, 16);

        if (!gamefinished){
            if (colorTurn == 1){
                font.draw(game.mainBatch, "The next colour is : ", 550, 100);
                game.mainBatch.draw(redTileTexture, 700, 70);
            } else if (colorTurn == 2){
                font.draw(game.mainBatch, "The next colour is : ", 550, 100);
                game.mainBatch.draw(blueTileTexture, 700, 70);
            } else if (colorTurn == 3){
                font.draw(game.mainBatch, "The next colour is : ", 550, 100);
                game.mainBatch.draw(greenTileTexture, 700, 70);
            } else if (colorTurn == 4){
                font.draw(game.mainBatch, "The next colour is : ", 550, 100);
                game.mainBatch.draw(yellowTileTexture, 700, 70);
            } else if (colorTurn == 5 && playerCount == 2 && round == 1){
                font.draw(game.mainBatch, "Waiting for player to confirm move", 550, 100);
            } else if (colorTurn == 5){
                font.draw(game.mainBatch, "Waiting for player to confirm or undo move", 550, 100);
            }
        }

        game.mainBatch.end();
    }

    public void updateState2(){
        // check if game is done
        if ((field.size() - numHex()) < (playerCount*playerCount) && stateArray[stateTracker].equals("P1P1")){
            gameFinish();
        }

        // bot v bot stuff idk this is broken
        if (ai2 && ai && (!gamefinished)) {
            botmove();
            bot2move();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            round++;
            return;
        }

        // main loop
        if ((stateTracker + 1)%(playerCount+1) == 0 && confirmButton.mouseDown()){
            //CONFIRM STATE
            colorTurn = 1;
            if (stateTracker == stateArray.length-1) {
                System.out.println("NEW ROUND");
                stateTracker = 0;
                playerTurn = 1;
                round++;
                undoHexagon = null;
                undoHexagon2 = null;
                undoHexagon3 = null;
                undoHexagon4 = null;
            }
            else{
                stateTracker++;
                playerTurn++;
                if (playerCount != 2) {
                    undoHexagon = null;
                    undoHexagon2 = null;
                    undoHexagon3 = null;
                    undoHexagon4 = null;
                }
                else if (numHex() != 2) {
                    undoHexagon = null;
                    undoHexagon2 = null;
                    undoHexagon3 = null;
                    undoHexagon4 = null;
                }
                else{
                    if (DEBUG) System.out.println("PIE");
                }
            }
            if (DEBUG) System.out.println("NEW STATE: " + stateArray[stateTracker]);
        }
        else{
            // NOT CONFIRM STATE
            if (ai && playerTurn != 1) {
                botmove();
                playerTurn = 1;
                stateTracker = 0;
                hexCounter += playerCount;
                round++;
            }
            if ((stateTracker + 1)%(playerCount+1) == 0){
                //change text
                colorTurn = 5;
            }
            if (numHex() == hexCounter){
                hexCounter++;
                stateTracker++;
                colorTurn++;
                if (DEBUG) System.out.println("NEW STATE: " + stateArray[stateTracker]);
            }
            if (round == 1 && !ai && playerCount == 2) {
                undoHexagonPie = undoHexagon;
                undoHexagonPie2 = undoHexagon2;
            }
        }
        return;
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

    public void createStateArray(){
        if (playerCount == 2){
            stateArray = new String[6];
            stateArray[0] = "P1P1";
            stateArray[1] = "P1P2";
            stateArray[2] = "P1P3";
            stateArray[3] = "P2P1";
            stateArray[4] = "P2P2";
            stateArray[5] = "P2P3";
            if (DEBUG){
                for (int i = 0; i < stateArray.length; i++){
                    System.out.print(stateArray[i] + " ");
                }
            }
        }
        else if (playerCount == 3){
            stateArray = new String[12];
            stateArray[0] = "P1P1";
            stateArray[1] = "P1P2";
            stateArray[2] = "P1P3";
            stateArray[3] = "P1P4";
            stateArray[4] = "P2P1";
            stateArray[5] = "P2P2";
            stateArray[6] = "P2P3";
            stateArray[7] = "P2P4";
            stateArray[8] = "P3P1";
            stateArray[9] = "P3P2";
            stateArray[10] = "P3P3";
            stateArray[11] = "P3P4";
            if (DEBUG){
                for (int i = 0; i < stateArray.length; i++){
                    System.out.print(stateArray[i] + " ");
                }
            }
        }
        else if (playerCount == 4){
            stateArray = new String[20];
            stateArray[0] = "P1P1";
            stateArray[1] = "P1P2";
            stateArray[2] = "P1P3";
            stateArray[3] = "P1P4";
            stateArray[4] = "P1P5";
            stateArray[5] = "P2P1";
            stateArray[6] = "P2P2";
            stateArray[7] = "P2P3";
            stateArray[8] = "P2P4";
            stateArray[9] = "P2P5";
            stateArray[10] = "P3P1";
            stateArray[11] = "P3P2";
            stateArray[12] = "P3P3";
            stateArray[13] = "P3P4";
            stateArray[14] = "P3P5";
            stateArray[15] = "P4P1";
            stateArray[16] = "P4P2";
            stateArray[17] = "P4P3";
            stateArray[18] = "P4P4";
            stateArray[19] = "P4P5";
            if (DEBUG){
                for (int i = 0; i < stateArray.length; i++){
                    System.out.print(stateArray[i] + " ");
                }
            }
        }


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
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
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
            if (r > 3 || r < -3) {
                field.add(new Hexagon(0, r, 50, game.mainBatch));
                field.add(new Hexagon(r, 0, 50, game.mainBatch));
            }
        }

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
                if (s <= fieldsize && s >= -fieldsize && r < 4 && r > -4 && q < 4 && q > -4) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
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
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
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
                if (s <= fieldsize + 3 && s >= -fieldsize - 3 && s != 3 && s != -3 && r != 3 && r != -3 && q != 3
                        && q != -3) {
                    field.add(new Hexagon(q, r, 50, game.mainBatch));
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
            if (!gamefinished) {
                // check if any tiles have the hover sprite while not being hovered over
                if (!h.mouseHover() && h.getMyState() == Hexagon.state.HOVER) {
                    h.setMyState(Hexagon.state.BLANK);
                }

                // add the hover sprite to the currently hovered over tile
                if (h.mouseHover() && ((stateTracker + 1)%(playerCount+1) != 0)) {
                    if (h.getMyState() == Hexagon.state.BLANK) {
                        h.setMyState(Hexagon.state.HOVER);

                    }
                }

                if (h.mouseDown() && ((stateTracker + 1)%(playerCount+1) != 0)) {// check if mouse is clicking current
                                                                                  // tile
                    if (h.getMyState() == Hexagon.state.HOVER) {
                        updateColor(h);
                        if (undoHexagon == null) {
                            undoHexagon = h;
                            System.out.println("undoHexagon set");
                        } else if (undoHexagon2 == null){
                            undoHexagon2 = h;
                            System.out.println("undoHexagon2 set");
                        } else if (undoHexagon3 == null && playerCount > 2){
                            undoHexagon3 = h;
                            System.out.println("undoHexagon3 set");
                        } else if (undoHexagon4 == null && playerCount > 3){
                            undoHexagon4 = h;
                            System.out.println("undoHexagon4 set");
                        }
                    } else {
                        System.out.println("you cannot colour that hexagon");
                    }
                }

                if (undoButton.mouseDown() && ((stateTracker + 1)%(playerCount+1) == 0)) { // undo IFF p1 or p2 turn
                                                                                              // is over
                    undo();
                    colorTurn = 1;
                    hexCounter -= playerCount;
                    undoHexagon = null;
                    undoHexagon2 = null;
                    undoHexagon3 = null;
                    undoHexagon4 = null;
                }

                if (pieButton.mouseDown() && round == 1 && stateArray[stateTracker].equals("P2P1") && playerCount == 2) {
                    for (Hexagon a : field) {
                        if (undoHexagonPie.equals(a)) {
                            a.setMyState(Hexagon.state.BLUE);
                        }
                        if (undoHexagonPie2.equals(a)) {
                            a.setMyState(Hexagon.state.RED);
                        }
                    }
                    playerTurn = 1;
                }
            }
            h.update();
        }
    }

    /**
     * All the logic behind the undo button
     */
    public void undo() {
        if (DEBUG) System.out.println("start of undo, state: " + stateArray[stateTracker]);
        if (DEBUG) System.out.println("start of undo, stateTracker: " + stateTracker);
        for (Hexagon h : field) {
            if (undoHexagon.equals(h)) {
                h.setMyState(Hexagon.state.BLANK);
            }
            if (undoHexagon2.equals(h)) {
                h.setMyState(Hexagon.state.BLANK);
            }
            if (playerCount > 2) {
                if (undoHexagon3.equals(h)) {
                    h.setMyState(Hexagon.state.BLANK);
                }
                if (playerCount > 3) {
                    if (undoHexagon4.equals(h)) {
                        h.setMyState(Hexagon.state.BLANK);
                    }
                }
            }
        }
        stateTracker = stateTracker - playerCount;
        if (DEBUG) System.out.println("start of undo, state: " + stateArray[stateTracker]);
        if (DEBUG) System.out.println("end of undo, stateTracker: " + stateTracker);
    }

    /**
     *
     * @param h the hexagon that was select will be coloured in a colour based on
     *          the players turn
     */
    public void updateColor(Hexagon h) {
        if (colorTurn == 1) {
            h.setMyState(Hexagon.state.RED);
        } else if (colorTurn == 2){
            h.setMyState(Hexagon.state.BLUE);
        } else if (colorTurn == 3){
            h.setMyState(Hexagon.state.GREEN);
        } else if (colorTurn == 4){
            h.setMyState(Hexagon.state.YELLOW);
        }
    }

    /**
     * update the score of each player
     */
    public void updateScore() {
        SEngine.calculate(field);
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

    /**
     * Method executed when the game is finished
     */
    public void gameFinish() {
        gamefinished = true;
        font.draw(game.mainBatch, " Game has ended ", 610, 600);
    }

}
