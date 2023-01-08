package com.mygdx.ann.neurons;

import java.util.ArrayList;

// Java representation of a output neuron with as many synapses as previous neurons

public class OutNeuron {

    private double z;
    private double y;

    private int index;

    private double bias;
    private ArrayList<Synapse> synapses;

    public OutNeuron(int index, int prevcount) {
        z=Double.MIN_VALUE;
        y=Double.MIN_VALUE;

        this.index = index;

        bias = 0;

        synapses = new ArrayList<>();
        for(int i=0; i<prevcount; i++) {
            synapses.add(new Synapse());
        }
    }

    public int getIndex() {
        return index;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double updatedBias) {
        bias=updatedBias;
    }

    public ArrayList<Synapse> getSynapses() {
        return synapses;
    }

    public double getY() {
        return y;
    }

    public void setY(double newValue) {
        y = newValue;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double newValue) {
        z = newValue;
    }
    
}
