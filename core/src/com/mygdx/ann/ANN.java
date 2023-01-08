package com.mygdx.ann;

import java.util.ArrayList;

import com.mygdx.ann.layers.HidLayer;
import com.mygdx.ann.layers.InLayer;
import com.mygdx.ann.layers.OutLayer;
import com.mygdx.ann.neurons.HidNeuron;
import com.mygdx.ann.neurons.OutNeuron;

public class ANN {

    private int insize;
    private int outsize;
    private int hidnum;
    private int hidsize;

    private final double LEARNINGRATE = 0.5;
    
    public ANN(int insize, int outsize, int hidnum, int hidsize) {
        this.insize = insize;
        this.outsize = outsize;
        this.hidnum = hidnum;
        this.hidsize = hidsize;
    }   

    public void exec() {
        // ! Init all the layers
        InLayer inLayer = new InLayer(insize);
        inLayer.initialize();
        HidLayer hidLayer = new HidLayer(hidsize, 1, inLayer.getSize());
        hidLayer.initialize();
        OutLayer outLayer = new OutLayer(outsize, hidLayer.getSize());
        outLayer.initialize();

        // ! Set the inputs
        ArrayList<Integer> indexes = new ArrayList<>();
        indexes.add(0); indexes.add(1);
        ArrayList<Double> values = new ArrayList<>();
        values.add(0.05); values.add(0.10);
        changeInput(inLayer, indexes, values);

        // ! Set the weights
        ArrayList<Double> weights = new ArrayList<>();
        weights.add(0.15); weights.add(0.20); weights.add(0.25); weights.add(0.30); weights.add(0.40); weights.add(0.45); weights.add(0.50); weights.add(0.55);
        changeWeights(hidLayer, outLayer, weights);

        // ! Set the biases
        ArrayList<Double> biases = new ArrayList<>();
        biases.add(0.35); biases.add(0.35); biases.add(0.60); biases.add(0.60);
        changeBiases(hidLayer, outLayer, biases);

        // ! Call the forward pass and get output values
        ArrayList<Double> forwardPass = forwardProp(inLayer, hidLayer, outLayer);

        // ! Run backward propegation
        //backwardProp(inLayer, hidLayer, outLayer, forwardPass, null);
    }

    public void changeBiases(HidLayer hiddenLayer, OutLayer outputLayer, ArrayList<Double> biases) {
        for(int i=0; i<hiddenLayer.getSize(); i++) {
            hiddenLayer.getNeurons().get(i).setBias(biases.get(i));
        }

        for(int i=hiddenLayer.getSize(); i<outputLayer.getSize()+hiddenLayer.getSize(); i++) {
            outputLayer.getNeurons().get(i-hiddenLayer.getSize()).setBias(biases.get(i));
        }
    }

    public void changeWeights(HidLayer hiddenLayer, OutLayer outputLayer, ArrayList<Double> weights) {
        for(int i=0; i<hiddenLayer.getNeurons().size(); i++) {
            for(int j=0; j<hiddenLayer.getNeurons().get(i).getSynapses().size(); j++) {
                hiddenLayer.getNeurons().get(i).getSynapses().get(j).setWeight(weights.get(i+j));
            }
        }

        int counter=hiddenLayer.getNeurons().size()*hiddenLayer.getNeurons().get(0).getSynapses().size();
        for(int i=0; i<outputLayer.getNeurons().size(); i++) {
            for(int j=0; j<outputLayer.getNeurons().get(i).getSynapses().size(); j++) {
                outputLayer.getNeurons().get(i).getSynapses().get(j).setWeight(weights.get(counter));
                counter++;
            }
        }
    }

    public void changeInput(InLayer inputLayer, ArrayList<Integer> index, ArrayList<Double> values) {
        // ! Set all the inputs with corresponding index to its value, index and values arraylist must be the same size
        // ! Default input value if nothing is given is 0
        for(int i=0; i<inputLayer.getSize(); i++) {
            inputLayer.getNeurons().get(i).setVal(0);
        }

        for(int i=0; i<index.size(); i++) {
            inputLayer.getNeurons().get(index.get(i)).setVal(values.get(i));
        }
    }

    public ArrayList<Double> computeError(ArrayList<Double> y, ArrayList<Double> labels) {
        // ! Compute errors for all outputs given the labels (y size and labels size must be equal)
        // ! Last position of the errors list which is returned is the total error
        ArrayList<Double> errors = new ArrayList<>();
        double totalerror=0;
        for(int i=0; i<y.size(); i++) {
            errors.add(((labels.get(i) - y.get(i))*(labels.get(i) - y.get(i)))*0.5);
            totalerror=totalerror+errors.get(i);
        }
        errors.add(totalerror);
        return errors;
    }

