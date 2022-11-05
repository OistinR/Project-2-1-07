package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.screens.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class FitnessGroupBot extends Bot{
    private Hexagon.state myColor;
    private Hexagon.state opponentColor;

    private FitnessEngine FE;

    public FitnessGroupBot(Hexagon.state myColor, Hexagon.state opponentColor){
        this.myColor = myColor;
        this.opponentColor = opponentColor;
        FE = new FitnessEngine(myColor, opponentColor);
    }

    @Override
    public void calculate(ArrayList<Hexagon> field) {
        FE.update(field);

        int one = getBestHexagon(field, GameScreen.state.P2P1);//this needs to change its hard coded rn to player 2
        if(field.get(one).getMyState()== Hexagon.state.BLANK)
            field.get(one).setMyState(Hexagon.state.RED);
        else
            System.out.println("could not find a good hex at index: "+one);

        FE.update(field);
        int two = getBestHexagon(field, GameScreen.state.P2P2);
        if(field.get(two).getMyState()== Hexagon.state.BLANK)
            field.get(two).setMyState(Hexagon.state.BLUE);
        else
            System.out.println("could not find a good hex at index: "+two);
    }

    public int getBestHexagon(ArrayList<Hexagon> field, GameScreen.state stateGame){
        ArrayList<Integer> bestList = new ArrayList<>();
        int t = 0;
        int count = 0;
        int maxFit = -50;
        for (Hexagon h:field) {

            if(h.getMyState()!= Hexagon.state.BLANK){
                count++;
                continue;
            }
            int highest = 0;
            //right now this could be improved and needs to be in order to optimize.
            switch(stateGame){
                case P1P1:
                    if(h.getFitness1()-h.getFitness2()>=maxFit){
                        maxFit = h.getFitness1()-h.getFitness2();
                        if(field.get(highest).getFitness1()-field.get(highest).getFitness2()==maxFit){
                            bestList.add(count);
                        }
                        else{
                            bestList.clear();
                            bestList.add(count);
                        }
                        t = count;
                    }
                    break;
                case P1P2:
                    if(h.getFitness2()-h.getFitness1()>=maxFit){
                        maxFit = h.getFitness2()-h.getFitness1();
                        if(field.get(highest).getFitness2()-field.get(highest).getFitness1()==maxFit){
                            bestList.add(count);
                        }
                        else{
                            bestList.clear();
                            bestList.add(count);
                        }
                        t = count;
                    }
                    break;
                case P2P1:
                    if((h.getFitness1()-h.getFitness2())*-1>=maxFit){
                        maxFit = (h.getFitness1()-h.getFitness2())*-1;
                        if((field.get(highest).getFitness1()-field.get(highest).getFitness2())*-1==maxFit){
                            bestList.add(count);
                        }
                        else{
                            bestList.clear();
                            bestList.add(count);
                        }
                        t = count;
                    }
                    break;
                case P2P2:
                    if((h.getFitness2()-h.getFitness1())*-1>=maxFit){
                        maxFit = (h.getFitness2()-h.getFitness1())*-1;
                        if((field.get(highest).getFitness2()-field.get(highest).getFitness1())*-1==maxFit){
                            bestList.add(count);
                        }
                        else{
                            bestList.clear();
                            bestList.add(count);
                        }
                        t = count;
                    }
                    break;
            }
        count++;
        }
        //not sure if this is needed
        if(bestList.size()>1){
            Random r = new Random();
            return bestList.get(r.nextInt(bestList.size()));
        }
        else{
            return bestList.get(0);
        }

    }
}


