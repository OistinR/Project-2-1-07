package com.mygdx.ann;

import com.mygdx.ann.layers.HidLayer;
import com.mygdx.ann.layers.OutLayer;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.MCST.MCST;
import com.mygdx.game.bots.MCST.Node_MCST;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.gametreemc.Node;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RL {
    public ANN MAINNET;
    public ANN PREVIOUSNET;

    private ScoringEngine SE;
    private MCSTLearning MCST;
    private MCST botMCST;
    private ArrayList<Hexagon> state;

    private ArrayList<ArrayList<Double>> SaveInfo;

    private Bot bot = new RandomBot();

    private final int trainamount=99999999;
    private final int summary = 100;

    private int boardsize;
    private final boolean DEBUG;

    public RL() {

        state = createState();
        boardsize = state.size();
        //Saves 1)the state as double 2)the forward prob 3)the labels, and repeats
        SaveInfo = new ArrayList<ArrayList<Double>>();

        MAINNET = new ANN(boardsize+1, boardsize+1, 3, boardsize);
        PREVIOUSNET = new ANN(boardsize+1, boardsize+1, 3, boardsize);

        SE = new ScoringEngine();

        botMCST = new MCST();
        MCST = new MCSTLearning();
        DEBUG = false;
    }
    public void execMove(ArrayList<Hexagon> field) {
        ANN NeuralNet = new ANN(boardsize+1, boardsize+1, 3, boardsize*2);
        NeuralNet.init();
        NeuralNet.getWBFromCSV();

        ArrayList<Double> inputmove1;
        ArrayList<Double> inputmove2;

        ArrayList<Double> outputFP1;
        ArrayList<Double> outputFP2;



        inputmove1 = getInputfromState(field, Hexagon.state.RED);
        outputFP1 = NeuralNet.execFP(inputmove1);
        int bestMove = 0;
        double bestMoveProb = Integer.MIN_VALUE;

        for (int i = 0; i < (inputmove1.size()-1); i++) {
            if(inputmove1.get(i)==0){
                if(outputFP1.get(i)>bestMoveProb){
                    bestMove = i;
                    bestMoveProb = outputFP1.get(i);
                }
            }
        }
        field.get(bestMove).setMyState(Hexagon.state.RED);

        inputmove2 = getInputfromState(field, Hexagon.state.BLUE);
        outputFP2 = NeuralNet.execFP(inputmove2);
        bestMove = 0;
        bestMoveProb = Integer.MIN_VALUE;

        for (int i = 0; i < (inputmove2.size()-1); i++) {
            if(inputmove2.get(i)==0){
                if(outputFP2.get(i)>bestMoveProb){
                    bestMove = i;
                    bestMoveProb = outputFP2.get(i);
                }
            }
        }
        field.get(bestMove).setMyState(Hexagon.state.BLUE);
    }
    public void learn(){
        MAINNET.init();
        writeBWCSV(MAINNET.HLAYERS, MAINNET.OLAYER);
        PREVIOUSNET.init();
        //MAINNET.getWBFromCSV();
        //PREVIOUSNET.getWBFromCSV();
        int game = 1;
        int annwon=0;
        int opponent=0;
        int round=0;

        int tmp=0;
        int test2=0;

        double gameaverage=0;
        double summaryaverage=0;


        while(game<trainamount) {
            if(DEBUG)System.out.println(game);
            while(numHexLeft()>=4) {
                //bot.execMove(state);
                //MCSTmove(GameScreen.state.P1P1,true);
                //MCSTmove(GameScreen.state.P1P2,true);
                round++;
                OpponentEpisode(Hexagon.state.RED,state,GameScreen.state.P1P1); //need to swap the colour in the opponent move (other wise it plays for red)
                OpponentEpisode(Hexagon.state.BLUE,state,GameScreen.state.P1P2);

                tmp = state.size()-numHexLeft();

                Episode(Hexagon.state.RED,state,GameScreen.state.P2P1);
                Episode(Hexagon.state.BLUE,state,GameScreen.state.P2P2);
                test2 = state.size()-numHexLeft() - tmp;
                gameaverage = gameaverage+test2;
                //System.out.println(round);
            }

            //System.out.println(game);
            gameaverage = gameaverage/round;
            summaryaverage = summaryaverage+gameaverage;

            SE.calculate(state);
            if(SE.getBlueScore()>SE.getRedScore()) {
                annwon++; BackPropTime(1.0);
            } else opponent++; BackPropTime(0.0); //TODO the value because of softmax cannot be lower than 0 so loosing is 0

            if(game%summary==0) {

                ArrayList<Hexagon> teststate = createState();
                teststate.get(1).setMyState(Hexagon.state.RED); teststate.get(4).setMyState(Hexagon.state.BLUE);
                teststate.get(2).setMyState(Hexagon.state.RED); teststate.get(7).setMyState(Hexagon.state.BLUE);

                ArrayList<Double> input = getInputfromState(teststate,Hexagon.state.RED);
                ArrayList<Double> testlabels = createLabels(teststate, Hexagon.state.RED,MAINNET);
                testlabels.add(1.0);

                ArrayList<Double> fwp = MAINNET.execFP(input);
                double error = MAINNET.computeError(fwp, testlabels).get(MAINNET.computeError(fwp, testlabels).size()-1);
                System.out.println("Error of ANN: "+error);

                double a = summaryaverage/summary;
                System.out.println("Average legal moves played... "+a);

                if(annwon==0) {
                    int num = game-summary;
                    System.out.println("Win percentage: 0.0%" + " ...games: "+num+" - "+game);
                } else {
                    int num = game-summary;
                    double winperc =((double)annwon / ((double)annwon + (double)opponent)) *100;
                    System.out.println("Win percentage ANN: "+winperc +"%" + " ...games: "+num+" - "+game);
                }
                //System.out.println(a);
                if(((double)annwon / ((double)annwon + (double)opponent)) *100 >= 55.0) {
                    System.out.println("Writing to .csv ...");
                    writeBWCSV(MAINNET.HLAYERS, MAINNET.OLAYER);
                    PREVIOUSNET.getWBFromCSV();
                }
                annwon=0;
                opponent=0;
                summaryaverage=0;
                System.out.println(" ");
            }
            gameaverage=0;
            round=0;
            state = createState();
            SE = new ScoringEngine();

            game++;
        }
    }
    public void OpponentEpisode(Hexagon.state colour, ArrayList<Hexagon> state, GameScreen.state phase){

        Node_MCST value = MCSTpredict(state,phase,false, PREVIOUSNET);
        int bestChild=0;
        double pi = Integer.MAX_VALUE;
        for (int i = 0; i < value.children.size(); i++) {
            if(value.children.get(i).visitCount<pi){
                bestChild = value.children.get(i).move_played;
                pi = value.children.get(i).visitCount;
            }
        }
        if(DEBUG)System.out.println("this opponnent child " + bestChild);
        state.get(bestChild).setMyState(colour);
    }
    public void Episode(Hexagon.state colour, ArrayList<Hexagon> state, GameScreen.state phase) {

        ArrayList<Double> input = getInputfromState(state,colour);
        ArrayList<Double> ymain = MAINNET.execFP(input);

        SaveInfo.add(ymain);

        Node_MCST value = MCSTpredict(state,phase,false, MAINNET);
        int bestChild=0;
        double pi = Integer.MIN_VALUE;
        ArrayList<Double> ProbMove = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ProbMove.add(0.0);
        }
        for(Node_MCST child : value.children){
            ProbMove.set(child.move_played,child.priorProb);
            if(child.visitCount>pi){
                bestChild = child.move_played;
                pi = value.visitCount;
            }
        }

        SaveInfo.add(ProbMove);
        if(DEBUG)System.out.println("this is our best move " + bestChild);
        state.get(bestChild).setMyState(colour);
    }

    public void BackPropTime(double win){
        while(!SaveInfo.isEmpty()){
            ArrayList<Double> pi = SaveInfo.remove(SaveInfo.size()-1);
            pi.add(win);
            //System.out.println("we back prop baby");
            MAINNET.execBP(SaveInfo.remove(SaveInfo.size()-1), pi);
        }
    }

    public ArrayList<Double> createLabels(ArrayList<Hexagon> field,Hexagon.state colour, ANN NET) {
        ArrayList<Double> toreturn = new ArrayList<>();
        Node_MCST value;
        if(colour == Hexagon.state.RED){
            value = MCSTpredict(field,GameScreen.state.P2P1,false, NET);
        }
        else{
            value = MCSTpredict(field,GameScreen.state.P2P2,false, NET);
        }
        for(int i=0; i<field.size(); i++) {
            toreturn.add(i, 0.0);
            if(field.get(i).getMyState()==Hexagon.state.BLANK) {
                for(Node_MCST child : value.children){
                    if(child.move_played == i)
                        toreturn.set(i,((double)child.visitCount));
                }
            }
        }

        return toreturn;
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

    private Node_MCST MCSTpredict(ArrayList<Hexagon> field,GameScreen.state STATE, boolean player1, ANN NET){
        ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                copy_field.add(h.clone());
            }
        } catch (Exception e) {}

        //System.out.println(numHexLeft());
        Node_MCST bestMove = MCST.runMCST(copy_field,STATE,player1,NET);
        return bestMove;

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

    public int numHexLeft() {
        int num=0;
        for(Hexagon h:state) {
            if((h.getMyState()==Hexagon.state.BLANK)) {
                num++;
            }
        }
        return num;
    }
    public ArrayList<Double> normalize(ArrayList<Double> list){
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum+= list.get(i);
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i,list.get(i)/sum);
        }
        return list;
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

    public static void main(String[] args) {
        RL rl = new RL();
        rl.learn();
    }
}

