package com.mygdx.game.bots.MCST.Learning;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
    //Neurons identifer
    private String id;

    //List of all neurons input connection
    protected List<Neuron_Connection> inputConnections;

    //List of all neurons output connection
    protected List<Neuron_Connection> outputConnections;

    //Summing function for the neuron
    protected WeightedSumFunction inputSumFunction;

    //Activation function for the neuron
    //protected ActivationFunction activationFunction;

    private final IActivationFunction activationFunction = new Sigmoid();

    //Default constructor
    public Neuron(){
        this.inputConnections = new ArrayList<>();
        this.outputConnections = new ArrayList<>();
    }

    public double calculateOutput(){
        double totalInput = inputSumFunction.collectOutput(inputConnections);

        return activationFunction.output(totalInput);
    }
}
