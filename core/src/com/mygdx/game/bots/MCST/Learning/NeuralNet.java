package com.mygdx.game.bots.MCST.Learning;

import java.util.List;

public class NeuralNet {

    // Neural network id
    private String id;

    //Neural network input layer
    private NeuralNetLayer inputLayer;

    //Neural network hidden layers
    private List<NeuralNetLayer> hiddenLayer;

    //Neural network output layer
    private NeuralNetLayer outputLayer;

    //Create a neural network with hidden layers
    public NeuralNet(String id, NeuralNetLayer inputLayer, List<NeuralNetLayer> hiddenLayer, NeuralNetLayer outputLayer){
        this.id = id;
        this.inputLayer = inputLayer;
        this.hiddenLayer = hiddenLayer;
        this.outputLayer = outputLayer;
    }

    //Create a neural network without any hidden layers
    public NeuralNet(String id, NeuralNetLayer inputLayer, NeuralNetLayer outputLayer){
        this.id = id;
        this.inputLayer = inputLayer;
        this.outputLayer = outputLayer;
    }

}
