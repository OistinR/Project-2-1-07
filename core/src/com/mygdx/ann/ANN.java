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

    private final double ACCURACY = 0.001;
    
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

        ArrayList<HidLayer> HidLayers = new ArrayList<>();
        for(int i=0; i<hidnum; i++) {
            HidLayers.add(new HidLayer(hidsize, i+1, inLayer.getSize()));
            HidLayers.get(i).initialize();;
        }

        OutLayer outLayer = new OutLayer(outsize, HidLayers.get(HidLayers.size()-1).getSize());
        outLayer.initialize();

        // ! Set the inputs and labels
        ArrayList<Integer> indexes = new ArrayList<>();
        indexes.add(0); indexes.add(1);
        ArrayList<Double> values = new ArrayList<>();
        values.add((double)ThreadLocalRandom.current().nextInt(0, 2)); values.add((double)ThreadLocalRandom.current().nextInt(0, 2));
        changeInput(inLayer, indexes, values);

        ArrayList<Double> labels = new ArrayList<>();
        if(values.get(0)+values.get(1)>1.1 || values.get(0)+values.get(1)<0.1) {
            labels.add(1.0); 
        } else labels.add(0.0);


        // ! Call the forward pass and get output values
        ArrayList<Double> forwardPass = forwardProp(inLayer, HidLayers, outLayer, false);

        // ! Run backward propegation
        backwardProp(inLayer, HidLayers, outLayer, forwardPass, labels);

        int loop=1;
        while(loop<=9999999) {
            values.clear();            
            values.add((double)ThreadLocalRandom.current().nextInt(0, 2)); values.add((double)ThreadLocalRandom.current().nextInt(0, 2));
            changeInput(inLayer, indexes, values);

            labels.clear();
            if(values.get(0)+values.get(1)>1.1 || values.get(0)+values.get(1)<0.1) {
                labels.add(1.0); 
            } else labels.add(0.0);

            forwardPass = forwardProp(inLayer, HidLayers, outLayer, false);
            backwardProp(inLayer, HidLayers, outLayer, forwardPass, labels);

            // ? Test below
            values.clear();            
            values.add(1.0); values.add(1.0);
            changeInput(inLayer, indexes, values);

            ArrayList<Double> label1 = new ArrayList<>();
            label1.add(1.0); label1.add(0.0);
            ArrayList<Double> forwardPass1 = forwardProp(inLayer, HidLayers, outLayer, false);


            values.clear();            
            values.add(1.0); values.add(0.0);
            changeInput(inLayer, indexes, values);
            
            ArrayList<Double> label2 = new ArrayList<>();
            label2.add(0.0); 
            ArrayList<Double> forwardPass2 = forwardProp(inLayer, HidLayers, outLayer, false);

            double error = computeError(forwardPass1, label1).get(computeError(forwardPass1, label1).size()-1)+computeError(forwardPass2, label2).get(computeError(forwardPass2, label2).size()-1);
            System.out.println("ERROR OF ANN ..."+ error);
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
            changeInput(inLayer, indexes, values);

            labels.clear();
            if(input1+input2>1.1 || input1+input2<0.1) {
                labels.add(1.0);
            } else labels.add(0.0); 
    
            forwardPass = forwardProp(inLayer, HidLayers, outLayer, true);
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

    public void backwardProp(InLayer inputLayer, ArrayList<HidLayer> HidLayers, OutLayer outputLayer, ArrayList<Double> y, ArrayList<Double> labels) {
        ArrayList<InNeuron> inputneurons = inputLayer.getNeurons();
        ArrayList<OutNeuron> outputneurons = outputLayer.getNeurons();

        for(int i=0; i<outputneurons.size(); i++) {
            for(int j=0; j<outputneurons.get(i).getSynapses().size(); j++) {
                double weight = outputneurons.get(i).getSynapses().get(j).getWeight();

                double dz = outputneurons.get(i).getY() * (1-outputneurons.get(i).getY());
                double dy = -1*(labels.get(i)-outputneurons.get(i).getY());
                
                outputneurons.get(i).setDelta(dz*dy);

                double dw = HidLayers.get(hidnum-1).getNeurons().get(j).getH();
            }
        }

    }

    public void backwardProp2(InLayer inputLayer, ArrayList<HidLayer> HidLayers, OutLayer outputLayer, ArrayList<Double> y, ArrayList<Double> labels) {
        ArrayList<InNeuron> inneurons = inputLayer.getNeurons();
        ArrayList<OutNeuron> outneurons = outputLayer.getNeurons();


        // ! Update the output layer neuron weights
        // * Loop through the output neurons
        for(int i=0; i<outneurons.size(); i++) {
            // * Loop through the neurons' synapses
            for(int j=0; j<outneurons.get(i).getSynapses().size(); j++) {
                double w = outneurons.get(i).getSynapses().get(j).getWeight();
                double dz = outneurons.get(i).getY() * (1-outneurons.get(i).getY());
                double dw = HidLayers.get(HidLayers.size()-1).getNeurons().get(j).getH();
                double dy = -1*(labels.get(i)-outneurons.get(i).getY());

                double derivative = dz*dw*dy;

                outneurons.get(i).getSynapses().get(j).setWeight(w-(LEARNINGRATE*derivative));
            }
        }

        // // ! Update the output layer neuron bias
        // for(int i=0; i<outneurons.size(); i++) {
        //     double b = outneurons.get(i).getBias();
        //     double dz = outneurons.get(i).getZ() * (1-outneurons.get(i).getZ());
        //     double dy = -1*(labels.get(i)-outneurons.get(i).getY());
                                
        //     double derivative = dz*dy;
                    
        //     outneurons.get(i).setBias(b-(LEARNINGRATE*derivative*0.001));
        // }

        // ! Update the hidden layer neuron weights
        for(int p=0; p<HidLayers.size(); p++) {
            ArrayList<HidNeuron> hidneurons = HidLayers.get(p).getNeurons();
            for(int i=0; i<hidneurons.size(); i++) {
                for(int j=0; j<hidneurons.get(i).getSynapses().size(); j++) {
                    double w = hidneurons.get(i).getSynapses().get(j).getWeight();
                    double dz = hidneurons.get(i).getH() * (1-hidneurons.get(i).getH());

                    double dw=0;
                    if(p==0) {
                        dw = inneurons.get(j).getValue();
                    } else {
                        dw = HidLayers.get(p-1).getNeurons().get(j).getH();
                    }

                    double dh = 0;
                    if(p==HidLayers.size()-1) {
                        for(int k=0; k<outneurons.size(); k++) {
                            double d3 = outneurons.get(k).getSynapses().get(i).getWeight();
                            double d1 = outneurons.get(k).getY() * (1-outneurons.get(k).getY());
                            double d2 = -1*(labels.get(k)-outneurons.get(k).getY());
                            dh = dh + (d3*(d1*d2));
                        }
                    } else {
                        for(int k=0; k<HidLayers.get(p).getNeurons().size(); k++) {
                            double d3 = HidLayers.get(p+1).getNeurons().get(k).getSynapses().get(i).getWeight();
                            double d1 = HidLayers.get(p+1).getNeurons().get(k).getH() * (1-HidLayers.get(p+1).getNeurons().get(k).getH());
                            double d2 = -1*(HidLayers.get(p).getNeurons().get(k).getH()-HidLayers.get(p+1).getNeurons().get(k).getH());
                            dh = dh + (d3*(d1*d2));
                        }
                    }
    
                    double derivative = dz*dw*dh;
                    hidneurons.get(i).getSynapses().get(j).setWeight(w-(LEARNINGRATE*derivative));
                }
            }
        }
        
        // // ! Update the hidden layer neuron bias
        // for(int p=0; p<HidLayers.size(); p++) {
        //     ArrayList<HidNeuron> hidneurons = HidLayers.get(p).getNeurons();
        //     for(int i=0; i<hidneurons.size(); i++) {
        //         double b = hidneurons.get(i).getBias();
        //         double dz = hidneurons.get(i).getZ() * (1-hidneurons.get(i).getZ());
    
        //         double dh = 0;
        //         if(p==HidLayers.size()-1) {
        //             for(int k=0; k<outneurons.size(); k++) {
        //                 double d3 = outneurons.get(k).getSynapses().get(i).getWeight();
        //                 double d1 = outneurons.get(k).getZ() * (1-outneurons.get(k).getZ());
        //                 double d2 = -1*(labels.get(k)-outneurons.get(k).getY());
        //                 dh = dh + (d3*(d1*d2));
        //             }
        //         } else {
        //             for(int k=0; k<HidLayers.get(p).getNeurons().size(); k++) {
        //                 double d3 = HidLayers.get(p+1).getNeurons().get(k).getSynapses().get(i).getWeight();
        //                 double d1 = HidLayers.get(p+1).getNeurons().get(k).getZ() * (1-HidLayers.get(p+1).getNeurons().get(k).getZ());
        //                 double d2 = -1*(labels.get(k)-HidLayers.get(p+1).getNeurons().get(k).getH());
        //                 dh = dh + (d3*(d1*d2));
        //             }
        //         }
    
        //         double derivative = dz*dh;
        //         hidneurons.get(i).setBias(b-(LEARNINGRATE*derivative*0.001));
        //     }
        // }
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
        ANN ann = new ANN(2, 1, 1, 3);
        ann.exec();
    }
}
