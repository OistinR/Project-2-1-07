package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.OLABot;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class rundev {
    public enum state{
        P1P1,
        P1P2,
        P1P3,
        P2P1,
        P2P2,
        P2P3
    }

    private state STATE = state.P1P1;
    private int round;
    private int fieldsize;
    private boolean gamefinished;
    private ArrayList<Hexagon> field;
    private ScoringEngine SEngine;
    private Bot botpone;
    private Bot botptwo;

    public void init() {
        fieldsize=2;
        round=1;
        gamefinished=false;
		field = new ArrayList<>();
        SEngine = new ScoringEngine();
        botpone = new RandomBot();
        botptwo = new OLABot();
        createHexagonFieldDefault();
    }

    public void update() {
        int gameindex=0;
        updateState();
        showGame(gameindex);
        while(!gamefinished) {
            gameindex++;
            updateState();
            makeMove();
            showGame(gameindex);
            updateState();
        }      
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

        // if(numhex == 1 && STATE == state.P1P1) {
        //     STATE = state.P1P2;
        // } else if (numhex == 2 && (STATE == state.P1P2||STATE == state.P1P1)) {
        //     STATE = state.P1P3;
        // } else if (numhex == 3 && STATE == state.P2P1) {
        //     STATE = state.P2P2;
        // } else if (numhex == 4 && (STATE == state.P2P2||STATE == state.P2P1)) {
        //     STATE = state.P2P3;
        // }

        // if (STATE == state.P1P3 /* && CONFIRM INPUT HERE */) {
        //     STATE = state.P2P1;
        //     //BOT MOVE HERE
        // }

        // if (STATE == state.P2P3 /* && CONFIRM INPUT HERE */) {
        //     STATE = state.P1P1;
        //     //BOT MOVE HERE
        //     round++;
        // }
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

    public void showGame(int gamenum) {
        System.out.println("----------------"+gamenum+"----------------");
        int[] a = new int[fieldsize*2+1];
        for(int j=0; j<a.length; j++) {
            if(a.length/2+1>j) {
                a[j] = fieldsize+1+j;
            } else {
                a[j] = fieldsize+1-(j-fieldsize*2);
            }
        }
        int i=0;
        int k=0;
        int p=0;
        for(Hexagon hex:field) {
            if(hex.getMyState()!=Hexagon.state.HOVER) {
                if(i==0) {
                    for(int o=0; o<5-a[p];o++) {
                        System.out.print("   ");
                    }
                }
                k++;
                System.out.print(hex.getMyState()+""+i+" ");
                if(k==a[p]) {
                    System.out.println("");
                    p++;
                    k=0;
                    if(p<a.length) {
                        for(int o=0; o<5-a[p];o++) {
                            System.out.print("   ");
                        }
                    }
                }
                i++;
            }
        }
        SEngine.calculate(field);
        System.out.println("PLAYER 1 (RED) SCORE: "+SEngine.getRedScore());
        System.out.println("PLAYER 2 (BLUE) SCORE: "+SEngine.getBlueScore());

    }

    public void gameFinish() {
        System.out.println("GAME HAS ENDED");
        gamefinished=true;
    }

    public static void main(String[] args) {
        rundev dev = new rundev();
        dev.init();
        dev.update();
    }
}
