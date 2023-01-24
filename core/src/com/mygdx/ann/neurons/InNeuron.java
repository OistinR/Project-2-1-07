package com.mygdx.ann.neurons;

// Java representation of a binary input neuron

public class InNeuron {

    private int index;
    private double value;
    
    public InNeuron(int index) {
        this.index = index;
        value = Integer.MIN_VALUE;
    }

    public int getIndex() {
        return index;
    }
    
    public double getValue() {
        return value;
    }

    public void setVal(double newValue) {
        value=newValue;
    }
}
