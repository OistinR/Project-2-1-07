package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;
import java.util.Random;

public class FitnessEngine {

    private ScoringEngine SEngine;
    private boolean DEBUG;
    private Hexagon.state player;
    private Hexagon.state opp;
    private boolean random;
    private int list [];
    Random rand = new Random();

    public FitnessEngine(state player, state opp,boolean random){
        this.player = player;
        this.opp = opp;
        SEngine = new ScoringEngine();
        DEBUG = false;
        list = new int[]{1, 3, 10, -10, -8, -6, -4, -2, -1};
        this.random = random;
        if(random){
            for (int i = 0; i < list.length; i++) {
                list[i] = rand.nextInt(21);
                System.out.println(list[i]);
            }

        }
        else{
            list = new int[]{1, 3, 10, -10, -8, -6, -4, -2, -1};
        }
    }

    public void update(ArrayList<Hexagon> field){

        for(Hexagon h:field) {
            if (h.getMyState() == state.BLANK && !h.getChecked()) {// maybe change UwU
                h.setMyState(player); //Simulate if the hex is RED

                h.setChecked(true);
                SEngine.floodcount = 1;
                SEngine.floodfill(h, field, player);
                //System.out.println("group size of red " + SEngine.floodcount);
                updateHexFitness(SEngine.floodcount, h, 1,random);
                if (DEBUG) System.out.print("this is the fitness of placing our colour " + h.getFitness1());
                ///////////////////////////////////////////////////////////////////
                h.setMyState(opp); //Simulate if the hex is RED

                SEngine.floodcount = 1;
                SEngine.floodfill(h, field, opp);
                //System.out.println("group size of blue " + SEngine.floodcount);
                updateHexFitness(SEngine.floodcount, h, -1,random);
                if (DEBUG) System.out.println(" this is the fitness of placing the others colour " + h.getFitness2());
                if (DEBUG) System.out.println(h.getR() + " " + h.getQ());

                h.setMyState(state.BLANK); //Put the hex back to BLANK
                SEngine.resetChecked(field);
                if (DEBUG) System.out.println("placing our colour there " + (h.getFitness1() - h.getFitness2()));
            }
        }
        if(DEBUG)System.out.println("////////////////////////////////////////");
    }

    public void updateHexFitness(int floodcount, Hexagon h, int FA, boolean random){ //FA is Fitness Adapter
        switch (floodcount){
            case 1:
                h.setMyFitness(1*FA,FA);
                break;
            case 2:
                h.setMyFitness(3*FA,FA);
                break;
            case 3:
                h.setMyFitness(10*FA,FA);
                break;
            case 4:
                h.setMyFitness(-10*FA,FA);
                break;
            case 5:
                h.setMyFitness(-8*FA,FA);
                break;
            case 6:
                h.setMyFitness(-6*FA,FA);
                break;
            case 7:
                h.setMyFitness(-4*FA,FA);
                break;
            case 8:
                h.setMyFitness(-2*FA,FA);
                break;
            default:
                h.setMyFitness(-1*FA,FA);
        }



    }
}
