package com.mygdx.game.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This is the confirm button sprite.
 */
public class ConfirmButton {
    private final int SCREENHEIGHT = 720;
    private final int Xcoordinate;
    private final int Ycoordinate;
    private SpriteBatch spriteBatch;
    private Texture buttonTexture = new Texture("CONFIRM_SPRITE.png");
    private Sprite confirmSprite = new Sprite(buttonTexture);

    /** Standard constructor, sets size to default button size.
     * @param Xcoordinate Initial x Coordination in pixels on screen (LibGDX)
     * @param Ycoordinate Initial y Coordination in pixels on screen (LibGDX)
     * @param spriteBatch The libgdx sprite batch is where all the sprites are rendered from by libgdx
     */
    public ConfirmButton(int Xcoordinate, int Ycoordinate, SpriteBatch spriteBatch){
        this.Xcoordinate = Xcoordinate;
        this.Ycoordinate = Ycoordinate;
        this.spriteBatch = spriteBatch;
        confirmSprite.setSize(100,50);
    }

    /**
     *  This method is called when we want to update the visual aspects of the sprite like location.
     */
    public void update(){
        confirmSprite.setPosition(Xcoordinate,Ycoordinate);
        confirmSprite.draw(spriteBatch);
    }

    /**
     * @return True if the mouse is inside the area of the sprite, false otherwise
     */
    public boolean mouseDown(){
            return (Gdx.input.justTouched()&&Gdx.input.getX()>Xcoordinate&&Gdx.input.getX()< Xcoordinate+100&&Math.abs(SCREENHEIGHT-Gdx.input.getY())>(Ycoordinate)&&Math.abs(SCREENHEIGHT-Gdx.input.getY())<(Ycoordinate)+50);
    }
}