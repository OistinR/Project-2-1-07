package com.mygdx.ann;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

import com.mygdx.ann.layers.HidLayer;
import com.mygdx.ann.layers.InLayer;
import com.mygdx.ann.layers.OutLayer;
import com.mygdx.ann.neurons.HidNeuron;
import com.mygdx.ann.neurons.InNeuron;
import com.mygdx.ann.neurons.OutNeuron;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.experiment.GameState;

public class ANN {

    private int insize;
    private int outsize;
    private int hidnum;
    private int hidsize;

    private final double LEARNINGRATE = 0.001;

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

    public ArrayList<Double> execFP(ArrayList<Double> values) {
        // ! Change the input values for the ANN
        changeInput(ILAYER, values);

        // ! Call the forward pass and get output values
        return forwardProp(ILAYER, HLAYERS, OLAYER, false);
    }

    public void execBP(ArrayList<Double> y, ArrayList<Double> labels) {
        // ! Run backward propegation
        backwardProp(ILAYER, HLAYERS, OLAYER, y, labels);
    }

    public void copyWB(ANN ann) {
        // ! The weights and biases from the ANN called 'ann' will be copied into this object
        for(int i=0; i<ann.getHiddenLayers().size(); i++) {
            HidLayer hlayer = ann.getHiddenLayers().get(i);
            for(int j=0; j<hlayer.getNeurons().size(); j++) {
                ArrayList<HidNeuron> hlayerneurons = hlayer.getNeurons();
                for(int k=0; k<hlayerneurons.get(j).getSynapses().size(); k++) {
                    HLAYERS.get(i).getNeurons().get(j).getSynapses().get(k).setWeight(hlayerneurons.get(j).getSynapses().get(k).getWeight());
                }
                HLAYERS.get(i).getNeurons().get(j).setBias(hlayerneurons.get(j).getBias());
            }
        }

        for(int i=0; i<ann.getOutputLayer().getNeurons().size(); i++) {
            for(int j=0; j<ann.getOutputLayer().getNeurons().get(i).getSynapses().size(); j++) {
                OLAYER.getNeurons().get(i).getSynapses().get(j).setWeight(ann.getOutputLayer().getNeurons().get(i).getSynapses().get(j).getWeight());
            }
            OLAYER.getNeurons().get(i).setBias(ann.getOutputLayer().getNeurons().get(i).getBias());
        }
    }
    



