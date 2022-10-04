package com.mygdx.game.bots;

import java.util.ArrayList;
import java.util.Random;

import com.mygdx.game.coordsystem.Hexagon;

public class RandomBot extends Bot {
    
    public void calculate(ArrayList<Hexagon> field) {
        Random r = new Random();
        int rnum;
        boolean turn1=true;
        boolean turn2=true;

        while(turn1) {
            rnum = r.nextInt(field.size());
            if(field.get(rnum).getMyState()==Hexagon.state.BLANK) {
                field.get(rnum).setMyState(Hexagon.state.RED);
                turn1=false;
            }
        }

        while(turn2) {
            rnum = r.nextInt(field.size());
            if(field.get(rnum).getMyState()==Hexagon.state.BLANK) {
                field.get(rnum).setMyState(Hexagon.state.BLUE);
                turn2=false;
            }
        }
    }
}
