package com.mygdx.game.coordsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
        hexSprite.setPosition(1280f/2f + getX(), 720f/2f + getY());//hard coded screen size for now.
    }

    public void update(){
        System.out.println(getMyState());
        if (myState==state.RED){
            hexSprite.setTexture(redTileTexture);
        }
        else if (myState==state.BLUE){
            hexSprite.setTexture(blueTileTexture);
        }
        else{
            hexSprite.setTexture(blankTileTexture);
        }

        hexSprite.setPosition(1280f/2f + getX(), 720f/2f + getY());//hard coded screen size for now.
        hexSprite.draw(bat);
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
