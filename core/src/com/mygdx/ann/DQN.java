package com.mygdx.ann;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.coordsystem.Hexagon;

public class DQN {

    private ANN TARGETNET;
    private ANN MAINNET;

    private ArrayList<Hexagon> state;

    private final double EPSILON = 0.1;

    int boardsize;

    public DQN() {

        state = createState();
        boardsize = state.size();

        TARGETNET = new ANN(boardsize*2, boardsize, 2, boardsize*2);
        MAINNET = new ANN(boardsize*2, boardsize, 2, boardsize*2);

    }

    public void learn() {

        TARGETNET.init();
        MAINNET.init();

        
    }

    public ArrayList<Hexagon> createState() {
        int s;
        int fieldsize = 2;
        ArrayList<Hexagon> field = new ArrayList<>();
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, 0, 0));
                }
            }
        }
        return field;
    }

}
