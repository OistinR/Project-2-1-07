package com.mygdx.game.experiment;

import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;

public class GameState {

    ArrayList<Double> state;

    public GameState(){
        state = new ArrayList<>();
    }

    GameState(ArrayList<Hexagon>field, double[] features){
        state = new ArrayList<>();
        for (Hexagon h:field) {
            switch (h.getMyState()) {
                case RED: state.add(1.0);
                    break;
                case BLUE: state.add(-1.0);
                    break;
                default: state.add(0.0);
                    break;
            }
        }
        state.add(99.0000001);
        for (int i = 0; i < features.length; i++) {
            state.add(features[i]);
        }
    }


    public void update(ArrayList<Hexagon>field, double[] features){
        state = new ArrayList<>();
        for (Hexagon h:field) {
            switch (h.getMyState()) {
                case RED: state.add(1.0);
                    break;
                case BLUE: state.add(-1.0);
                    break;
                default: state.add(0.0);
                    break;
            }
        }
//        state.add(99.0000001);
//        for (int i = 0; i < features.length; i++) {
//            state.add(features[i]);
//        }
    }
    public void update(ArrayList<Hexagon>field){
        state = new ArrayList<>();
        for (Hexagon h:field) {
            switch (h.getMyState()) {
                case RED: state.add(1.0);
                    break;
                case BLUE: state.add(-1.0);
                    break;
                default: state.add(0.0);
                    break;
            }
        }
//        state.add(99.0000001);
//        for (int i = 0; i < features.length; i++) {
//            state.add(features[i]);
//        }
    }

    public ArrayList<Double> getState() {
        return state;
    }

    @Override public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Hexagons: \n");
        for (int i = 0; i < state.size(); i++) {
            out.append(state.get(i)+" ,");
        }
        return out.toString();
    }


}
