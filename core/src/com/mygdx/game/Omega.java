package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.coordsystem.Hexagon;

public class Omega extends ApplicationAdapter {

	public final int SCREENWIDTH = 1280;
	public final int SCREENHEIGHT = 720;

	private final int TMPCIRCLESIZE = 20;

	private ShapeRenderer sr;

	@Override
	public void create () {
		sr = new ShapeRenderer();
	}

	@Override
	public void render () {
		//Reset screen after every render tick
		ScreenUtils.clear(1, 1, 1, 1);

		//Set color of the rendering of the shapes
		sr.setColor(Color.SALMON);

		//Create and render Hexagon field
		createHexagonField(10);
	}
	
	@Override
	public void dispose () {
		sr.dispose();
	}

	public void createHexagonField(int fieldsize) {
		int s;
		for(int q=-fieldsize; q<=fieldsize; q++) {
			for(int r=fieldsize; r>=-fieldsize; r--) {
				s=-q-r;
				if(s<=fieldsize && s>=-fieldsize) {
					Hexagon hex = new Hexagon(q, r, TMPCIRCLESIZE);
					renderCircle(SCREENWIDTH/2 + hex.getX(), SCREENHEIGHT/2 + hex.getY());
				}
			}
		}
	}

	public void renderCircle(int x, int y) {
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.circle(x, y, TMPCIRCLESIZE-12);
		sr.end();
	}
}
