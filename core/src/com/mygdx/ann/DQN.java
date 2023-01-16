package com.mygdx.ann;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Target;
import java.nio.ReadOnlyBufferException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.utils.Array;
import com.mygdx.ann.layers.HidLayer;
import com.mygdx.ann.layers.OutLayer;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.bots.MCST.MCST;
import com.mygdx.game.bots.MCST.Node_MCST;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;


public class DQN {

    private ANN TARGETNET;
    private ANN MAINNET;

    private final double DISCOUNT = 0.1;

    private ScoringEngine SE;

    private ArrayList<Hexagon> state;

    private final double EPSILON = 0.0;
    private final int trainamount=99999999;
    private final int summary = 100;

    private MCST botMCST;
    private Bot bot = new RandomBot();

    private int boardsize;

    public DQN() {

        state = createState();
        boardsize = state.size();

        MAINNET = new ANN(boardsize+1, boardsize, 1, boardsize*3);
        TARGETNET = new ANN(boardsize+1, boardsize,1, boardsize*3);

        SE = new ScoringEngine();

        botMCST = new MCST();
    }

    public void execMove(ArrayList<Hexagon> field) {
        ANN NeuralNet = new ANN(boardsize+1, boardsize, 1, boardsize*3);
        NeuralNet.init();
        NeuralNet.getWBFromCSV();

        ArrayList<Double> inputmove1;
        ArrayList<Double> inputmove2;

        ArrayList<Double> ymove1;
        ArrayList<Double> ymove2;

        int Qmove1;
        int Qmove2;

        inputmove1 = getInputfromState(field, Hexagon.state.RED);
        ymove1 = NeuralNet.execFP(inputmove1);
        Qmove1 = getQmax(ymove1);
        System.out.println("Q for red piece"+Qmove1);
        field.get(Qmove1).setMyState(Hexagon.state.RED);

        inputmove2 = getInputfromState(field, Hexagon.state.BLUE);
        ymove2 = NeuralNet.execFP(inputmove2);
        Qmove2 = getQmax(ymove2);
        System.out.println("Q for blue piece"+Qmove2);
        field.get(Qmove2).setMyState(Hexagon.state.BLUE);
    }

    public void learn() {

        MAINNET.init();
        TARGETNET.init();

        int game = 1;
        int annwon=0;
        int mcstwon=0;
        int round=0;

        int tmp=0;
        int test2=0;
        
        double gameaverage=0;
        double summaryaverage=0;
        
        while(game<trainamount) {
            while(numHexLeft()>=4) {
                round++;
                bot.execMove(state);
                tmp = state.size()-numHexLeft();
                Episode(Hexagon.state.RED);
                Episode(Hexagon.state.BLUE);
                test2 = state.size()-numHexLeft() - tmp;
                gameaverage = gameaverage+test2;
                //MCSTmove(GameScreen.state.P2P1,false);
                //MCSTmove(GameScreen.state.P2P2,false);
            }
            gameaverage = gameaverage/round;
            summaryaverage = summaryaverage+gameaverage;

            SE.calculate(state);
            if(SE.getBlueScore()>SE.getRedScore()) {
                annwon++;
            } else mcstwon++;

            // if(game%10==0) {
            //     TARGETNET.copyWB(MAINNET);
            // }

            if(game%summary==0) {

                ArrayList<Hexagon> teststate = createState();
                teststate.get(1).setMyState(Hexagon.state.RED); teststate.get(4).setMyState(Hexagon.state.BLUE);
                teststate.get(2).setMyState(Hexagon.state.RED); teststate.get(7).setMyState(Hexagon.state.BLUE);
                ArrayList<Double> testlabels = createLabels(teststate, 1, 1);
                ArrayList<Double> fwp = MAINNET.execFP(testlabels);
                double error = MAINNET.computeError(fwp, testlabels).get(MAINNET.computeError(fwp, testlabels).size()-1);
                System.out.println("Error of ANN: "+error);

                double a = summaryaverage/summary;
                System.out.println("Average legal moves played... "+a);

                if(annwon==0) {
                    int num = game-summary;
                    System.out.println("Win percentage: 0.0%" + " ...games: "+num+" - "+game);
                } else {
                    int num = game-summary;
                    double winperc =((double)annwon / ((double)annwon + (double)mcstwon)) *100;
                    System.out.println("Win percentage ANN: "+winperc +"%" + " ...games: "+num+" - "+game);
                }

                if(a==2.0) {
                    System.out.println("Writing to .csv ...");
                    writeBWCSV(MAINNET.HLAYERS, MAINNET.OLAYER);
                }

                annwon=0;
                mcstwon=0;
                summaryaverage=0;
                System.out.println(" ");
            }

            // if(game==100000) {
            //     System.out.println("Writing to .csv ...");
            //     writeBWCSV(MAINNET.HLAYERS, MAINNET.OLAYER);
            // }
    
            gameaverage=0;
            round=0;
            state = createState();
            SE = new ScoringEngine();

            game++;
        }

    }

