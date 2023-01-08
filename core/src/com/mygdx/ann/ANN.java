package com.mygdx.ann;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

import com.mygdx.ann.layers.HidLayer;
import com.mygdx.ann.layers.InLayer;
import com.mygdx.ann.layers.OutLayer;
import com.mygdx.ann.neurons.HidNeuron;
import com.mygdx.ann.neurons.InNeuron;
import com.mygdx.ann.neurons.OutNeuron;

public class ANN {

    private int insize;
    private int outsize;
    private int hidnum;
    private int hidsize;

    private final double LEARNINGRATE = 0.5;
    private final double ACCURACY = 0.0001;
    
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

        // ! Set the inputs and labels
        ArrayList<Integer> indexes = new ArrayList<>();
        indexes.add(0); indexes.add(1);
        ArrayList<Double> values = new ArrayList<>();
        values.add((double)ThreadLocalRandom.current().nextInt(0, 2)); values.add((double)ThreadLocalRandom.current().nextInt(0, 2));
        changeInput(inLayer, indexes, values);

        ArrayList<Double> labels = new ArrayList<>();
        if(values.get(0)+values.get(1)>1.1 || values.get(0)+values.get(1)<0.1) {
            labels.add(0.99); labels.add(0.01);
        } else labels.add(0.01); labels.add(0.99);

        // ! Set the weights
        ArrayList<Double> weights = new ArrayList<>();
        weights.add(0.15); weights.add(0.20); weights.add(0.25); weights.add(0.30); weights.add(0.40); weights.add(0.45); weights.add(0.50); weights.add(0.55); weights.add(0.15); weights.add(0.20); weights.add(0.25); weights.add(0.30); weights.add(0.40); weights.add(0.45);
        changeWeights(hidLayer, outLayer, weights);

        // ! Set the biases
        ArrayList<Double> biases = new ArrayList<>();
        biases.add(0.5); biases.add(0.5); biases.add(0.5); biases.add(0.5); biases.add(0.5);
        changeBiases(hidLayer, outLayer, biases);

        // ! Call the forward pass and get output values
        ArrayList<Double> forwardPass = forwardProp(inLayer, hidLayer, outLayer, false);

        // ! Run backward propegation
        backwardProp(inLayer, hidLayer, outLayer, forwardPass, labels);

        int loop=1;
        while(loop<=100000) {
            values.clear();            
            values.add((double)ThreadLocalRandom.current().nextInt(0, 2)); values.add((double)ThreadLocalRandom.current().nextInt(0, 2));
            changeInput(inLayer, indexes, values);

            labels.clear();
            if(values.get(0)+values.get(1)>1.1 || values.get(0)+values.get(1)<0.1) {
                labels.add(1.0); labels.add(0.0);
            } else labels.add(0.0); labels.add(1.0);

            forwardPass = forwardProp(inLayer, hidLayer, outLayer, false);
            backwardProp(inLayer, hidLayer, outLayer, forwardPass, labels);

            if(computeError(forwardPass, labels).get(computeError(forwardPass, labels).size()-1)<ACCURACY) {
                System.out.println("Solution found at iteration number "+loop);
                loop = Integer.MAX_VALUE-1;
            }
            loop++;
        }

        while(true) {
            Scanner in = new Scanner(System.in);
            System.out.println("Input of first neuron (must be 0 or 1): ");
            double input1 = in.nextDouble();
            System.out.println("Input of second neuron (must be 0 or 1):");
            double input2 = in.nextDouble();
    
            values.clear();            
            values.add(input1); values.add(input2);
            changeInput(inLayer, indexes, values);

            labels.clear();
            if(input1+input2>1.1 || input1+input2<0.1) {
                labels.add(1.0); labels.add(0.0);
            } else labels.add(0.0); labels.add(1.0);
    
            forwardPass = forwardProp(inLayer, hidLayer, outLayer, true);
            computeError(forwardPass, labels);
        }
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
        int othercounter=0;
        for(int i=0; i<hiddenLayer.getNeurons().size(); i++) {
            for(int j=0; j<hiddenLayer.getNeurons().get(i).getSynapses().size(); j++) {
                hiddenLayer.getNeurons().get(i).getSynapses().get(j).setWeight(weights.get(othercounter));
                othercounter++;
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
            errors.add((labels.get(i) - y.get(i))*(labels.get(i) - y.get(i)));
            totalerror=totalerror+(labels.get(i) - y.get(i))*(labels.get(i) - y.get(i));
        }
        totalerror=totalerror/errors.size();
        errors.add(totalerror);

        return errors;
    }

