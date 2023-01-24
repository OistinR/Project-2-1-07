package com.mygdx.ann.layers;

import java.util.ArrayList;

import com.mygdx.ann.neurons.OutNeuron;

public class OutLayer {

    private int size;
    private int prevcount;

    private ArrayList<OutNeuron> outNeurons;
    
    /**
     * Constructor for the output layer
     * @param size
     * @param prevcount
     */
    public OutLayer(int size, int prevcount) {
        this.size = size;
        outNeurons = new ArrayList<>();
        this.prevcount = prevcount;
    }

    /**
     * Initialize the output layer
     */
    public void initialize() {
        for(int i=0; i<size; i++) {
            outNeurons.add(new OutNeuron(i,prevcount));
        }
    }
    
    /**
     * Get the neurons of the output layer
     * @return
     */
    public ArrayList<OutNeuron> getNeurons() {
        return outNeurons;
    }

    /**
     * Return the size of the output layer
     * @return
     */
    public int getSize() {
        return size;
    }
}
