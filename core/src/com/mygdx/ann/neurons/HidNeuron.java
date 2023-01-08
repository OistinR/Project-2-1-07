package com.mygdx.ann.neurons;

import java.util.ArrayList;

// Java representation of a hidden neuron with as many synapses as previous neurons

public class HidNeuron {

    private double z;
    private double h;

    private int index;
    private int layerindex;

    private double bias;
    private ArrayList<Synapse> synapses;
    
    public HidNeuron(int index, int layerindex, int prevcount) {
        h=Double.MIN_VALUE;

        this.index = index;
        this.layerindex = layerindex;

        bias = 0;

        synapses = new ArrayList<>();
        for(int i=0; i<prevcount; i++) {
            synapses.add(new Synapse());
        }
    }

    public int getIndex() {
        return index;
    }

    public int getLayerindex() {
        return layerindex;
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

    public double getH() {
        return h;
    }

    public void setH(double newValue) {
        h = newValue;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double newValue) {
        z = newValue;
    }
}
