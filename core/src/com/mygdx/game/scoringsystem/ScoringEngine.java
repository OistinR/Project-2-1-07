package com.mygdx.game.scoringsystem;

import java.util.ArrayList;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.coordsystem.Hexagon.state;

/**
 * This file is checking the scores of both player and based on the different state of the map, increasing or decreasing
 * the score of the players
 */
public class ScoringEngine {

    private int redScore;
    private int blueScore;
    private int floodcount;

    private ArrayList<Integer> redList;
    private ArrayList<Integer> blueList;

    /**
     *the method sets to 0 all the different variables that we will use to keep track of the scoring
     */
    public ScoringEngine() {
        redScore = 0;
        blueScore = 0;
        floodcount = 0;
        redList = new ArrayList<>();
        blueList = new ArrayList<>();
    }

    /**
     *
     * @param field take the current field with the state of the hexagons and calculates the score of each playing based on that
     */
    public void calculate(ArrayList<Hexagon> field) {
        redScore=0;
        blueScore=0;
        for (Hexagon h:field) {
            if(h.getMyState()==Hexagon.state.RED&&h.getChecked()==false) {
                h.setChecked(true);
                floodcount=1;
                floodfill(h,field,Hexagon.state.RED);
                redList.add(floodcount);
            } else if (h.getMyState()== Hexagon.state.BLUE) {
                h.setChecked(true);
                floodcount=1;
                floodfill(h,field,Hexagon.state.BLUE);
                blueList.add(floodcount);
            }
        }

        if(redList.isEmpty()==false) {
            redScore=1;
        } else {
            redScore=0;
        }

        for(int i=0; i<redList.size(); i++) {
            redScore = redScore * redList.get(i);
        }

        if(blueList.isEmpty()==false) {
            blueScore=1;
        } else {
            blueScore=0;
        }

        for(int i=0; i<blueList.size(); i++) {
            blueScore = blueScore * blueList.get(i);
        }

        blueList.clear();
        redList.clear();
        resetChecked(field);
    }

    /**
     *
     * @param h the current hexagon
     * @param field the field with all the hexagons of the map
     * @param hexstate the enum of the hexagon BLANK RED BLUE
     */
    public void floodfill(Hexagon h, ArrayList<Hexagon> field, state hexstate) {
        if(h.getMyState()!=hexstate) {return;}

        for(Hexagon hex:field) {
            if(hex.getMyState()==hexstate&&hex.getChecked()==false&&hex.getQ()==h.getQ()+1&&hex.getR()==h.getR()) {
                hex.setChecked(true);
                floodcount++;
                floodfill(hex, field, hexstate);
            }
            if(hex.getMyState()==hexstate&&hex.getChecked()==false&&hex.getQ()==h.getQ()-1&&hex.getR()==h.getR()) {
                hex.setChecked(true);
                floodcount++;
                floodfill(hex, field, hexstate);
            }
            if(hex.getMyState()==hexstate&&hex.getChecked()==false&&hex.getQ()==h.getQ()&&hex.getR()==h.getR()+1) {
                hex.setChecked(true);
                floodcount++;
                floodfill(hex, field, hexstate);
            }
            if(hex.getMyState()==hexstate&&hex.getChecked()==false&&hex.getQ()==h.getQ()&&hex.getR()==h.getR()-1) {
                hex.setChecked(true);
                floodcount++;
                floodfill(hex, field, hexstate);
            }
            if(hex.getMyState()==hexstate&&hex.getChecked()==false&&hex.getQ()==h.getQ()-1&&hex.getR()==h.getR()+1) {
                hex.setChecked(true);
                floodcount++;
                floodfill(hex, field, hexstate);
            }
            if(hex.getMyState()==hexstate&&hex.getChecked()==false&&hex.getQ()==h.getQ()+1&&hex.getR()==h.getR()-1) {
                hex.setChecked(true);
                floodcount++;
                floodfill(hex, field, hexstate);
            } 
        }
    }

    /**
     *
     * @param field the field with all the hexagon of the map
     */
    public void resetChecked(ArrayList<Hexagon> field) {
        for(Hexagon h:field) {
            h.setChecked(false);
        }
    }

    /**
     *
     * @return return the score of the red player
     */
    public int getRedScore() {
        return redScore;
    }

    /**
     *
     * @return return the score of the blue player
     */
    public int getBlueScore() {
        return blueScore;
    }
}
