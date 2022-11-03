package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;

import java.util.ArrayList;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class FitnessEngine {

    private ScoringEngine SEngine;
    private boolean simulation;

    public FitnessEngine(){
        SEngine = new ScoringEngine();
        simulation = true;
    }

    public void update(ArrayList<Hexagon> field){

        for(Hexagon h:field){

            if(h.getMyState()==state.BLANK && h.getChecked()==false){
                h.setMyState(state.RED); //Simulate if the hex is RED

                h.setChecked(true);
                SEngine.floodcount=1;
                SEngine.floodfill(h,field,Hexagon.state.RED,simulation);
                //System.out.println("group size of red " + SEngine.floodcount);
                updateHexFitness(SEngine.floodcount,h,1);
                System.out.print("this is the fitness of placing our colour " + h.getFitness1());
                ///////////////////////////////////////////////////////////////////
                h.setMyState(state.BLUE); //Simulate if the hex is RED

                SEngine.floodcount=1;
                SEngine.floodfill(h,field,Hexagon.state.BLUE,simulation);
                //System.out.println("group size of blue " + SEngine.floodcount);
                updateHexFitness(SEngine.floodcount,h,-1);
                System.out.println(" this is the fitness of placing the others colour " + h.getFitness2());
                System.out.println(h.getR() + " " + h.getQ());

                h.setMyState(state.BLANK); //Put the hex back to BLANK
            }

        }
        SEngine.resetChecked(field);
    }
    public void updateHexFitness(int floodcount, Hexagon h, int FA){ //FA is Fitness Adapter
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
