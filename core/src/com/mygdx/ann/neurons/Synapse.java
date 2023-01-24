package com.mygdx.ann.neurons;

import java.util.concurrent.ThreadLocalRandom;

// Java representation of a synapse 

public class Synapse {
    
    private double weight;
    
    /**
     * Synapse constructor
     */
    public Synapse() {
        weight=ThreadLocalRandom.current().nextDouble(-0.1,0.1);
    }

    /**
     * Get the weight of this synapse
     * @return
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Set a new weight for this synapse
     * @param updatedweight
     */
    public void setWeight(double updatedweight) {
        weight=updatedweight;
    }
}
