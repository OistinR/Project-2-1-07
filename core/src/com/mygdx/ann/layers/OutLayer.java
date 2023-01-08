package com.mygdx.ann.layers;

import java.util.ArrayList;

import com.mygdx.ann.neurons.OutNeuron;

public class OutLayer {

    private int size;
    private int prevcount;

    private ArrayList<OutNeuron> outNeurons;
    
    public OutLayer(int size, int prevcount) {
        this.size = size;
        outNeurons = new ArrayList<>();
        this.prevcount = prevcount;
    }

    public void initialize() {
        for(int i=0; i<size; i++) {
            outNeurons.add(new OutNeuron(i,prevcount));
        }
    }
    
    public ArrayList<OutNeuron> getNeurons() {
        return outNeurons;
    }

    public int getSize() {
        return size;
    }
}
