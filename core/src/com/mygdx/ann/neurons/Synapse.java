package com.mygdx.ann.neurons;

import java.util.concurrent.ThreadLocalRandom;

// Java representation of a synapse 

public class Synapse {
    
    private double weight;
    
    public Synapse() {
        weight=ThreadLocalRandom.current().nextDouble(-0.1,0.1);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double updatedweight) {
        weight=updatedweight;
    }
}
