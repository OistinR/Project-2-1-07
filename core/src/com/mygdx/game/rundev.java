package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Array;

import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.FitnessGroupBot;
import com.mygdx.game.bots.MCST.MCST;
import com.mygdx.game.bots.MCST.Node_MCST;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;
import com.mygdx.game.bots.OLABot;

import com.mygdx.game.bots.TreeBotMC;

import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.bots.gametree.TreeBot;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.gametreemc.MonteCarloTree;
import com.mygdx.game.gametreemc.Node;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;

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

    public void init() {
        //Initiate variables
        round=1;
        totalnumgames=5;
        gamefinished=false;
		field = new ArrayList<>();
        SEngine = new ScoringEngine();
        bot1wins = new ArrayList<>();
        bot2wins = new ArrayList<>();
        draws = new ArrayList<>();

        //Initiate fieldsize
        fieldsize=4;

        //Create field and initiate bots

        botptwo = new TreeBot(Hexagon.state.BLUE,Hexagon.state.RED);
        botpone = new FitnessGroupBot(Hexagon.state.RED, Hexagon.state.BLUE,false);
        botMCST = new MCST();
        createHexagonFieldDefault();
    }

    public void update() {
        double winperc1;
        double winperc2;
        double winpercd;
        int i=1;
        while(i<=totalnumgames) {
            System.out.println("game "+i+"");
            while(!gamefinished) {
                updateState();
                makeMove();
                updateState();
            } 
            if(i%10==0) {
                System.out.println(i+" games simulated.");
            }

            round=1;
            gamefinished=false;
            field = new ArrayList<>();
            SEngine = new ScoringEngine();
            createHexagonFieldDefault();

            i++;
        }

        winperc1 = ((double)bot1wins.size()/(double)totalnumgames)*100;
        winperc2 = ((double)bot2wins.size()/(double)totalnumgames)*100;
        winpercd = ((double)draws.size()/(double)totalnumgames)*100;

        System.out.println("Bot 1 number of wins: "+bot1wins.size()+ " Win percentage: "+ winperc1+ " %" );
        System.out.println("Bot 2 number of wins: "+bot2wins.size()+ " Win percentage: "+ winperc2+ " %" );
        System.out.println("Number of draws: "+draws.size()+ " Draw percentage: "+ winpercd+" %");
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
        botpone.execMove(field);
        MCSTmove(GameScreen.state.P2P1);
        MCSTmove(GameScreen.state.P2P2);
    }

    public void gameFinish() {
        SEngine.calculate(field);
        int p1score=SEngine.getRedScore();
        int p2score=SEngine.getBlueScore();

        System.out.println("GAME HAS ENDED");
        System.out.println("PLAYER 1 (RED) SCORE: "+p1score);
        System.out.println("PLAYER 2 (BLUE) SCORE: "+p2score);
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

    private void MCSTmove(GameScreen.state STATE){

        ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                copy_field.add(h.clone());
            }
        } catch (Exception e) {}

        count++;
        Node_MCST bestMove = botMCST.runMCST(copy_field,STATE);
        //System.out.println("the best move " + bestMove.move_played);
        //System.out.println(count);

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
    }
}
