package com.mygdx.ann;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

import com.badlogic.gdx.utils.Array;
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

    private final double ACCURACY = 0.001;

    InLayer ILAYER;
    ArrayList<HidLayer> HLAYERS;
    OutLayer OLAYER;


    
    public ANN(int insize, int outsize, int hidnum, int hidsize) {
        this.insize = insize;
        this.outsize = outsize;
        this.hidnum = hidnum;
        this.hidsize = hidsize;
    }   

    public void init() {
        // ! Initialize input layer
        ILAYER= new InLayer(insize);
        ILAYER.initialize();

        // ! Initialize hidden layer(s)
        HLAYERS = new ArrayList<>();
        for(int i=0; i<hidnum; i++) {
            if(i==0) {
                HLAYERS.add(new HidLayer(hidsize, i+1, ILAYER.getSize()));
            } else {
                HLAYERS.add(new HidLayer(hidsize, i+1, hidsize));
            }
            HLAYERS.get(i).initialize();
        }

        //! Initialize output layer
        OLAYER = new OutLayer(outsize, HLAYERS.get(HLAYERS.size()-1).getSize());
        OLAYER.initialize();
    }

    public void exec(ArrayList<Double> values, ArrayList<Double> labels) {
        // ! Change the input values for the ANN
        changeInput(ILAYER, values);

        // ! Call the forward pass and get output values
        ArrayList<Double> forwardPass = forwardProp(ILAYER, HLAYERS, OLAYER, false);

        // ! Run backward propegation
        backwardProp(ILAYER, HLAYERS, OLAYER, forwardPass, labels);
    }

    public void execforandgate() {
        init();
        // ! Set the inputs and labels
        ArrayList<Double> values = new ArrayList<>();
        values.add((double)ThreadLocalRandom.current().nextInt(0, 2)); values.add((double)ThreadLocalRandom.current().nextInt(0, 2));
        changeInput(ILAYER, values);

        ArrayList<Double> labels = new ArrayList<>();
        if(values.get(0)+values.get(1)>1.1) {
            labels.add(1.0); 
        } else labels.add(0.0);


        // ! Call the forward pass and get output values
        ArrayList<Double> forwardPass = forwardProp(ILAYER, HLAYERS, OLAYER, false);

        // ! Run backward propegation
        backwardProp(ILAYER, HLAYERS, OLAYER, forwardPass, labels);

        int loop=1;
        while(loop<=9999999) {
            values.clear();            
            values.add((double)ThreadLocalRandom.current().nextInt(0, 2)); values.add((double)ThreadLocalRandom.current().nextInt(0, 2));
            changeInput(ILAYER, values);

            labels.clear();
            if(values.get(0)+values.get(1)>1.1) {
                labels.add(1.0); 
            } else labels.add(0.0);

            forwardPass = forwardProp(ILAYER, HLAYERS, OLAYER, false);
            backwardProp(ILAYER, HLAYERS, OLAYER, forwardPass, labels);

            // ? Test below
            values.clear();            
            values.add(1.0); values.add(1.0);
            changeInput(ILAYER, values);

            ArrayList<Double> label1 = new ArrayList<>();
            label1.add(1.0); label1.add(0.0);
            ArrayList<Double> forwardPass1 = forwardProp(ILAYER, HLAYERS, OLAYER, false);


            values.clear();            
            values.add(1.0); values.add(0.0);
            changeInput(ILAYER, values);
            
            ArrayList<Double> label2 = new ArrayList<>();
            label2.add(0.0); 
            ArrayList<Double> forwardPass2 = forwardProp(ILAYER, HLAYERS, OLAYER, false);

            double error = computeError(forwardPass1, label1).get(computeError(forwardPass1, label1).size()-1)+computeError(forwardPass2, label2).get(computeError(forwardPass2, label2).size()-1);
            if(loop%2000==0) {
                System.out.println("ERROR OF ANN ..."+ error + "... AT ITERATION "+loop);
            }
            if(error<ACCURACY) {
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
            changeInput(ILAYER, values);

            labels.clear();
            if(input1+input2>1.1) {
                labels.add(1.0);
            } else labels.add(0.0); 
    
            forwardPass = forwardProp(ILAYER, HLAYERS, OLAYER, true);
        }
    }


    public void changeInput(InLayer inputLayer, ArrayList<Double> values) {
        for(int i=0; i<values.size(); i++) {
            inputLayer.getNeurons().get(i).setVal(values.get(i));
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

    public void backwardProp(InLayer inputLayer, ArrayList<HidLayer> HidLayers, OutLayer outputLayer, ArrayList<Double> y, ArrayList<Double> labels) {
        ArrayList<InNeuron> inputneurons = inputLayer.getNeurons();
        ArrayList<OutNeuron> outputneurons = outputLayer.getNeurons();

        // ! Update the input neurons' weights
        for(int i=0; i<outputneurons.size(); i++) {
            for(int j=0; j<outputneurons.get(i).getSynapses().size(); j++) {
                double weight = outputneurons.get(i).getSynapses().get(j).getWeight();

                double dz = outputneurons.get(i).getY() * (1-outputneurons.get(i).getY());
                double dy = -1*(labels.get(i)-outputneurons.get(i).getY());
                
                // ! Set the initial delta of the output neurons
                outputneurons.get(i).setDelta(dz*dy);

                double dw = HidLayers.get(hidnum-1).getNeurons().get(j).getH();

                // ! Change output neurons' weights
                double derivative = dz*dy*dw;
                outputneurons.get(i).getSynapses().get(j).setWeight(weight-(LEARNINGRATE*derivative));
            }
        }

        // ! Update the input neurons' bias
        for(int i=0; i<outputneurons.size(); i++) {
            double bias = outputneurons.get(i).getBias();
                                
            double derivative = outputneurons.get(i).getDelta();
                    
            outputneurons.get(i).setBias(bias-(LEARNINGRATE*derivative));
        }

        // ! Update the hidden layer weights
        for(int p=hidnum-1; p>=0; p--) {
            ArrayList<HidNeuron> hiddenneurons = HidLayers.get(p).getNeurons();

            for(int i=0; i<hiddenneurons.size(); i++) {
                for(int j=0; j<hiddenneurons.get(i).getSynapses().size(); j++) {
                    double weight = hiddenneurons.get(i).getSynapses().get(j).getWeight();

                    double hiddelta=0;
                    if(p==hidnum-1) {
                        for(int k=0; k<outputneurons.size(); k++) {
                            double delta = outputneurons.get(k).getDelta();
                            double theta = outputneurons.get(k).getSynapses().get(i).getWeight();
                            double dz = hiddenneurons.get(i).getH() * (1-hiddenneurons.get(i).getH());
                            hiddelta = hiddelta+(delta*theta*dz);
                        }
                    } else {
                        for(int k=0; k<HidLayers.get(p+1).getNeurons().size(); k++) {
                            double delta = HidLayers.get(p+1).getNeurons().get(k).getDelta();
                            double theta = HidLayers.get(p+1).getNeurons().get(k).getSynapses().get(i).getWeight();
                            double dz = hiddenneurons.get(i).getH() * (1-hiddenneurons.get(i).getH());
                            hiddelta = hiddelta+(delta*theta*dz);
                        }
                    }
                    hiddenneurons.get(i).setDelta(hiddelta);

                    double dw=0;
                    if(p==0) {
                        dw = inputneurons.get(j).getValue();
                    } else {
                        dw = HidLayers.get(p-1).getNeurons().get(j).getH();
                    }

                    double derivative = hiddelta * dw;

                    hiddenneurons.get(i).getSynapses().get(j).setWeight(weight-(derivative*LEARNINGRATE));
                }
            }
        }

        // ! Update the hidden layers' bias
        for(int p=hidnum-1; p>=0; p--) {
            ArrayList<HidNeuron> hiddenneurons = HidLayers.get(p).getNeurons();
            
            for(int i=0; i<hiddenneurons.size(); i++) {
                double bias = hiddenneurons.get(i).getBias();

                double derivative = hiddenneurons.get(i).getDelta();

                hiddenneurons.get(i).setBias(bias-(LEARNINGRATE*derivative));
            }
        }

    }

    public ArrayList<Double> forwardProp(InLayer inputLayer, ArrayList<HidLayer> hiddenLayers, OutLayer outputLayer, boolean last) {
        // ! Compute the values z & h for the neurons in the hidden layer
        // ! It uses the hidden layer synapses and input values of input layer
        for(int k=0; k<hiddenLayers.size(); k++) {
            ArrayList<HidNeuron> hidneuronlist = hiddenLayers.get(k).getNeurons();
            for(int j=0; j<hidneuronlist.size(); j++) {
                double z=0;
                for(int i=0; i<hidneuronlist.get(j).getSynapses().size(); i++) {
                    if(k==0) {
                        z = z + (hidneuronlist.get(j).getSynapses().get(i).getWeight()*inputLayer.getNeurons().get(i).getValue());
                    } else {
                        z = z + hidneuronlist.get(j).getSynapses().get(i).getWeight()*hiddenLayers.get(k-1).getNeurons().get(i).getH();
                    }
                }
                z = z + hidneuronlist.get(j).getBias();
                hidneuronlist.get(j).setZ(z);
                hidneuronlist.get(j).setH(sigmoid(z));
            }
        }


        // ! Compute the values z & y for the neurons in output layer 
        // ! It uses the output layer synapses and h values of hidden layer
        ArrayList<OutNeuron> outneuronlist = outputLayer.getNeurons();
        ArrayList<Double> zlist = new ArrayList<>();

        for(int j=0; j<outneuronlist.size(); j++) {
            double z=0;
            for(int i=0; i<outneuronlist.get(j).getSynapses().size(); i++) {
                z = z + (outneuronlist.get(j).getSynapses().get(i).getWeight()*hiddenLayers.get(hiddenLayers.size()-1).getNeurons().get(i).getH());
            }
            z = z + outneuronlist.get(j).getBias();
            outneuronlist.get(j).setZ(z);

            outneuronlist.get(j).setY(sigmoid(z));

            zlist.add(z);
        }
        for(int i=0; i<outneuronlist.size(); i++) {
            //outneuronlist.get(i).setY(SoftMax(zlist, i));
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
        ANN ann = new ANN(2, 1, 2, 20);
        ann.execforandgate();
    }
}
