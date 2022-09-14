package com.mygdx.game.coordsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hexagon {
    private int size;

    private int q;
    private int r;
    private int s;

    public enum state{
        BLANK,
        RED,
        BLUE
    }

    private state myState = state.BLANK;
    private SpriteBatch bat;

    private Texture blankTileTexture = new Texture(Gdx.files.internal("Hex.png"));
    private Texture redTileTexture = new Texture(Gdx.files.internal("HexRed.png"));
    private Texture blueTileTexture = new Texture(Gdx.files.internal("HexBlue.png"));
    private Sprite hexSprite;

    public Hexagon (int q, int r, int size, SpriteBatch bat) {
        this.q = q;
        this.r = r;
        this.s = -q-r;
        this.size = size;
        this.bat= bat;

        hexSprite = new Sprite(blankTileTexture,50,50);
        hexSprite.setPosition(1280f/2f + getX(), 720f/2f - getY());//hard coded screen size for now. - or +???
    }


    /** sets texture of sprite based on state of tile.
     * sets position of sprite.
     * draws the sprite based on sprite batch.
     * **/
    public void update(){

        switch (myState) {
            case BLUE:
                hexSprite.setTexture(blueTileTexture);
                break;
            case RED:
                hexSprite.setTexture(redTileTexture);
                break;
            case BLANK:
                hexSprite.setTexture(blankTileTexture);
                break;
        }

        hexSprite.setPosition(1280f/2f + getX(), 720f/2f - getY());//hard coded screen size for now.
        hexSprite.draw(bat);
    }

    /** Checks if mouse is clicking this tile.
     * returns true if mouse is clicking this tile.
     * false otherwise.
     * **/
    public boolean mouseDown(){
        return (Gdx.input.isTouched()&&Gdx.input.getX()>1280f/2f + getX()&&Gdx.input.getX()<1280f/2f + getX()+50
                &&Gdx.input.getY()<(720/2f+getY())&&Gdx.input.getY()>(720/2f+getY())-35); //more hard coded sizes. :(
    }

    public void setMyState(state myState) {
        this.myState = myState;
    }

    public state getMyState() {
        return myState;
    }

    public int getX() {
        return q*size/2 - s*size/2;
    }

    public int getY() {
        return -r*size/2 + q*size/4 + s*size/4;
    }
}
