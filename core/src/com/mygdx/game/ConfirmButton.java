package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ConfirmButton {
    private final int SCREENWIDTH = 1280;
    private final int SCREENHEIGHT = 720;
    private final int Xcoordinate;
    private final int Ycoordinate;
    private SpriteBatch spriteBatch;
    private Texture buttonTexture = new Texture("CONFIRM_SPRITE.png");
    private Sprite confirmSprite = new Sprite(buttonTexture);

    ConfirmButton(int Xcoordinate, int Ycoordinate, SpriteBatch spriteBatch){
        this.Xcoordinate = Xcoordinate;
        this.Ycoordinate = Ycoordinate;
        this.spriteBatch = spriteBatch;
        confirmSprite.setSize(100,50);
    }

    public void update(){
        confirmSprite.setPosition(Xcoordinate,Ycoordinate);
        confirmSprite.draw(spriteBatch);
    }

    public boolean mouseDown(){
            return (Gdx.input.justTouched()&&Gdx.input.getX()>Xcoordinate&&Gdx.input.getX()< Xcoordinate+100);//&&Gdx.input.getY()<(Ycoordinate)&&Gdx.input.getY()>(Ycoordinate)-50);
    }


}