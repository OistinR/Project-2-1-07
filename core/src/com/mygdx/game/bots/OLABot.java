package com.mygdx.game.bots;

import java.util.ArrayList;
import java.util.Random;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class OLABot extends Bot{
    //OLA = One Look Ahead

    public void calculate(ArrayList<Hexagon> field) {
        //TURN 1 (random)
        Random r = new Random();
        int rnum;
        boolean turn1=true;
        while(turn1) {
            rnum = r.nextInt(field.size());
            if(field.get(rnum).getMyState()==Hexagon.state.BLANK) {
                field.get(rnum).setMyState(Hexagon.state.RED);
                turn1=false;
            }
        }

        //TURN 2 (maximize own score One Look Ahead)
        ScoringEngine se = new ScoringEngine();
        ArrayList<Hexagon> clone = new ArrayList<Hexagon>();

        se.calculate(clone);
        int bestscore=se.getBlueScore();
        int bestpos=-1;

        try {
            for(Hexagon h : field) {
                clone.add(h.clone());
            }
        } catch (Exception e) {}

        for(Hexagon h:clone) {
            if(h.getMyState()==Hexagon.state.BLANK) {

                h.setMyState(Hexagon.state.BLUE);
                se.calculate(clone);

                if(bestscore<=se.getBlueScore()) {
                    bestscore=se.getBlueScore();
                    bestpos = clone.indexOf(h);
                }
                clone.get(clone.indexOf(h)).setMyState(Hexagon.state.BLANK);
            }
        }

        if(bestpos!=-1) {
            field.get(bestpos).setMyState(Hexagon.state.BLUE);
        } else {
            System.out.println("No best pos was found an no blue hexagon was placed.");
        }
    }

}
