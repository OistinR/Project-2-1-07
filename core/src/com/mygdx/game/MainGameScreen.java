package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;

import java.util.ArrayList;

public class MainGameScreen implements Screen {

    public static int SCREENWIDTH;
    private menuScreen menu;
    public static int SCREENHEIGHT;
    private boolean firstColor = true;

    private ArrayList<Hexagon> field;
    private ScoringEngine SEngine;
    public BitmapFont font;
    private int numberOfHex;
    private boolean stopGame;
    private Texture texture1; // Normal arrow
    private Animation<TextureRegion> texture2; // Moving Gif Arrow
    // The library to animate Gif doesn't exist in LIBGDX, I had to take a class
    // created by someone and include it inside the code : GifDecoder.
    float elapsed; // Moving Gif Arrow
    private int hexPlaced;
    private boolean arrowPlayerOne;
    private int round;
    private Texture blueTileTexture;
    private Texture redTileTexture;
    private Omega game;
    private Hexagon lastHex;

    public MainGameScreen(Omega game){
        this.game = game;
    }

    @Override
    public void show() {

		numberOfHex = 0;
		round = 1;

		field = new ArrayList<>();
		createHexagonField(5);
		SCREENWIDTH = Gdx.graphics.getWidth();
		SCREENHEIGHT = Gdx.graphics.getHeight();
		SEngine = new ScoringEngine();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		stopGame = false;
		texture1 = new Texture(Gdx.files.internal("rightarrow.png")); // Arrow still
		texture2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Arrow.gif").read()); // Moving			// Arrow
		hexPlaced = 0;
		arrowPlayerOne = true;
		redTileTexture = new Texture(Gdx.files.internal("HexRed.png"));
		blueTileTexture = new Texture(Gdx.files.internal("HexBlue.png"));
    }

    @Override
    public void render(float delta) {

		elapsed += Gdx.graphics.getDeltaTime(); // For Gif Arrow
		// Reset screen after every render tick
		ScreenUtils.clear(1, 1, 1, 1);
		// start sprite batch
		game.mainBatch.begin();

		// update hex field check below for info.
		updateHexField();
		updateScore();
		whoIsPlaying();
		if (arrowPlayerOne) {
			game.mainBatch.draw(texture2.getKeyFrame(elapsed), 135, 582, 60, 60); // the arrow GIF to show who's playing
		} else {
			game.mainBatch.draw(texture2.getKeyFrame(elapsed), 950, 582, 60, 60); // the arrow GIF to show who's playing
		}

		// Draw text on screen
		font.draw(game.mainBatch, "Score Blue: " + SEngine.getBlueScore(), 200, 620);
		font.draw(game.mainBatch, "Score Red: " + SEngine.getRedScore(), 1000, 620);
		font.draw(game.mainBatch, "Round " + round, 640, 620);

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

    public void createHexagonField(int fieldsize) {
        int s;
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
                    numberOfHex--;
                    hexPlaced++;
                } else {
                    System.out.println("you cannot colour that hexagon");
                }
            }

            h.update();// this redraws the tile updating its position and texture.
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
        if (hexPlaced >= 2) {
            arrowPlayerOne = false;
            if (hexPlaced == 4) {
                arrowPlayerOne = true;
                hexPlaced = 0;
                round++;
                if (numberOfHex < 4) {
                    gameFinish();
                    System.out.println(numberOfHex);
                }
            }
        }
    }

    public void gameFinish() {
        font.draw(game.mainBatch, " Game has ended ", 600, 800);
        stopGame = true;
    }

}
