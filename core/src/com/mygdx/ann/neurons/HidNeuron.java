package com.mygdx.ann.neurons;

import java.util.ArrayList;

// Java representation of a hidden neuron with as many synapses as previous neurons

public class HidNeuron {

    private int index;
    private int layerindex;

    private int bias;
    private ArrayList<Synapse> synapses;
    
    public HidNeuron(int index, int layerindex, int prevcount) {
        this.index = index;
        this.layerindex = layerindex;

        bias = 0;

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

    public int getBias() {
        return bias;
    }

    public void setBias(int updatedBias) {
        bias=updatedBias;
    }

    public ArrayList<Synapse> getSynapses() {
        return synapses;
    }
}
