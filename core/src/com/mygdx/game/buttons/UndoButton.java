package com.mygdx.game.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UndoButton {
    private final int SCREENWIDTH = 1280;
    private final int SCREENHEIGHT = 720;
    private float Xcoordinate;
    private float Ycoordinate;
    private SpriteBatch spriteBatch;
    private Texture buttonTexture = new Texture("undo.png");
    private Sprite undoSprite = new Sprite(buttonTexture);

    public UndoButton(float Xcoordinate, float Ycoordinate, SpriteBatch spriteBatch){
        this.Xcoordinate = Xcoordinate;
        this.Ycoordinate = Ycoordinate;
        this.spriteBatch = spriteBatch;
        undoSprite.setSize(100,50);
        System.out.println(Ycoordinate);
    }

    public void update(){
        undoSprite.setPosition(Xcoordinate,Ycoordinate);
        undoSprite.draw(spriteBatch);
    }

    public boolean mouseDown(){
        return (Gdx.input.justTouched()&&Gdx.input.getX()>Xcoordinate&&Gdx.input.getX()< Xcoordinate+100&&Math.abs(SCREENHEIGHT-Gdx.input.getY())>(Ycoordinate)&&Math.abs(SCREENHEIGHT-Gdx.input.getY())<(Ycoordinate)+50);
    }
}