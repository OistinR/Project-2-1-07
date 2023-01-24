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
    
    /**
     * Constructor of a hidden neuron 
     * @param index
     * @param layerindex
     * @param prevcount
     */
    public HidNeuron(int index, int layerindex, int prevcount) {
        z=Double.MIN_VALUE;
        h=Double.MIN_VALUE;
        delta=Double.MIN_VALUE;


        this.index = index;
        this.layerindex = layerindex;

        bias = ThreadLocalRandom.current().nextDouble(-0.1,0.1);

        synapses = new ArrayList<>();
        for(int i=0; i<prevcount; i++) {
            synapses.add(new Synapse());
        }
    }

    /**
     * Get the index of the hidden neuron inside its layer
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Return the index of the hidden layer it is in
     * @return
     */
    public int getLayerindex() {
        return layerindex;
    }

    /**
     * Return the bias of this neuron
     * @return
     */
    public double getBias() {
        return bias;
    }

    /**
     * Set a new bias for this neuron
     * @param updatedBias
     */
    public void setBias(double updatedBias) {
        bias=updatedBias;
    }

    /**
     * Returns all the synapses of this neuron
     * @return
     */
    public ArrayList<Synapse> getSynapses() {
        return synapses;
    }

    /**
     * Return the h value
     * @return
     */
    public double getH() {
        return h;
    }

    /**
     * Set a new h value
     * @param newValue
     */
    public void setH(double newValue) {
        h = newValue;
    }

    /**
     * Get the z value
     * @return
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the z value
     * @param newValue
     */
    public void setZ(double newValue) {
        z = newValue;
    }

    /**
     * Get the delta value
     * @return
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Set the new delta value
     * @param newdelta
     */
    public void setDelta(double newdelta) {
        delta=newdelta;
    }
}
