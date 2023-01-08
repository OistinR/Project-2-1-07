package com.mygdx.ann.layers;

import java.util.ArrayList;

import com.mygdx.ann.neurons.HidNeuron;

public class HidLayer {
    
    private int size;
    private int prevcount;
    private int layerindex;

    private ArrayList<HidNeuron> hidNeurons;
    
    public HidLayer(int size, int layerindex, int prevcount) {
        this.size = size;
        hidNeurons = new ArrayList<>();
        this.layerindex = layerindex;
        this.prevcount = prevcount;
    }

    public void initialize() {
        for(int i=0; i<size; i++) {
            hidNeurons.add(new HidNeuron(i,layerindex,prevcount));
        }
    }
    
    public ArrayList<HidNeuron> getNeurons() {
        return hidNeurons;
    }

    public int getSize() {
        return size;
    }

}
