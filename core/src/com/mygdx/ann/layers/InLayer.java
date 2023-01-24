package com.mygdx.ann.layers;

import java.util.ArrayList;

import com.mygdx.ann.neurons.InNeuron;

public class InLayer {

    private int size;

    private ArrayList<InNeuron> inNeurons;
    
    /**
     * Constructor for the input layer
     * @param size
     */
    public InLayer(int size) {
        this.size=size;
        inNeurons = new ArrayList<>();
    }

    /**
     * Initialize the input layer
     */
    public void initialize() {
        for(int i=0; i<size; i++) {
            inNeurons.add(new InNeuron(i));
        }
    }

    /**
     * Get all the input neurons of the input layer
     * @return
     */
    public ArrayList<InNeuron> getNeurons() {
        return inNeurons;
    }

    /**
     * Return the size of the input layer
     * @return
     */
    public int getSize() {
        return size;
    }
}
