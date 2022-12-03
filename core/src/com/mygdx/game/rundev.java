package com.mygdx.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.FitnessGroupBot;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;
import com.mygdx.game.bots.OLABot;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.experiment.experiment;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class rundev {
    public enum state {
        P1P1,
        P1P2,
        P1P3,
        P2P1,
        P2P2,
        P2P3
    }

    private int round;
    private ArrayList<Float> dataWinPerc = new ArrayList<Float>();
    private ArrayList<Float> dataLossPerc = new ArrayList<Float>();
    private ArrayList<Float> dataDrawPerc = new ArrayList<Float>();
    private ArrayList<Float> dataWins = new ArrayList<Float>();
    private ArrayList<Float> dataDraws = new ArrayList<Float>();
    private experiment exp = new experiment();
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
    private double winperc1, winperc2, winpercd;

    public void init() {
        // Initiate variables
        round = 1;
        totalnumgames = 10000;
        gamefinished = false;
        field = new ArrayList<>();
        SEngine = new ScoringEngine();
        bot1wins = new ArrayList<>();
        bot2wins = new ArrayList<>();
        draws = new ArrayList<>();

        // Initiate fieldsize
        fieldsize = 2;

        // Create field and initiate bots
        botpone = new MaxN_Paranoid_Bot(Hexagon.state.RED, Hexagon.state.BLUE);
        botptwo = new FitnessGroupBot(Hexagon.state.BLUE, Hexagon.state.RED, false);
        createHexagonFieldDefault();
    }

    public void update() {

        int i = 0;
        while (i <= totalnumgames) {
            while (!gamefinished) {
                updateState();
                makeMove();
                updateState();
            }
            if (i % 100 == 0) {
                System.out.println(i + " games simulated.");
            }

            round = 1;
            gamefinished = false;
            field = new ArrayList<>();
            SEngine = new ScoringEngine();
            createHexagonFieldDefault();

            i++;
        }
        winperc1 = ((double) bot1wins.size() / (double) totalnumgames) * 100;
        winperc2 = ((double) bot2wins.size() / (double) totalnumgames) * 100;
        winpercd = ((double) draws.size() / (double) totalnumgames) * 100;

        System.out.println("Bot 1 number of wins: " + bot1wins.size() + " Win percentage: " + winperc1 + " %");
        System.out.println("Bot 2 number of wins: " + bot2wins.size() + " Win percentage: " + winperc2 + " %");
        System.out.println("Number of draws: " + draws.size() + " Draw percentage: " + winpercd + " %");

    }

    public void createHexagonFieldDefault() {
        int s;
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, 0, 0));
                }
            }
        }
    }

    public void updateState() {
        int numhex = numHex() - 4 * (round - 1);

        if (field.size() - (numhex + (4 * (round - 1))) < 4) {
            gameFinish();
        }
    }

    public int numHex() {
        int num = 0;
        for (Hexagon h : field) {
            if ((h.getMyState() != Hexagon.state.BLANK)) {
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
        int p1score = SEngine.getRedScore();
        int p2score = SEngine.getBlueScore();
        // System.out.println("GAME HAS ENDED");
        // System.out.println("PLAYER 1 (RED) SCORE: "+p1score);
        // System.out.println("PLAYER 2 (BLUE) SCORE: "+p2score);
        if (p1score > p2score) {
            // System.out.println("PLAYER 1 (RED) WON");
            bot1wins.add(p1score);
        } else if (p1score < p2score) {
            // System.out.println("PLAYER 2 (BLUE) WON");
            bot2wins.add(p2score);
        } else {
            // System.out.println("THE GAME IS A DRAW");
            draws.add(p1score);
        }

        gamefinished = true;
    }

    public static void main(String[] args) {
        rundev dev = new rundev();
        dev.init();
        dev.update();

        String filePath = "C:/Users/Fred/Documents/GitHub/Project-2-1-07/core/src/com/mygdx/game/experiment/CSV Files/lossesPerc.csv";

        dev.dataLossPerc.add((float) (100 - dev.winperc1));
        dev.dataLossPerc.add((float) (100 - dev.winperc2));
        try {
            dev.exp.writeDatatoCSV(filePath, dev.dataLossPerc);
            System.out.println("Wrote in lossesPerc.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        filePath = "C:/Users/Fred/Documents/GitHub/Project-2-1-07/core/src/com/mygdx/game/experiment/CSV Files/winsPerc.csv";

        dev.dataWinPerc.add((float) (dev.winperc1));
        dev.dataWinPerc.add((float) (dev.winperc2));
        try {
            dev.exp.writeDatatoCSV(filePath, dev.dataWinPerc);
            System.out.println("Wrote in winsPerc.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        filePath = "C:/Users/Fred/Documents/GitHub/Project-2-1-07/core/src/com/mygdx/game/experiment/CSV Files/drawsPerc.csv";

        dev.dataDrawPerc.add((float) (dev.winpercd));
        try {
            dev.exp.writeDatatoCSV(filePath, dev.dataDrawPerc);
            System.out.println("Wrote in drawsPerc.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        filePath = "C:/Users/Fred/Documents/GitHub/Project-2-1-07/core/src/com/mygdx/game/experiment/CSV Files/numberOfWins.csv";

        dev.dataWins.add((float) (dev.bot1wins.size()));
        dev.dataWins.add((float) (dev.bot2wins.size()));
        try {
            dev.exp.writeDatatoCSV(filePath, dev.dataWins);
            System.out.println("Wrote in numberOfWins.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        filePath = "C:/Users/Fred/Documents/GitHub/Project-2-1-07/core/src/com/mygdx/game/experiment/CSV Files/numberOfDraws.csv";

        dev.dataDraws.add((float) (dev.draws.size()));
        try {
            dev.exp.writeDatatoCSV(filePath, dev.dataDraws);
            System.out.println("Wrote in numberOfDraws.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
