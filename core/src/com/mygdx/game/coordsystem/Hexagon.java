package com.mygdx.game.coordsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Omega;

public class Hexagon {
    private int size;

    private int q;
    private int r;
    private int s;

    private int SCREENWIDTH;
	private int SCREENHEIGHT;

    private boolean checked;

    public enum state{
        BLANK,
        RED,
        BLUE,
        HOVER
    }

    private state myState = state.BLANK;
    private SpriteBatch bat;

    private Texture blankTileTexture = new Texture(Gdx.files.internal("Hex.png"));
    private Texture redTileTexture = new Texture(Gdx.files.internal("HexRed.png"));
    private Texture blueTileTexture = new Texture(Gdx.files.internal("HexBlue.png"));
    private Texture highlightTexture = new Texture(Gdx.files.internal("Highlight.png"));
    private Sprite hexSprite;

    public Hexagon (int q, int r, int size, SpriteBatch bat) {
        this.q = q;
        this.r = r;
        this.s = -q-r;
        this.size = size;
        this.bat= bat;
		this.SCREENWIDTH = Gdx.graphics.getWidth();
		this.SCREENHEIGHT = Gdx.graphics.getHeight();
        this.checked = false;

        hexSprite = new Sprite(blankTileTexture,50,50);
        hexSprite.setPosition(SCREENWIDTH/2f + getX(), SCREENHEIGHT/2f - getY());
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
            case HOVER:
                hexSprite.setTexture(highlightTexture);
                break;
        }

        hexSprite.setPosition(SCREENWIDTH/2f + getX(), SCREENHEIGHT/2f - getY());
        hexSprite.draw(bat);
    }


    /** Checks if mouse is clicking this tile.
     * returns true if mouse is clicking this tile.
     * false otherwise.
     * **/
    public boolean mouseDown(){
        //return (Gdx.input.justTouched()&&Gdx.input.getX()>SCREENWIDTH/2f + getX()&&Gdx.input.getX()<SCREENWIDTH/2f + getX()+50&&Gdx.input.getY()<(SCREENHEIGHT/2f+getY())&&Gdx.input.getY()>(SCREENHEIGHT/2f+getY())-35); 

        if(Gdx.input.justTouched()) {
            int colx = (getX()+SCREENWIDTH/2)+25;
            int coly = (getY()+SCREENHEIGHT/2)-25;
            int mousex = Gdx.input.getX();
            int mousey = Gdx.input.getY();

            double d = Math.sqrt(Math.pow(mousex-colx, 2)+Math.pow(mousey-coly, 2));
            if(d<24) {
                return true;
            }
        } return false;
    }

    /** Checks if mouse is hovering this tile.
     * returns true if mouse is hovering this tile.
     * false otherwise.
     * **/
    public boolean mouseHover(){
        int colx = (getX()+SCREENWIDTH/2)+25;
        int coly = (getY()+SCREENHEIGHT/2)-25;
        int mousex = Gdx.input.getX();
        int mousey = Gdx.input.getY();

        double d = Math.sqrt(Math.pow(mousex-colx, 2)+Math.pow(mousey-coly, 2));
        if(d<24) {
            return true;
        } else return false;
    } 

    public void setMyState(state myState) {
        this.myState = myState;
    }

    public state getMyState() {
        return myState;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean flag) {
        checked=flag;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public int getX() {
        return q*size/2 - s*size/2;
    }

    public int getY() {
        return -r*size/2 + q*size/4 + s*size/4;
    }
}
