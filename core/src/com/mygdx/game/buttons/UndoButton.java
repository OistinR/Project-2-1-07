package com.mygdx.game.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * This is the confirm button sprite.
 */
public class UndoButton {
    private final int SCREENHEIGHT = 720;
    private float Xcoordinate;
    private float Ycoordinate;
    private SpriteBatch spriteBatch;
    private Texture buttonTexture = new Texture("undo.png");
    private Sprite undoSprite = new Sprite(buttonTexture);
    private boolean activated;

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    /** Standard constructor, sets size to default button size.
     * @param Xcoordinate Initial x Coordination in pixels on screen (LibGDX)
     * @param Ycoordinate Initial y Coordination in pixels on screen (LibGDX)
     * @param spriteBatch The libgdx sprite batch is where all the sprites are rendered from by libgdx
     * @param activated This tells us whether to show the button or not.
     */
    public UndoButton(float Xcoordinate, float Ycoordinate, SpriteBatch spriteBatch, boolean activated){
        this.activated = activated;
        this.Xcoordinate = Xcoordinate;
        this.Ycoordinate = Ycoordinate;
        this.spriteBatch = spriteBatch;
        undoSprite.setSize(100,50);

    }
    /**
     *  This method is called when we want to update the visual aspects of the sprite like location.
     */
    public void update(){
        undoSprite.setPosition(Xcoordinate,Ycoordinate);
        undoSprite.draw(spriteBatch);
    }
    /**
     * @return True if the mouse is inside the area of the sprite, false otherwise
     */
    public boolean mouseDown(){
        return (activated && Gdx.input.justTouched()&&Gdx.input.getX()>Xcoordinate&&Gdx.input.getX()< Xcoordinate+100&&Math.abs(SCREENHEIGHT-Gdx.input.getY())>(Ycoordinate)&&Math.abs(SCREENHEIGHT-Gdx.input.getY())<(Ycoordinate)+50);
    }

}