package com.mygdx.game.bots.gametree;

import java.util.ArrayList;
import java.util.Collections;

import com.mygdx.game.coordsystem.Hexagon;

public class Root {
    
    private int roothexq;
    private int roothexr;
    private Hexagon.state rootstate;
    private Hexagon.state playerstate;
    private ArrayList<Integer> leafscores;
    private int positive;
    private int negative;

    /**
     * @param roothexq
     * @param roothexr
     * @param rootstate
     * @param playerstate
     * Root constructor
     */

    public Root(int roothexq, int roothexr, Hexagon.state rootstate, Hexagon.state playerstate) {
        this.roothexq = roothexq;
        this.roothexr = roothexr;
        this.rootstate = rootstate;
        this.playerstate = playerstate;
        positive=0;
        negative=0;

        leafscores = new ArrayList<Integer>();

    }

    /**
     * Second method for adding a score from a leaf
     * @param score
     */
    public void addLeaf2(int score) {
        if(score>0) {
            positive++;
        } else if(score<0) {
            negative++;
        }
    }

    /**
     * @return score for the second method
     */
    public int getScore2() {
        if(playerstate==Hexagon.state.RED) {
            return positive-negative;
        } else if(playerstate==Hexagon.state.BLUE) {
            return negative-positive;
        }
        return 0;
    }

    /**
     * First method for adding a score from a leaf
     * @param score
     */
    public void addLeaf(int score) {
        leafscores.add(score);
    }

    /**
     * @return score for the first method
     */
    public int getScore() {
        if(playerstate==Hexagon.state.RED) {
            if(rootstate==Hexagon.state.RED) {
                return Collections.max(leafscores);
            } else if(rootstate==Hexagon.state.BLUE) {
                return Collections.min(leafscores);
            }
        } else if(playerstate==Hexagon.state.BLUE) {
            if(rootstate==Hexagon.state.RED) {
                return Collections.min(leafscores);
            } else if(rootstate==Hexagon.state.BLUE) {
                return Collections.max(leafscores);
            }
        }
        return 0;
    }

    /**
     * @return rootstate
     */
    public Hexagon.state getRootState() {
        return rootstate;
    }

    /**
     * @return roothexq
     */
    public int getRootQ() {
        return roothexq;
    }

    /**
     * @return roothexr
     */
    public int getRootR() {
        return roothexr;
    }
}
