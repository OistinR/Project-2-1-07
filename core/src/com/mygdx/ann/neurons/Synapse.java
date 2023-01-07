package com.mygdx.ann.neurons;

// Java representation of a synapse 

public class Synapse {
    
    private int weight;
    
    public Synapse() {
        weight=1;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int updatedweight) {
        weight=updatedweight;
    }
}
