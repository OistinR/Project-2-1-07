package com.mygdx.game.bots.MCST.Learning;

import java.util.ArrayList;
import java.util.List;

/**
 * Neural networks can be composed of several linked layers, forming the so-called multilayer networks.
 */
public class NeuralNetLayer {

    //Layers identifier
    private String id;

    //collection of neurons in a layer
    protected List<Neuron> neurons;

    //Create an empty layer with id
    public NeuralNetLayer(String id){
        this.id = id;
        neurons = new ArrayList<>();
    }

    //Creates a layer with a list of neurons and an id.
    public NeuralNetLayer(String id, List<Neuron> neurons){
        this.id = id;
        this.neurons = neurons;
    }
}