    public void backwardProp(InLayer inputLayer, HidLayer hiddenLayer, OutLayer outputLayer, ArrayList<Double> y, ArrayList<Double> labels) {
        // ! Compute the errors
        ArrayList<Double> errors = computeError(y, labels);
        double totalerror = errors.get(errors.size()-1);

        ArrayList<OutNeuron> outneurons = outputLayer.getNeurons();
        ArrayList<HidNeuron> hidneurons = hiddenLayer.getNeurons();

        // * Loop through the output neurons
        for(int i=0; i<outneurons.size(); i++) {
            // * Loop through the neurons' synapses
            for(int j=0; j<outneurons.get(i).getSynapses().size(); j++) {
                double w = outneurons.get(i).getSynapses().get(j).getWeight();
                double dz = outneurons.get(i).getY() * (1-outneurons.get(i).getY());
                double dw = hidneurons.get(j).getH();
                double dy = -1*(labels.get(j)-outneurons.get(i).getY());
                double derivative = dz*dw*dy;
                outneurons.get(i).getSynapses().get(j).setWeight(w-(LEARNINGRATE*derivative));
                System.out.println(outneurons.get(i).getSynapses().get(j).getWeight());
            }

        }
    }

    public ArrayList<Double> forwardProp(InLayer inputLayer, HidLayer hiddenLayer, OutLayer outputLayer) {
        // ! Compute the values z & h for the neurons in the hidden layer
        // ! It uses the hidden layer synapses and input values of input layer
        ArrayList<HidNeuron> hidneuronlist = hiddenLayer.getNeurons();

        for(int j=0; j<hidneuronlist.size(); j++) {
            double z=0;
            for(int i=0; i<hidneuronlist.get(j).getSynapses().size(); i++) {
                z = z + (hidneuronlist.get(j).getSynapses().get(i).getWeight()*inputLayer.getNeurons().get(i).getValue());
            }
            z = z + hidneuronlist.get(j).getBias();
            hidneuronlist.get(j).setZ(z);
            hidneuronlist.get(j).setH(sigmoid(z));
        }

        // ! Compute the values z & y for the neurons in output layer 
        // ! It uses the output layer synapses and h values of hidden layer
        ArrayList<OutNeuron> outneuronlist = outputLayer.getNeurons();
        ArrayList<Double> zlist = new ArrayList<>();

        for(int j=0; j<outneuronlist.size(); j++) {
            double z=0;
            System.out.println(" ");
            for(int i=0; i<outneuronlist.get(j).getSynapses().size(); i++) {
                z = z + (outneuronlist.get(j).getSynapses().get(i).getWeight()*hiddenLayer.getNeurons().get(i).getH());
                System.out.println(outneuronlist.get(j).getSynapses().get(i).getWeight()+" weight ");
                System.out.println(hiddenLayer.getNeurons().get(i).getH()+  " * h");
            }
            System.out.println(outneuronlist.get(j).getBias() + " + bias");
            z = z + outneuronlist.get(j).getBias();
            outneuronlist.get(j).setZ(z);

            outneuronlist.get(j).setY(sigmoid(z));

            zlist.add(z);
        }
        for(int i=0; i<outneuronlist.size(); i++) {
            //outneuronlist.get(i).setY(SoftMax(zlist, i));
        }

        // ? Test printing values at hidden neurons
        for (int i = 0; i<hidneuronlist.size(); i++) {
            System.out.println(hidneuronlist.get(i).getH());
        }

        // * Printing and returning list with final probabilities
        ArrayList<Double> prob = new ArrayList<>();
        for(int i=0; i<outneuronlist.size(); i++) {
            System.out.println("Neuron at index "+ i +" has probability (value) p: "+outneuronlist.get(i).getY());
            prob.add(outneuronlist.get(i).getY());
        }

        return prob;
    }

    public double sigmoid(double x) {
        return 1/(1+Math.exp(-x));
    }

    public double SoftMax(ArrayList<Double> list, int index) {
        double sum=0;
        for(int i=0; i<list.size(); i++) {
            sum=sum+list.get(i);
        }

        return list.get(index)/sum;
    }


    public static void main(String[] args) {
        ANN ann = new ANN(2, 2, 1, 2);
        ann.exec();
    }
}
