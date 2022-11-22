package com.mygdx.game;

import java.util.ArrayList;

import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;
import com.mygdx.game.bots.OLABot;
import com.mygdx.game.bots.TreeBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.gametree.Node;
import com.mygdx.game.gametree.Tree;
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

    public void init() {
        //Initiate variables
        round=1;
        totalnumgames=0;
        gamefinished=false;
		field = new ArrayList<>();
        SEngine = new ScoringEngine();
        bot1wins = new ArrayList<>();
        bot2wins = new ArrayList<>();
        draws = new ArrayList<>();

        //Initiate fieldsize
        fieldsize=3;

        //Create field and initiate bots
        botpone = new MaxN_Paranoid_Bot(Hexagon.state.RED, Hexagon.state.BLUE);
        botptwo =  new MaxN_Paranoid_Bot(Hexagon.state.RED, Hexagon.state.BLUE);//TODO INCORROECT
        createHexagonFieldDefault();
    }

    public void update() {
        double winperc1;
        double winperc2;
        double winpercd;
        int i=0;
        while(i<=totalnumgames) {
            while(!gamefinished) {
                updateState();
                makeMove();
                updateState();
            } 
            if(i%100==0) {
                System.out.println(i+" games simulated.");
            }

            round=1;
            gamefinished=false;
            field = new ArrayList<>();
            SEngine = new ScoringEngine();
            createHexagonFieldDefault();

            i++;
        }
        //TODO issue: the tree cant tell when game is over so its possible that infinite loops can occur.
//        Tree tr = new Tree(6,5);
//        // Storage is a massive issue, larger map sizes means lower depth/widths.
//        long runtime=0L;
//        long startTime = System.nanoTime();
//        tr.generateTree(field, GameScreen.state.P2P1,false);
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime);
//        runtime += duration/10000000;
//        System.out.println("\nruntime: "+ ((double)(runtime))/100+" seconds(i think)");
//
//        System.out.println(tr.displayTree(false));
//
//        System.out.println("bots assessment of board: "+ tr.getNodes().get(0).getCombinedScore());
//
//        double maxScore = 0;
//        for (Node n0: tr.getNodes().get(0).getChildArray()) {
//            if(maxScore<n0.getCombinedScore()){
//                maxScore = n0.getCombinedScore();
//            }
//            System.out.println(n0);
//        }
//        System.out.println("max score is: "+ maxScore);

        winperc1 = ((double)bot1wins.size()/(double)totalnumgames)*100;
        winperc2 = ((double)bot2wins.size()/(double)totalnumgames)*100;
        winpercd = ((double)draws.size()/(double)totalnumgames)*100;

        System.out.println("Bot 1 number of wins: "+bot1wins.size()+ " Win percentage: "+ winperc1+ " %" );
        System.out.println("Bot 2 number of wins: "+bot2wins.size()+ " Win percentage: "+ winperc2+ " %" );
        System.out.println("Number of draws: "+draws.size()+ " Draw percentage: "+ winpercd+" %");
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
        botptwo.execMove(field);
    }

    public void gameFinish() {
        SEngine.calculate(field);
        int p1score=SEngine.getRedScore();
        int p2score=SEngine.getBlueScore();
        //System.out.println("GAME HAS ENDED");
        //System.out.println("PLAYER 1 (RED) SCORE: "+p1score);
        //System.out.println("PLAYER 2 (BLUE) SCORE: "+p2score);
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

    public static void main(String[] args) {
        rundev dev = new rundev();
        dev.init();

        dev.update();
    }
}
