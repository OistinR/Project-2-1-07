package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;
import java.util.Random;

public class Omega extends ApplicationAdapter {

	public final int SCREENWIDTH = 1280;
	public final int SCREENHEIGHT = 720;

	private final int TMPCIRCLESIZE = 20;

	private ShapeRenderer sr;
	private SpriteBatch mainBatch;
	private ArrayList<Hexagon> field;

	@Override
	public void create () {
		sr = new ShapeRenderer();
		mainBatch = new SpriteBatch();
		field = new ArrayList<>();
		createHexagonField(5);
	}

	@Override
	public void render () {
		//Reset screen after every render tick
		ScreenUtils.clear(1, 1, 1, 1);

		//Set color of the rendering of the shapes
		sr.setColor(Color.SALMON);

		mainBatch.begin();
		renderHexField();
//		Hexagon h = new Hexagon(0,0,50,mainBatch);
//		Hexagon t = new Hexagon(1,0,50,mainBatch);
//		h.update(Hexagon.state.BLANK);
//		t.update(Hexagon.state.BLANK);
		mainBatch.end();

		//Create and render Hexagon field
		//createHexagonField(10);
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
	Random r;
	public void renderHexField(){
		for (Hexagon h:field) {
			r = new Random();
			int t = r.nextInt(3);
			switch (t){
				case 0:h.setMyState(Hexagon.state.BLANK);break;
				case 1:h.setMyState(Hexagon.state.RED);break;
				case 2:h.setMyState(Hexagon.state.BLUE);break;
			}

			h.update();

		}
	}

	public void renderCircle(int x, int y) {
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.circle(x, y, TMPCIRCLESIZE-12);
		sr.end();
	}
}