    public void Episode(Hexagon.state colour) {
        double reward = 0;
        int Q = 0;

        ArrayList<Double> input = getInputfromState(state,colour);
        ArrayList<Double> ymain = MAINNET.execFP(input);

        double U = ThreadLocalRandom.current().nextDouble();

        if(U>EPSILON) {
            Q = getQmax(ymain);
            reward = 1.0;
            state.get(Q).setMyState(colour);
        } else {
            Q = ThreadLocalRandom.current().nextInt(0,state.size());
            reward = 1.0;
            state.get(Q).setMyState(colour);
        }

       //System.out.println(MAINNET.getOutputLayer().getNeurons().get(0).getSynapses().get(0).getWeight());

        double label = reward;

        ArrayList<Double> labels = createLabels(state, Q, label);

        // !
        MAINNET.execBP(ymain, labels);

    }


    public ArrayList<Double> createLabels(ArrayList<Hexagon> field, int index, double value) {
        ArrayList<Double> toreturn = new ArrayList<>();
        for(int i=0; i<field.size(); i++) {
            toreturn.add(1.0);
            // if(i==index) {
            //     toreturn.set(i, value);
            // }
            if(field.get(i).getMyState()!=Hexagon.state.BLANK) {
                toreturn.set(i, -100.0);
            }
            
        }
        return toreturn;
    }

    public int getQmax(ArrayList<Double> list) {
        double max=Double.MIN_VALUE;
        int maxi=0;
        for(int i=0; i<list.size(); i++) {
            if(list.get(i)>max) {
                max=list.get(i);
                maxi=i;
            }
        }
        return maxi;
    }

    public ArrayList<Double> getInputfromState(ArrayList<Hexagon> statefield, Hexagon.state col) {
        ArrayList<Double> inutreturn = new ArrayList<>();
        for(int i=0; i<statefield.size(); i++) {
            if(statefield.get(i).getMyState()==Hexagon.state.BLANK) {
                inutreturn.add(0.0);
            } else if(statefield.get(i).getMyState()==Hexagon.state.RED) {
                inutreturn.add(1.0);
            } else if(statefield.get(i).getMyState()==Hexagon.state.BLUE){
                inutreturn.add(-1.0);
            } else System.out.println("An error has occurred when reading the board");
        }
        if(col==Hexagon.state.RED) {
            inutreturn.add(1.0);
        } else if(col==Hexagon.state.BLUE) {
            inutreturn.add(-1.0);
        }
        return inutreturn;
    }

    public ArrayList<Hexagon> createState() {
        int s;
        int fieldsize = 2;
        ArrayList<Hexagon> field = new ArrayList<>();
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, 0, 0));
                }
            }
        }
        return field;
    }

    private void MCSTmove(GameScreen.state STATE, boolean player1){
        ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : state) {
                copy_field.add(h.clone());
            }
        } catch (Exception e) {}

        Node_MCST bestMove = botMCST.runMCST(copy_field,STATE,player1);

        if(bestMove.phase==GameScreen.state.P1P1 || bestMove.phase==GameScreen.state.P2P1)
            state.get(bestMove.move_played).setMyState(Hexagon.state.RED);
        else if(bestMove.phase==GameScreen.state.P1P2 || bestMove.phase==GameScreen.state.P2P2){
            state.get(bestMove.move_played).setMyState(Hexagon.state.BLUE);
        }
        else{
            throw new IllegalStateException("The children phase is not assign correctly: ");
        }
    }

    public int numHexLeft() {
        int num=0;
        for(Hexagon h:state) {
            if((h.getMyState()==Hexagon.state.BLANK)) {
                num++;
            }
        }
        return num;
    }

    public static void main(String[] args) {
        DQN dqn = new DQN();
        dqn.learn();
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
