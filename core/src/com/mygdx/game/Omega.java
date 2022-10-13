package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.screens.MenuScreen;

/**
 * The Omega class is extending the Game class to create the design of our game
 */

public class Omega extends Game {

	/**
	 * Represent the render of the board
	 */
	public ShapeRenderer sr;
	/**
	 * Represent the render of the board
	 */
	public SpriteBatch mainBatch;
	/**
	 * Put the font of our board
	 */
	public BitmapFont font;

	@Override
	/**
	 * Create method is called at the start of the game, to create the game sprite, font and shape
	 */
	public void create() {
		mainBatch = new SpriteBatch();
		font = new BitmapFont();
		sr = new ShapeRenderer();
		this.setScreen(new MenuScreen(this));
	}

	@Override
	/**
	 * The render method is called every x time to render the screen
	 */
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

}
