package com.mygdx.game;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.MCST.MCST;
import com.mygdx.game.bots.MCST.Node_MCST;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;

import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.bots.gametree.TreeBot;
import com.mygdx.game.coordsystem.Hexagon;

import com.mygdx.game.experiment.GameState;

import com.mygdx.game.experiment.StateWrite;
import com.mygdx.game.experiment.experiment;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.tensorAnn.PredictBot;

public class rundev {
    public enum state{
        P1P1,
        P1P2,
        P1P3,
        P2P1,
        P2P2,
        P2P3
    }

    private int round;
    private int fieldsize;
    private boolean gamefinished;
    private ArrayList<Hexagon> field;
    private ScoringEngine SEngine;
    private Bot botpone;
    private Bot botptwo;
    private ArrayList<Integer> bot1wins;
    private ArrayList<Integer> bot2wins;
    private ArrayList<Integer> draws;
    private int totalnumgames;
    private double finalWinRate;

    private ArrayList<Float> dataWinPerc = new ArrayList<Float>();
    private ArrayList<Float> dataLossPerc = new ArrayList<Float>();
    private ArrayList<Float> dataDrawPerc = new ArrayList<Float>();
    private ArrayList<Float> dataWins = new ArrayList<Float>();
    private ArrayList<Float> dataDraws = new ArrayList<Float>();
    private experiment exp = new experiment();
    private double winperc1, winperc2, winpercd;
    private ArrayList<Double> ar;

    private GameState gameState;
    private File fileWrite;
    private FileWriter writer;
    private  StringBuilder output;
    private StateWrite sw;

    public void init() {
        fileWrite = new File("core\\src\\com\\mygdx\\SENDTHIStrainingData.csv");
        // Initiate variables
        ar = new ArrayList<>();
        gameState = new GameState();
        round = 1;
        totalnumgames = 100;
        gamefinished = false;
        field = new ArrayList<>();
        SEngine = new ScoringEngine();
        bot1wins = new ArrayList<>();
        bot2wins = new ArrayList<>();
        draws = new ArrayList<>();

        //Initiate fieldsize
        fieldsize=3;

        //Create field and initiate bots



        botMCST = new MCST();
        // botpone = new TreeBot(Hexagon.state.RED,Hexagon.state.BLUE);
//        botpone = new TreeBot(Hexagon.state.RED, Hexagon.state.BLUE);
        botpone = new RandomBot();
        botptwo = new PredictBot();

        createHexagonFieldDefault();
    }

    public void update() {

        double winperc1;
        double winperc2;
        double winpercd;
   

        output = new StringBuilder();
        int i = 0;
        while (i < totalnumgames) {
//            ar.clear();
//            if (i>0)
//            output.append("\n");

            while (!gamefinished) {
                updateState();
                makeMove();
                updateState();
            }
            if(i%10==0) {
                System.out.println(i+" games simulated.");
            }

                for (Double feature:ar) {
                    output.append(feature.toString()).append(",");
                }
//            output.setLength(output.length() - 1);
//          output.replace(output.length()-1,output.length()-1,"");
            round = 1;
            gamefinished = false;

            field = new ArrayList<>();
            SEngine = new ScoringEngine();
            createHexagonFieldDefault();

            i++;
        }


//        try {
//            output.append(",999.0");
//            writer = new FileWriter(fileWrite);
//            writer.write(output.toString());
//            writer.close();
//        } catch (IOException e) {
//            System.out.println("error with IO: ");
//            e.printStackTrace();
//        }


        winperc1 = ((double) bot1wins.size() / (double) totalnumgames) * 100;
        winperc2 = ((double) bot2wins.size() / (double) totalnumgames) * 100;
        winpercd = ((double) draws.size() / (double) totalnumgames) * 100;
//
        System.out.println("Bot 1 number of wins: " + bot1wins.size() + " Win percentage: " + winperc1 + " %");
        System.out.println("Bot 2 number of wins: " + bot2wins.size() + " Win percentage: " + winperc2 + " %");
        System.out.println("Number of draws: " + draws.size() + " Draw percentage: " + winpercd + " %");

        this.finalWinRate = winperc1;
    }

