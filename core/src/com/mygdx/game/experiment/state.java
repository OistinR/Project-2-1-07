package com.mygdx.game.experiment;

import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class state {
    ArrayList<Double> state;
    Map<String, Double> features;


    state(ArrayList<Hexagon>field){
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
    }

    state(ArrayList<Hexagon>field, Map<String, Double> features){
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

        this.features = features;
        for (String fName: features.keySet()) {
            state.add(features.get(fName));
        }
    }

    public ArrayList<Double> getState() {
        return state;
    }

    public Map<String, Double> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("hexagons: \n");
        for (int i = 0; i < state.size()- features.size(); i++) {
            out.append(state.get(i)+"\n");
        }
        out.append("Features:\n"+ features);
        return out.toString();
    }
}
