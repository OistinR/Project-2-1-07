package com.mygdx.game.bots;

import java.util.ArrayList;
import java.util.Random;

import com.mygdx.game.coordsystem.Hexagon;

/**
 *  RandomBot randomly chooses an available place and places a tile there.
 */
public class RandomBot extends Bot {

    /**
     * @param field the hexagon field
     * randomly chooses an available place and places a tile there.
     */
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
