package com.mygdx.game.bots;

import java.util.ArrayList;
import java.util.Random;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;

/** One-look ahead bot 
 *  looks one move ahead and makes decisions based on the bots best move vs the players first move.
 *  it also contains some random behaviour to encourage groups to be formed.
 */
public class OLABot extends Bot{
    //OLA = One Look Ahead

    /**
     * @param field The field of hexagons
     * Chooses best move for bot in current turn (placing their hexagon) and best move against enemy player.
     */
    public void calculate(ArrayList<Hexagon> field) {
         //TURN 1 (random)
         Random r = new Random();
         int rnum;
         boolean turn1=true;

        ScoringEngine se = new ScoringEngine();

        ArrayList<Hexagon> clone1 = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                clone1.add(h.clone());
            }
        } catch (Exception e) {}

        se.calculate(clone1);
        int worstpos=-1;
        int worstscore=se.getRedScore();

        for(Hexagon h:clone1) {
            if(h.getMyState()==Hexagon.state.BLANK) {
                h.setMyState(Hexagon.state.RED);
                se.calculate(clone1);
                if(se.getRedScore()<=worstscore) {
                    worstscore=se.getRedScore();
                    worstpos=clone1.indexOf(h);
                }
                h.setMyState(Hexagon.state.BLANK);
            }
        }

        if(worstpos!=-1) {
            field.get(worstpos).setPlayer(2);
            field.get(worstpos).setMyState(Hexagon.state.RED);
        } else {
            while(turn1) {
                rnum = r.nextInt(field.size());
                if(field.get(rnum).getMyState()==Hexagon.state.BLANK) {
                    field.get(rnum).setPlayer(2);
                    field.get(rnum).setMyState(Hexagon.state.RED);
                    turn1=false;
                }
            }
        }

        //TURN 2 (maximize own score One Look Ahead)
        ArrayList<Hexagon> clone = new ArrayList<Hexagon>();
        int bestpos=-1;

        try {
            for(Hexagon h : field) {
                clone.add(h.clone());
            }
        } catch (Exception e) {}

        se.calculate(clone);
        //save the old original best score before we simulate
        int bestscoreOld = se.getBlueScore();
        int bestscore=se.getBlueScore();

        for(Hexagon h:clone) {
            if(h.getMyState()==Hexagon.state.BLANK) {

                h.setMyState(Hexagon.state.BLUE);
                se.calculate(clone);

                if(bestscore<=se.getBlueScore()) {
                    bestscore=se.getBlueScore();
                    bestpos = clone.indexOf(h);
                }
                h.setMyState(Hexagon.state.BLANK);
            }
        }

        // if the difference between the original score and the "best score we found" is == 2
        // we randomly select a tile instead to encourage groups. works poorly in small maps.

        if(bestscore-bestscoreOld<=2){
            boolean turn2 = true;
            while(turn2) {
                rnum = r.nextInt(field.size());
                if(field.get(rnum).getMyState()==Hexagon.state.BLANK) {
                    field.get(rnum).setPlayer(2);
                    field.get(rnum).setMyState(Hexagon.state.BLUE);
                    turn2=false;
                }
            }
        }
        //otherwise we take the best tile found by simulations
        // Project 1 girls is weird
        else if(bestpos!=-1) {
            field.get(bestpos).setPlayer(2);
            field.get(bestpos).setMyState(Hexagon.state.BLUE);
        } else {
        }
    }

}
