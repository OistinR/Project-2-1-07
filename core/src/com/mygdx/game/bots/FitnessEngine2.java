package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.bots.FitnessEngine;

public class FitnessEngine2 {

    private ScoringEngine SEngine;
    private FitnessEngine FEngine;
    private boolean DEBUG;
    private Hexagon.state player;
    private Hexagon.state opp;

    private ArrayList<Integer> oldBlueList;
    private ArrayList<Integer> oldRedList;

    private ArrayList<Integer> newBlueList;
    private ArrayList<Integer> newRedList;

    private ArrayList<Integer> blueListWD;
    private ArrayList<Integer> redListWD;

    public FitnessEngine2(state player, state opp){
        this.player = player;
        this.opp = opp;
        SEngine = new ScoringEngine();
        FEngine = new FitnessEngine(Hexagon.state.RED, Hexagon.state.BLUE);
        DEBUG = false;
    }

    public void update(ArrayList<Hexagon> field){
        for(Hexagon h:field){

            if(h.getMyState()==state.BLANK && h.getChecked()==false){
                oldRedList = SEngine.getRedList();
                h.setMyState(player); //Simulate if the hex is RED

                h.setChecked(true);
                SEngine.floodcount=1;
                SEngine.floodfill(h,field,player);

                FEngine.updateHexFitness(SEngine.floodcount,h,1);

                newRedList = SEngine.getRedList();
                ///////////////////////////////////////////////////////////////////
                h.setMyState(state.BLANK); //Put the hex back to BLANK
                oldBlueList = SEngine.getBlueList();
                h.setMyState(opp); //Simulate if the hex is BLUE

                SEngine.floodcount=1;
                SEngine.floodfill(h,field,opp);

                FEngine.updateHexFitness(SEngine.floodcount,h,-1);

                if(DEBUG)System.out.println(h.getR() + " " + h.getQ());
                newBlueList = SEngine.getBlueList();

                h.setMyState(state.BLANK); //Put the hex back to BLANK
                redListWD = removeNotAffected(oldRedList,newRedList);
                blueListWD = removeNotAffected(oldBlueList,newBlueList);

                countFitness(redListWD,true,h);
                countFitness(blueListWD,false,h);

                SEngine.resetChecked(field);

            }

        }
    }
    public ArrayList<Integer> removeNotAffected(ArrayList<Integer>oldList,ArrayList<Integer>newList){
        for (int i = 0; i < oldList.size(); i++) {
            for (int j = 0; j < newList.size(); j++) {
                if(oldList.get(i) == newList.get(j)){
                    oldList.remove(i);
                    newList.remove(j);
                    i--;
                    j--;
                }
            }
        }


        return oldList;
    }
    public void countFitness(ArrayList<Integer>List,Boolean player, Hexagon h){
        int fitness = 0;
        int value = 0;
        int temp = 0;
        for (int i = 0; i < List.size(); i++) {
            value = List.get(i);
            temp = getFitness(value);
            fitness += temp;
        }
        if(player){h.setMyFitness(fitness,-1);}

        else{h.setMyFitness(fitness,1);}
    }
    public int getFitness(int value){ //FA is Fitness Adapter
        switch (value){
            case 1:
                value = 1;
                return value;
            case 2:
                value = 8;
                return value;
            case 3:
                value = 10;
                return value;
            case 4:
                value = 8;
                return value;
            case 5:
                value = 6;
                return value;
            case 6:
                value = 4;
                return value;
            case 7:
                value = 3;
                return value;
            case 8:
                value = 2;
                return value;
            default:
                value = 1;
                return value;
        }
    }
}

