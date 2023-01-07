package com.mygdx.ann.neurons;

import java.util.ArrayList;

// Java representation of a output neuron with as many synapses as previous neurons

public class OutNeuron {

    private int index;

    private int bias;
    private ArrayList<Synapse> synapses;

    public OutNeuron(int index, int prevcount) {
        this.index = index;

        bias = 0;

        for(int i=0; i<prevcount; i++) {
            synapses.add(new Synapse());
        }
    }

    public int getIndex() {
        return index;
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