    public void execMove(ArrayList<Hexagon> field) {
        ANN NeuralNet = new ANN(37, 37, 4, 10);
        NeuralNet.init();
        NeuralNet.getWBFromCSV();

        ArrayList<Double> inputmove1;

        ArrayList<Double> ymove1;

        GameState gs = new GameState();
        gs.update(field);
        inputmove1 = gs.getState();
        ymove1 = NeuralNet.execFP(inputmove1);
        double max = -100;
        double min = 100;

        int bestindexRed = 0;
        int bestindexBlue = 0;
        for (int i = 0; i < ymove1.size(); i++) {
            if (ymove1.get(i)>max){
                bestindexRed = i;
                max = ymove1.get(i);
            }
            if (ymove1.get(i)<min){
                bestindexBlue = i;
                min = ymove1.get(i);
            }
        }

        field.get(bestindexRed).setMyState(Hexagon.state.RED);
        field.get(bestindexBlue).setMyState(Hexagon.state.BLUE);

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

        // ! Update the output neurons' weights
        for(int i=0; i<outputneurons.size(); i++) {
            for(int j=0; j<outputneurons.get(i).getSynapses().size(); j++) {
                double weight = outputneurons.get(i).getSynapses().get(j).getWeight();

                double dz = 1.0;//outputneurons.get(i).getY() * (1-outputneurons.get(i).getY());
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
                            double dz = derivReLU(hiddenneurons.get(i).getZ());
                            //double dz = hiddenneurons.get(i).getH() * (1-hiddenneurons.get(i).getH());
                            hiddelta = hiddelta+(delta*theta*dz);
                        }
                    } else {
                        for(int k=0; k<HidLayers.get(p+1).getNeurons().size(); k++) {
                            double delta = HidLayers.get(p+1).getNeurons().get(k).getDelta();
                            double theta = HidLayers.get(p+1).getNeurons().get(k).getSynapses().get(i).getWeight();
                            double dz = derivReLU(hiddenneurons.get(i).getZ());
                            //double dz = hiddenneurons.get(i).getH() * (1-hiddenneurons.get(i).getH());
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
                hidneuronlist.get(j).setH(ReLU(z));
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
            outneuronlist.get(j).setY(z);

            zlist.add(z);
        }
        for(int i=0; i<outneuronlist.size(); i++) {
            //outneuronlist.get(i).setY(SoftMax(zlist, i));
        }


        // * Printing and returning list with final probabilities
        ArrayList<Double> prob = new ArrayList<>();
        for(int i=0; i<outneuronlist.size(); i++) {
            prob.add(outneuronlist.get(i).getY());
        }


        return prob;
    }

    public double sigmoid(double x) {
        return 1/(1+Math.exp(-x));
    }

    public double ReLU(double x) {
        if(x<0) {
            return 0.1*x;
        } else return x;
    }

    public double derivReLU(double x) {
        if(x<0) {
            return 0.1;
        } else return 1;
    }

    public double SoftMax(ArrayList<Double> list, int index) {
        double sum=0;
        for(int i=0; i<list.size(); i++) {
            sum=sum+list.get(i);
        }
        return list.get(index)/sum;
    }


    public ArrayList<HidLayer> getHiddenLayers() {
        return HLAYERS;
    }

    public OutLayer getOutputLayer() {
        return OLAYER;
    }

    public void getWBFromCSV() {
        ArrayList<Double> data = new ArrayList<>();
        try {

            Scanner sc = new Scanner(new File("core\\src\\com\\mygdx\\ann\\data\\NNBW.csv"));

            while (sc.hasNext()) {  
                data.add(Double.parseDouble(sc.next()));
            }   
            sc.close(); 

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  

        int ITERATION = 0;

        for(int i=0; i<HLAYERS.size(); i++) {
            for(int j=0; j<HLAYERS.get(i).getNeurons().size(); j++) {
                for(int k=0; k<HLAYERS.get(i).getNeurons().get(j).getSynapses().size(); k++) {
                    HLAYERS.get(i).getNeurons().get(j).getSynapses().get(k).setWeight(data.get(ITERATION));
                    ITERATION++;
                }
            }
        }
        for(int i=0; i<HLAYERS.size(); i++) {
            for(int j=0; j<HLAYERS.get(i).getNeurons().size(); j++) {
                HLAYERS.get(i).getNeurons().get(j).setBias(data.get(ITERATION));
                ITERATION++;
            }
        }
        for(int i=0; i<OLAYER.getNeurons().size(); i++) {
            for(int j=0; j<OLAYER.getNeurons().get(i).getSynapses().size(); j++) {
                OLAYER.getNeurons().get(i).getSynapses().get(j).setWeight(data.get(ITERATION));
                ITERATION++;
            }
        }
        for(int i=0; i<OLAYER.getNeurons().size(); i++) {
            OLAYER.getNeurons().get(i).setBias(data.get(ITERATION));
            ITERATION++;
        }
    }

    public void writeBWCSV(ArrayList<HidLayer> hiddenlayers, OutLayer outputlayer) {

        ArrayList<String> writeable = new ArrayList<>();

        for(int i=0; i<hiddenlayers.size(); i++) {
            for(int j=0; j<hiddenlayers.get(i).getNeurons().size(); j++) {
                for(int k=0; k<hiddenlayers.get(i).getNeurons().get(j).getSynapses().size(); k++) {
                    writeable.add(Double.toString(hiddenlayers.get(i).getNeurons().get(j).getSynapses().get(k).getWeight()));
                }
            }
        }
        for(int i=0; i<hiddenlayers.size(); i++) {
            for(int j=0; j<hiddenlayers.get(i).getNeurons().size(); j++) {
                writeable.add(Double.toString(hiddenlayers.get(i).getNeurons().get(j).getBias()));
            }
        }
        for(int i=0; i<outputlayer.getNeurons().size(); i++) {
            for(int j=0; j<outputlayer.getNeurons().get(i).getSynapses().size(); j++) {
                writeable.add(Double.toString(outputlayer.getNeurons().get(i).getSynapses().get(j).getWeight()));
            }
        }
        for(int i=0; i<outputlayer.getNeurons().size(); i++) {
            writeable.add(Double.toString(outputlayer.getNeurons().get(i).getBias()));
        }

        File csvFile = new File("core\\src\\com\\mygdx\\ann\\data\\NNBW.csv");

        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            for (String data : writeable) {
                StringBuilder line = new StringBuilder();
                line.append(data);

                line.append("\n");
                fileWriter.write(line.toString());
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