    public void createHexagonFieldDefault() {
        int s;
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50,0,0));
                }
            }
        }
    }

    public void updateState() {
        int numhex = numHex() - 4*(round-1);

        if(field.size()-(numhex+(4*(round-1)))<4) {
            gameFinish();
        }
    }

    private void duplicateRemover(ArrayList<Double> before, ArrayList<Double> after ){
        if(before.size()!= after.size()){
            System.out.println("sizes not equal");
            System.out.println(before);
            System.out.println(after);
            return;
//            throw new RuntimeException("length of arraylists is not the equal");
        }
        for (int i = 0; i < after.size(); i++) {
            if(after.get(i).equals(before.get(i))){
                after.set(i, 0.0);
            }
        }
    }


    public int numHex() {
        int num=0;
        for(Hexagon h:field) {
            if((h.getMyState()!=Hexagon.state.BLANK)) {
                num++;
            }
        }
        return num;
    }

    public void makeMove() {
//        MCSTmove(GameScreen.state.P1P2,true);
//        MCSTmove(GameScreen.state.P1P1,true);
        botpone.calculate(field);
        botptwo.calculate(field);

//        ar.add(999.0);
//        gameState.update(field);
//        ar.addAll(gameState.getState());
//         add features here
//        ar.add(1.0);
//        ar.add(999.0);

//        gameState.update(field);
//        temp = new ArrayList<>(ar.subList(ar.size() - gameState.getState().size()-2, ar.size()-2));
//        duplicateRemover(temp,gameState.getState());
//        ar.addAll(gameState.getState());

//        ar.add(999.0);
//        gameState.update(field);
//        ar.addAll(gameState.getState());
        //add features here
//        ar.add(-1.0);
//        ar.add(999.0);



//        gameState.update(field);
//        temp = new ArrayList<>(ar.subList(ar.size() - gameState.getState().size()-2, ar.size()-2));
//        duplicateRemover(temp,gameState.getState());
//        ar.addAll(gameState.getState());

    }
    private ArrayList<Double> temp;

    public void gameFinish() {
        SEngine.calculate(field);
        int p1score=SEngine.getRedScore();
        int p2score=SEngine.getBlueScore();

//        System.out.println("GAME HAS ENDED");
//        System.out.println("PLAYER 1 (RED) SCORE: "+p1score);
//        System.out.println("PLAYER 2 (BLUE) SCORE: "+p2score);
        if(p1score>p2score) {
            //System.out.println("PLAYER 1 (RED) WON");
            bot1wins.add(p1score);
        } else if(p1score<p2score) {
            //System.out.println("PLAYER 2 (BLUE) WON");
            bot2wins.add(p2score);
        } else {
            //System.out.println("THE GAME IS A DRAW");
            draws.add(p1score);
        }
        gamefinished=true;
    }

    public ArrayList<Hexagon> getField(){
        return this.field;
    }

    public double getWinRate(){
        return this.finalWinRate;
    }

    public void setField(ArrayList<Hexagon> field){
        ArrayList<Hexagon> clone = new ArrayList<>();
        try{
            for(Hexagon hex:field){
                clone.add(hex.clone());
            }
        }
        catch(Exception e){

        }
        this.field = field;
    }
    private MCST botMCST;
    private int count =0;


    private void MCSTmove(GameScreen.state STATE,boolean player1){

        ArrayList<Hexagon> copy_field = new ArrayList<>();
        try {
            for(Hexagon h : field) {
                copy_field.add(h.clone());
            }
        } catch (Exception e) {}

        count++;
        Node_MCST bestMove = botMCST.runMCST(copy_field,STATE,player1, 0);

        if(bestMove.phase==GameScreen.state.P1P1 || bestMove.phase==GameScreen.state.P2P1)
            field.get(bestMove.move_played).setMyState(Hexagon.state.RED);
        else if(bestMove.phase==GameScreen.state.P1P2 || bestMove.phase==GameScreen.state.P2P2){
            field.get(bestMove.move_played).setMyState(Hexagon.state.BLUE);
        }
        else{
            throw new IllegalStateException("The children phase is not assign correctly: ");
        }
    }


    public static void main(String[] args) {
        rundev dev = new rundev();
        dev.init();
        dev.update();

        
        
//        Toolkit.getDefaultToolkit().beep();

//        StateWrite sw = new StateWrite();
//        sw.readFrom();
    }
}
