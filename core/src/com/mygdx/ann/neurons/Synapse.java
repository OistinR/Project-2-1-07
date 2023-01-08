package com.mygdx.ann.neurons;

// Java representation of a synapse 

public class Synapse {
    
    private double weight;
    
    public Synapse() {
        weight=1;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double updatedweight) {
        weight=updatedweight;
    }
}
