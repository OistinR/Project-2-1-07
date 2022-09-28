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

import java.util.ArrayList;

public class Omega extends Game {
	public ShapeRenderer sr;

	public SpriteBatch mainBatch;
	@Override
	public void create() {
		mainBatch = new SpriteBatch();
		sr = new ShapeRenderer();
		this.setScreen(new menuScreen(this));
//leTexture = new Texture(Gdx.files.internal("HexBlue.png"));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}


}
