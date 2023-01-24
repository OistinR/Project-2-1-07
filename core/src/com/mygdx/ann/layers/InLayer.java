package com.mygdx.ann.layers;

import java.util.ArrayList;

import com.mygdx.ann.neurons.InNeuron;

public class InLayer {

    private int size;

    private ArrayList<InNeuron> inNeurons;
    
    public InLayer(int size) {
        this.size=size;
        inNeurons = new ArrayList<>();
    }

    public void initialize() {
        for(int i=0; i<size; i++) {
            inNeurons.add(new InNeuron(i));
        }
    }

    public ArrayList<InNeuron> getNeurons() {
        return inNeurons;
    }

    public int getSize() {
        return size;
    }
}
