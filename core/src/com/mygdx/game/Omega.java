package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.MenuScreen;

import java.util.ArrayList;

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
