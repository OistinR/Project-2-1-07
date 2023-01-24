package com.mygdx.ann.layers;

import java.util.ArrayList;

import com.mygdx.ann.neurons.HidNeuron;

public class HidLayer {
    
    private int size;
    private int prevcount;
    private int layerindex;

    private ArrayList<HidNeuron> hidNeurons;
    
    /**
     * Constructor for the hidden layer
     * @param size
     * @param layerindex
     * @param prevcount
     */
    public HidLayer(int size, int layerindex, int prevcount) {
        this.size = size;
        hidNeurons = new ArrayList<>();
        this.layerindex = layerindex;
        this.prevcount = prevcount;
    }

    /**
     * Method to init the hidden layer
     */
    public void initialize() {
        for(int i=0; i<size; i++) {
            hidNeurons.add(new HidNeuron(i,layerindex,prevcount));
        }
    }
    
    /**
     * Get all the neurons inside this hidden layer
     * @return 
     */
    public ArrayList<HidNeuron> getNeurons() {
        return hidNeurons;
    }

    /**
     * Return the size of this layer
     * @return
     */
    public int getSize() {
        return size;
    }

}
