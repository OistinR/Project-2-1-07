package com.mygdx.ann.neurons;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

// Java representation of a output neuron with as many synapses as previous neurons

public class OutNeuron {

    private double z;
    private double y;

    private double delta;

    private int index;

    private double bias;
    private ArrayList<Synapse> synapses;

    /**
     * Constructor for the output neuron
     * @param index
     * @param prevcount
     */
    public OutNeuron(int index, int prevcount) {
        z=Double.MIN_VALUE;
        y=Double.MIN_VALUE;
        delta=Double.MIN_VALUE;

        this.index = index;

        bias = ThreadLocalRandom.current().nextDouble(-0.1,0.1);

        synapses = new ArrayList<>();
        for(int i=0; i<prevcount; i++) {
            synapses.add(new Synapse());
        }
    }

    /**
     * Get the index of this output neuron
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get the bias of this neuron
     * @return
     */
    public double getBias() {
        return bias;
    }

    /**
     * Set a new bias
     * @param updatedBias
     */
    public void setBias(double updatedBias) {
        bias=updatedBias;
    }

    /**
     * Get the synapses
     * @return
     */
    public ArrayList<Synapse> getSynapses() {
        return synapses;
    }

    /**
     * Get the y value
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * Set a new y value
     * @param newValue
     */
    public void setY(double newValue) {
        y = newValue;
    }

    /**
     * Get the z value
     * @return
     */
    public double getZ() {
        return z;
    }

    /**
     * Set a new z value
     * @param newValue
     */
    public void setZ(double newValue) {
        z = newValue;
    }

    /**
     * Get the delta value
     * @return
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Set a new delta value
     * @param newdelta
     */
    public void setDelta(double newdelta) {
        delta=newdelta;
    }
    
}
