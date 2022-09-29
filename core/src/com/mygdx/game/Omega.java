package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

public class Omega extends ApplicationAdapter {

	public static int SCREENWIDTH;
	public static int SCREENHEIGHT;
	private boolean firstColor = true;
	private ShapeRenderer sr;
	private SpriteBatch mainBatch;
	private ArrayList<Hexagon> field;
	private ScoringEngine SEngine;
    private BitmapFont font;
	private int numberOfHex;
	private boolean stopGame;
	private Texture texture1; // Normal arrow
	private Animation<TextureRegion> texture2; // Moving Gif Arrow
	// The library to animate Gif doesn't exist in LIBGDX, I had to take a class created by someone and include it inside the code : GifDecoder.
	float elapsed; // Moving Gif Arrow
	private int hexPlaced;
	private boolean arrowPlayerOne;
	private int round;
	private Texture blueTileTexture;
	private Texture redTileTexture;

	private Hexagon lastHex;

	private ConfirmButtonSprite confirmButton;
	private UndoButton undoButton;

	public Hexagon undoHexagon;

	public Hexagon undoHexagon2;
	public Hexagon tempUndoHex;
	public int turnTracker = 0;




	@Override
	public void create () {
		numberOfHex = 0;
		round = 1;
		sr = new ShapeRenderer();
		mainBatch = new SpriteBatch();
		field = new ArrayList<>();
		createHexagonField(5);
		SCREENWIDTH = Gdx.graphics.getWidth();
		SCREENHEIGHT = Gdx.graphics.getHeight();
		SEngine = new ScoringEngine();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		stopGame = false;
		texture1 = new Texture(Gdx.files.internal("rightarrow.png")); // Arrow still
		texture2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Arrow.gif").read()); // Moving Arrow
		hexPlaced = 0;
		arrowPlayerOne = true;
		redTileTexture = new Texture(Gdx.files.internal("HexRed.png"));
		blueTileTexture = new Texture(Gdx.files.internal("HexBlue.png"));
		confirmButton = new ConfirmButtonSprite(100,100, mainBatch);
		undoButton = new UndoButton(1000, 100, mainBatch);

	}

	@Override
	public void render () {
		elapsed += Gdx.graphics.getDeltaTime(); // For Gif Arrow
		//Reset screen after every render tick
		ScreenUtils.clear(1, 1, 1, 1);

		//start sprite batch
		mainBatch.begin();


		//update hex field check below for info.
		updateHexField();
		updateScore();

		whoIsPlaying();
		System.out.println(turnTracker);

		confirmButton.update();

		undoButton.update();

		if(arrowPlayerOne){
			mainBatch.draw(texture2.getKeyFrame(elapsed), 135, 582, 60, 60); //the arrow GIF to show who's playing
		} else{
			mainBatch.draw(texture2.getKeyFrame(elapsed), 950, 582, 60, 60); //the arrow GIF to show who's playing
		}

		//Draw text on screen
        font.draw(mainBatch, "Score Blue: " + SEngine.getBlueScore(), 200, 620);
		font.draw(mainBatch, "Score Red: " + SEngine.getRedScore(), 1000, 620);
		font.draw(mainBatch, "Round " + round , 640, 620);

		if(firstColor){
			mainBatch.draw(redTileTexture,700,70);
			font.draw(mainBatch, "The next colour is : ", 550, 100);
		}else{
			mainBatch.draw(blueTileTexture,700,70);
			font.draw(mainBatch, "The next colour is : ", 550, 100);
		}

		mainBatch.end();
	}

	@Override
	public void dispose () {
		sr.dispose();
		mainBatch.dispose();
		font.dispose();
	}

	public void createHexagonField(int fieldsize) {
		int s;
		for(int q=-fieldsize; q<=fieldsize; q++) {
			for(int r=fieldsize; r>=-fieldsize; r--) {
				s=-q-r;
				if(s<=fieldsize && s>=-fieldsize) {
					field.add(new Hexagon(q,r,50,mainBatch));
					numberOfHex += 1;
				}
			}
		}
		System.out.println(numberOfHex);
	}
	/** Loops through the field arraylist and updates each tile.
	 * checks if the mouse is pressing the current tile "h" - if so
	 * sets the current tiles state to "RED".
	 * **/
	public void updateHexField(){
		for (Hexagon h:field) {//for each tile in the field array

			//check if any tiles have the hover sprite while not being hovered over
			if (!h.mouseHover() && h.getMyState()== Hexagon.state.HOVER){ 
				h.setMyState(Hexagon.state.BLANK);
			}

			//add the hover sprite to the currently hovered over tile
			if (h.mouseHover() & !stopGame){
				if(h.getMyState()== Hexagon.state.BLANK){
					h.setMyState(Hexagon.state.HOVER);
					
				}
			}

			if(h.mouseDown() && !stopGame){//check if mouse is clicking current tile
				if(h.getMyState()== Hexagon.state.HOVER){
					updateColor(h);
					if(undoHexagon == null){
						undoHexagon = h;
						System.out.println("hex 1 filled");
					}
					else{
						undoHexagon2 = h;
						System.out.println("hex 2 filled");
					}
					numberOfHex--;
					hexPlaced++;
					turnTracker++;
					System.out.println(turnTracker);
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




			h.update();//this redraws the tile updating its position and texture.
		}
	}
	public void updateColor(Hexagon h){
		if(firstColor){
			h.setMyState(Hexagon.state.RED);
			firstColor = false;
		}else if(!firstColor){
			h.setMyState(Hexagon.state.BLUE);
			firstColor = true;
		}
	}

	public void updateScore() {
		SEngine.calculate(field);
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



	public void whoIsPlaying(){
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
				undoHexagon = null;
				undoHexagon2 = null; // reset the undo temp variables after confirm

			}
			if(turnTracker==6){

				arrowPlayerOne=true;

				hexPlaced=0;
				turnTracker = 0; // reset the tracker
				System.out.println(turnTracker);
				round++;
				if(numberOfHex<4){
					gameFinish();
					System.out.println(numberOfHex);
				}
			}
		}
	}
	public void gameFinish(){
		font.draw(mainBatch, " Game has ended ", 600, 800);
		stopGame=true;
	}

}
