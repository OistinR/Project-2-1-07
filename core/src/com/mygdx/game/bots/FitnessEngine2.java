package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.bots.FitnessEngine;

/**
 * This is the second Fitness engine that we have created, its purpose is to help bot making moves based on the fitness value that
 * the engine returns, compared to the first one, it also attributes a fitness score for the different hexagon that we colour,
 * but the calculation of the fitness score is based on the difference from the preview groups and the groups after the simulation
 */

public class FitnessEngine2 {

    private ScoringEngine SEngine;
    private boolean DEBUG;
    private Hexagon.state player;
    private Hexagon.state opp;

    private ArrayList<Integer> oldBlueList;
    private ArrayList<Integer> oldRedList;

    private ArrayList<Integer> newBlueList;
    private ArrayList<Integer> newRedList;

    private ArrayList<Integer> blueListWD;
    private ArrayList<Integer> redListWD;

    private int fitnessOld;
    private int fitnessNew;

    /**
     *
     * @param player to know the players colour, to try to increase our score
     * @param opp to know the opponent colour, to try to decrease his score
     */

    public FitnessEngine2(state player, state opp){
        this.player = player;
        this.opp = opp;
        SEngine = new ScoringEngine();
        DEBUG = true;
    }

    public void update(ArrayList<Hexagon> field){
        for(Hexagon h:field){

            if(h.getMyState()==state.BLANK && h.getChecked()==false){
                SEngine.calculate(field);
                oldRedList = SEngine.getRedList();
                h.setMyState(player); //Simulate if the hex is RED

                SEngine.calculate(field);

                newRedList = SEngine.getRedList();
                h.setMyState(state.BLANK); //Put the hex back to BLANK

                if(DEBUG)System.out.println("this is oldred " + oldRedList);
                if(DEBUG)System.out.println("this is newred " + newRedList);

                if(oldRedList.size() > 0){
                    removeNotAffected(oldRedList,newRedList);
                    fitnessOld = countFitness(oldRedList,player,h);
                    fitnessNew = countFitness(newRedList,player,h);
                }
                h.setMyFitness((fitnessNew-fitnessOld),1);
                /////////////////////////////////////////
                SEngine.calculate(field);
                oldBlueList = SEngine.getBlueList();
                h.setMyState(opp); //Simulate if the hex is BLUE

                SEngine.calculate(field);

                newBlueList = SEngine.getBlueList();
                h.setMyState(state.BLANK); //Put the hex back to BLANK

                if(DEBUG)System.out.println("this is oldblue " + oldBlueList);
                if(DEBUG)System.out.println("this is newblue " + newBlueList);

                if(oldBlueList.size() > 0){
                    removeNotAffected(oldBlueList,newBlueList);
                    fitnessOld = countFitness(oldBlueList,opp,h);
                    fitnessNew = countFitness(newBlueList,opp,h);
                }


                h.setMyFitness(fitnessNew-fitnessOld,-1);


                SEngine.resetChecked(field);

            }

        }
    }
    public void removeNotAffected(ArrayList<Integer>oldList,ArrayList<Integer>newList){
        for (int i = 0; i < oldList.size(); i++) {
            for (int j = 0; j < newList.size(); j++) {
                if(oldList.get(i) == newList.get(j)){
                    oldList.remove(i);
                    newList.remove(j);
                    i--;
                    j--;
                    if(i<=0 || j<=0){
                        break;
                    }
                }
            }
        }


    }
    public int countFitness(ArrayList<Integer>List,state playerA, Hexagon h){
        int fitness = 0;
        int value = 0;
        int temp = 0;
        for (int i = 0; i < List.size(); i++) {
            value = List.get(i);
            temp = getFitness(value);
            fitness += temp;
        }
        if(DEBUG)System.out.println(fitness);
        return fitness;
        //if(player == playerA){h.setMyFitness((-1)*fitness,1);}

        //else{h.setMyFitness(fitness,-1);}
    }
    public int getFitness(int value){ //FA is Fitness Adapter
        switch (value){
            case 1:
                value = 1;
                return value;
            case 2:
                value = 5;
                return value;
            case 3:
                value = 10;
                return value;
            case 4:
                value = 5;
                return value;
            case 5:
                value = 4;
                return value;
            case 6:
                value = 3;
                return value;
            case 7:
                value = 2;
                return value;
            case 8:
                value = 1;
                return value;
            default:
                value = 1;
                return value;
        }
    }
}

