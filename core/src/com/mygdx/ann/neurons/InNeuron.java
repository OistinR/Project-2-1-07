package com.mygdx.ann.neurons;

// Java representation of a binary input neuron

public class InNeuron {

    private int index;
    private double value;
    
    /**
     * Constructor for the input neuron
     * @param index
     */
    public InNeuron(int index) {
        this.index = index;
        value = Integer.MIN_VALUE;
    }

    /**
     * Get the index of the input neuron
     * @return
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * Get the value of this neuron
     * @return
     */
    public double getValue() {
        return value;
    }

    /**
     *  Set a new value for this neuron 
     * @param newValue
     */
    public void setVal(double newValue) {
        value=newValue;
    }
}
