package com.mygdx.ann.neurons;

// Java representation of a binary input neuron

public class InNeuron {

    private int index;
    private int value;
    
    public InNeuron(int index) {
        this.index = index;
        value = 0;
    }

    public int getIndex() {
        return index;
    }
    
    public int getValue() {
        return value;
    }

    public void setVal(int newValue) {
        value=newValue;
    }
}
