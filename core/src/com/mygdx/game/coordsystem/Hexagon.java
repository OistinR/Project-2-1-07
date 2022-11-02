package com.mygdx.game.coordsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This is the general Hexagon-shaped tile object.
 * This object contains both the location of each tile, its state and the visual sprite of the tile.
 * We used Axial coordinates for this project.
 * @see <a href="https://www.redblobgames.com/grids/hexagons/">The hexagon coordinate system we used</a>
 */
public class Hexagon implements Cloneable {
    private int size;

    private int q;
    private int r;
    private int s;

    private int SCREENWIDTH;
	private int SCREENHEIGHT;

    private boolean checked;

    /**
     * Enumerator for the various possible tile states
     * @BLANK unowned - the gray blank tile state
     * @RED the tiles belonging to player 1 - the pinkish tile color
     * @BLUE the tiles belonging to player 2 - the blueish tile color
     * @HOVER unowned - tile lighter gray color.
     */
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

    /**
     * @param q the q coordinate of the tile
     * @param r the r coordinate of the tile
     * @param size the width and height of tile
     * @param bat the sprite batch this sprite will be rendered in
     */
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
        hexSprite.setSize(50,50);
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
            if(d<22) {
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
        if(d<22) {
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

    /**
     * @param flag whether this tile has been checked by the scoring engine.
     */
    public void setChecked(boolean flag) {
        checked=flag;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    /**
     * @return gives integer pixel x location of the hexagon
     */
    public int getX() {
        return q*size/2 - s*size/2;
    }

    /**
     * @return gives integer pixel y location of the hexagon
     */
    public int getY() {
        return -r*size/2 + q*size/4 + s*size/4;
    }


    /**
     * @return clones the hexagon via java clone.
     * @throws CloneNotSupportedException
     */
    @Override
    public Hexagon clone() throws CloneNotSupportedException {
        Hexagon newHex = (Hexagon) super.clone();
        return newHex;
    }
}