    public void backwardProp(InLayer inputLayer, HidLayer hiddenLayer, OutLayer outputLayer, ArrayList<Double> y, ArrayList<Double> labels) {
        // ! Compute the errors
        ArrayList<Double> errors = computeError(y, labels);
        double totalerror = errors.get(errors.size()-1);

        ArrayList<InNeuron> inneurons = inputLayer.getNeurons();
        ArrayList<HidNeuron> hidneurons = hiddenLayer.getNeurons();
        ArrayList<OutNeuron> outneurons = outputLayer.getNeurons();


        // ! Update the output layer neuron weights
        // * Loop through the output neurons
        for(int i=0; i<outneurons.size(); i++) {
            // * Loop through the neurons' synapses
            for(int j=0; j<outneurons.get(i).getSynapses().size(); j++) {
                double w = outneurons.get(i).getSynapses().get(j).getWeight();
                double dz = outneurons.get(i).getY() * (1-outneurons.get(i).getY());
                double dw = hidneurons.get(j).getH();
                double dy = -1*(labels.get(i)-outneurons.get(i).getY());

                double derivative = dz*dw*dy;
                if(Double.isNaN(w)==true) {
                }
                outneurons.get(i).getSynapses().get(j).setWeight(w-(LEARNINGRATE*derivative));
            }
        }

        // ! Update the hidden layer neuron weights
        for(int i=0; i<hidneurons.size(); i++) {

            for(int j=0; j<hidneurons.get(i).getSynapses().size(); j++) {
                double w = hidneurons.get(i).getSynapses().get(j).getWeight();
                double dz = hidneurons.get(i).getH() * (1-hidneurons.get(i).getH());
                double dw = inneurons.get(j).getValue();

                double dh = 0;
                for(int k=0; k<outneurons.size(); k++) {
                    double d3 = outneurons.get(k).getSynapses().get(i).getWeight();
                    double d1 = outneurons.get(k).getY() * (1-outneurons.get(k).getY());
                    double d2 = -1*(labels.get(k)-outneurons.get(k).getY());
                    dh = dh + (d3*(d1*d2));
                }

                double derivative = dz*dw*dh;
                hidneurons.get(i).getSynapses().get(j).setWeight(w-(LEARNINGRATE*derivative));
            }
        }
    }

    public ArrayList<Double> forwardProp(InLayer inputLayer, HidLayer hiddenLayer, OutLayer outputLayer, boolean last) {
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
            for(int i=0; i<outneuronlist.get(j).getSynapses().size(); i++) {
                z = z + (outneuronlist.get(j).getSynapses().get(i).getWeight()*hiddenLayer.getNeurons().get(i).getH());
            }
            z = z + outneuronlist.get(j).getBias();
            outneuronlist.get(j).setZ(z);

            zlist.add(z);
        }
        for(int i=0; i<outneuronlist.size(); i++) {
            outneuronlist.get(i).setY(SoftMax(zlist, i));
        }

        // ? Test printing values at hidden neurons
        for (int i = 0; i<hidneuronlist.size(); i++) {
            //System.out.println(hidneuronlist.get(i).getH());
        }

        // * Printing and returning list with final probabilities
        ArrayList<Double> prob = new ArrayList<>();
        for(int i=0; i<outneuronlist.size(); i++) {
            if(last) {
                System.out.println("Neuron at index "+ i +" has probability (value) p: "+outneuronlist.get(i).getY());
            }
            prob.add(outneuronlist.get(i).getY());
        }
        if (last) {
            System.out.println(" ");
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
        ANN ann = new ANN(2, 2, 1, 3);
        ann.exec();
    }
}
