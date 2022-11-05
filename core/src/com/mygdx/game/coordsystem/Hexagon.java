package com.mygdx.game.coordsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.GameScreen;

/**
 * This is the general Hexagon-shaped tile object.
 * This object contains both the location of each tile, its state and the visual sprite of the tile.
 * We used Axial coordinates for this project.
 * @see <a href="https://www.redblobgames.com/grids/hexagons/">The hexagon coordinate system we used</a>
 */
public class Hexagon implements Cloneable {
    private int size;
    private boolean DEBUG;
    private int q;
    private int r;
    private int s;
    //placing your own
    //placing your opponents
    private int fitness1;
    private int fitness2;

    private BitmapFont fitnessText = new BitmapFont();

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
    public Hexagon (int q, int r, int size, SpriteBatch bat, int fitness1, int fitness2) {
        this.q = q;
        this.r = r;
        this.s = -q-r;
        this.size = size;
        this.bat= bat;
        this.fitness1 = fitness1;
        this.fitness2 = fitness2;
		this.SCREENWIDTH = Gdx.graphics.getWidth();
		this.SCREENHEIGHT = Gdx.graphics.getHeight();
        this.checked = false;

        //this enables the fitness score being rendered over the hexagon
        DEBUG = true;

        hexSprite = new Sprite(blankTileTexture,50,50);
        hexSprite.setSize(50,50);
        hexSprite.setPosition(SCREENWIDTH/2f + getX(), SCREENHEIGHT/2f - getY());
    }


    /** sets texture of sprite based on state of tile.
     * sets position of sprite.
     * draws the sprite based on sprite batch.
     * **/
    public void update(GameScreen.state stateGame){
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

        String fitness ="";

        switch(stateGame){
            case P1P1:
                fitness = fitness1-fitness2+"";
                break;
            case P1P2:
                fitness = fitness2-fitness1+"";
                break;
            case P2P1:
                fitness = (fitness1-fitness2)*-1+"";
                break;
            case P2P2:
                fitness = (fitness2-fitness1)*-1+"";
                break;
            default:
                fitness= "";
                break;
        }
        if(getMyState()!=Hexagon.state.BLANK&&getMyState()!= state.HOVER){
            fitness="";
        }

        fitnessText.getData().setScale(0.8f);
        hexSprite.setPosition(SCREENWIDTH/2f + getX(), SCREENHEIGHT/2f - getY());
        hexSprite.draw(bat);
        fitnessText.setColor(Color.BLACK);
        //fitnessText.draw(bat,""+fitness1+"/"+fitness2,SCREENWIDTH/2f + getX()+15,SCREENHEIGHT/2f - getY()+30);
        if(DEBUG){
            fitnessText.draw(bat,fitness,SCREENWIDTH/2f + getX()+20,SCREENHEIGHT/2f - getY()+35);
        }

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

    public void setMyFitness(int fitness, int FA) {
        if(FA == 1)
            this.fitness1 = fitness;
        else
            this.fitness2 = fitness;
    }
    //to see the best placement for our colour we need to do fitness1 + -fitness2
    //Same for if we want to see what the best placement is for the opponent colour we need to do -fitness1 + fitness2
    public int getFitness1(){
        return fitness1;
    }
    public int getFitness2(){
        return fitness2;
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

    public int getS() { return -q -r; }

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
