package com.mygdx.game.bots.MCST.Learning;

//represent a connection between 2 neurons, with the associated weight
public class Neuron_Connection {

    //From this neuron, in this connection
    protected Neuron fromNeuron;

    //To this neuron, in this connection
    protected Neuron toNeuron;

    //The weight of the connection;
    protected double weight;

    //Create a new connection between the neurons with a random weight
    public Neuron_Connection(Neuron fromNeuron, Neuron toNeuron){
        this.fromNeuron = fromNeuron;
        this.toNeuron = toNeuron;
        this.weight = Math.random();

    }

    //Create a new connection between the neuron with a specific weight
    public Neuron_Connection(Neuron fromNeuron, Neuron toNeuron, double weight){
        this(fromNeuron,toNeuron);
        this.weight = weight;
    }

    //Return weight for this connection
    public double getWeight(){
        return weight;
    }

    //Set the weight for this connection
    public void setWeight(double weight){
        this.weight = weight;
    }

    //Returns input of this connection - the activation function result
    public double getInput(){
        return fromNeuron.calculateOutput();
    }

    //Returns the weighted input of this connection
    public double getWeightInput(){
        return fromNeuron.calculateOutput() * weight;
    }

    //Get the from neuron of this connections
    public Neuron getFromNeuron(){
        return fromNeuron;
    }

    //Get the to neuron of this connections
    public Neuron getToNeuron(){
        return toNeuron;
    }
}
