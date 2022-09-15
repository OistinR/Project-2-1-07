package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;
import java.util.Random;

public class Omega extends ApplicationAdapter {

	public static int SCREENWIDTH;
	public static int SCREENHEIGHT;
	public static boolean firstColor = true;

	private ShapeRenderer sr;
	private SpriteBatch mainBatch;
	private ArrayList<Hexagon> field;

	@Override
	public void create () {
		sr = new ShapeRenderer();
		mainBatch = new SpriteBatch();
		field = new ArrayList<>();
		createHexagonField(5);
		SCREENWIDTH = Gdx.graphics.getWidth();
		SCREENHEIGHT = Gdx.graphics.getHeight();

	}

	@Override
	public void render () {
		//Reset screen after every render tick
		ScreenUtils.clear(1, 1, 1, 1);

		//Set color of the rendering of the shapes
		//sr.setColor(Color.SALMON);


		//start sprite batch
		mainBatch.begin();

		//update hex field check below for info.
		updateHexField();
		mainBatch.end();
	}
	
	@Override
	public void dispose () {
		sr.dispose();
		mainBatch.dispose();
	}

	public void createHexagonField(int fieldsize) {
		int s;
		for(int q=-fieldsize; q<=fieldsize; q++) {
			for(int r=fieldsize; r>=-fieldsize; r--) {
				s=-q-r;
				if(s<=fieldsize && s>=-fieldsize) {
					field.add(new Hexagon(q,r,50,mainBatch));
				}
			}
		}
	}
	/** Loops through the field arraylist and updates each tile.
	 * checks if the mouse is pressing the current tile "h" - if so
	 * sets the current tiles state to "RED".
	 * **/
	public void updateHexField(){
		for (Hexagon h:field) {//for each tile in the field array

			if(h.mouseDown()){//check if mouse is clicking current tile
				if(h.getMyState()== Hexagon.state.BLANK)
					updateColor(h);
				else{
					System.out.println("you cannot colour that hexagon");
				}
				//h.setMyState(Hexagon.state.RED);
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

}
