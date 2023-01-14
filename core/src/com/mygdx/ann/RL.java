package com.mygdx.ann;

import com.mygdx.ann.layers.HidLayer;
import com.mygdx.ann.layers.OutLayer;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.MCST.MCST;
import com.mygdx.game.bots.MCST.Node_MCST;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RL {
    private ANN MAINNET;

    private ScoringEngine SE;
    private MCST botMCST;
    private ArrayList<Hexagon> state;

    private Bot bot = new RandomBot();

    private final int trainamount=99999999;
    private final int summary = 10;
    private final double EPSILON = 0.0;

    private int boardsize;
    public RL() {

        state = createState();
        boardsize = state.size();

        MAINNET = new ANN(boardsize+1, boardsize, 2, boardsize+1);

        SE = new ScoringEngine();

        botMCST = new MCST();
    }

    public void learn(){
        MAINNET.init();
        int game = 1;
        int annwon=0;
        int opponent=0;
        int round=0;

        double gameaverage=0;
        double summaryaverage=0;

        while(game<trainamount) {
            while(numHexLeft()>=4) {
                round++;
                bot.execMove(state);
                //MCSTmove(GameScreen.state.P1P1,true);
                //MCSTmove(GameScreen.state.P1P2,true);
                Episode(Hexagon.state.RED);
                Episode(Hexagon.state.BLUE);
                //System.out.println(round);
            }
            System.out.println(game);
            gameaverage = gameaverage/round;
            summaryaverage = summaryaverage+gameaverage;

            SE.calculate(state);
            if(SE.getBlueScore()>SE.getRedScore()) {
                annwon++;
            } else opponent++;
            if(game%summary==0) {

                ArrayList<Hexagon> teststate = createState();
                teststate.get(1).setMyState(Hexagon.state.RED); teststate.get(4).setMyState(Hexagon.state.BLUE);
                teststate.get(2).setMyState(Hexagon.state.RED); teststate.get(7).setMyState(Hexagon.state.BLUE);

                ArrayList<Double> testlabels = createLabels(teststate, Hexagon.state.RED);

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
                    double winperc =((double)annwon / ((double)annwon + (double)opponent)) *100;
                    System.out.println("Win percentage ANN: "+winperc +"%" + " ...games: "+num+" - "+game);
                }
                System.out.println(a);
                if(a==2.0) {
                    System.out.println("Writing to .csv ...");
                    writeBWCSV(MAINNET.HLAYERS, MAINNET.OLAYER);
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

    public void Episode(Hexagon.state colour) {

        ArrayList<Double> input = getInputfromState(state,colour);
        ArrayList<Double> ymain = MAINNET.execFP(input);
        int bestMove = 0;
        double bestMoveProb = -1;

        for (int i = 0; i < ymain.size(); i++) {
            if(input.get(i)==0){
                if(ymain.get(i)>bestMoveProb){
                    bestMove = i;
                    bestMoveProb = ymain.get(i);
                }
            }
        }

        ArrayList<Double> labels = createLabels(state,colour);
        state.get(bestMove).setMyState(colour);
        MAINNET.execBP(ymain, labels);
    }

    public ArrayList<Double> createLabels(ArrayList<Hexagon> field,Hexagon.state colour) {
        ArrayList<Double> toreturn = new ArrayList<>();
        double value;
        for(int i=0; i<field.size(); i++) {

            toreturn.add(i, 0.0);
            if(field.get(i).getMyState()==Hexagon.state.BLANK) {
                if(colour == Hexagon.state.RED){
                    value = MCSTpredict(field,GameScreen.state.P2P1,false,i);
                    if(value<0)
                        toreturn.set(i, 0.0);
                    else
                        toreturn.set(i, value);
                }
                else{
                    value = MCSTpredict(field,GameScreen.state.P2P2,false,i);
                    if(value<0)
                        toreturn.set(i, 0.0);
                    else
                        toreturn.set(i, value);
                }
            }
        }
        return toreturn;
    }

    public ArrayList<Hexagon> createState() {
        int s;
        int fieldsize = 3;
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

    private double MCSTpredict(ArrayList<Hexagon> field,GameScreen.state STATE, boolean player1, int move){
        ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                copy_field.add(h.clone());
            }
            if(STATE == GameScreen.state.P2P1){
                copy_field.get(move).setMyState(Hexagon.state.RED);
            }
            else{
                copy_field.get(move).setMyState(Hexagon.state.BLUE);
            }
        } catch (Exception e) {}

        //System.out.println(numHexLeft());
        Node_MCST bestMove = botMCST.runMCST(copy_field,STATE,player1);
        //System.out.println(bestMove.parent.returnWinrate());
        return bestMove.parent.returnWinrate();

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

