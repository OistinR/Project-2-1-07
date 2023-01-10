package com.mygdx.ann.neurons;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

// Java representation of a hidden neuron with as many synapses as previous neurons

public class HidNeuron {

    private double z;
    private double h;

    private double delta;

    private int index;
    private int layerindex;

    private double bias;
    private ArrayList<Synapse> synapses;
    
    public HidNeuron(int index, int layerindex, int prevcount) {
        z=Double.MIN_VALUE;
        h=Double.MIN_VALUE;
        delta=Double.MIN_VALUE;


        this.index = index;
        this.layerindex = layerindex;

        bias = ThreadLocalRandom.current().nextDouble();

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

    public double getDelta() {
        return delta;
    }

    public void setDelta(double newdelta) {
        delta=newdelta;
    }
}
