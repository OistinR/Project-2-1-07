package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.screens.GameScreen;

import javax.swing.*;
import java.util.ArrayList;

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

        int one = getBestHexagon(field, GameScreen.state.P2P1);
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
        int t = 0;
        int count = 0;
        int maxFit = -50;
        for (Hexagon h:field) {

            if(h.getMyState()!= Hexagon.state.BLANK){
                count++;
                continue;
            }

            switch(stateGame){
                case P1P1:
                    if(h.getFitness1()-h.getFitness2()>=maxFit){
                        maxFit = h.getFitness1()-h.getFitness2();
                        t = count;
                    }
                    break;
                case P1P2:
                    if(h.getFitness2()-h.getFitness1()>=maxFit){
                        maxFit = h.getFitness2()-h.getFitness1();
                        t = count;
                    }
                    break;
                case P2P1:
                    if((h.getFitness1()-h.getFitness2())*-1>=maxFit){
                        maxFit = (h.getFitness1()-h.getFitness2())*-1;
                        t = count;
                    }
                    break;
                case P2P2:
                    if((h.getFitness2()-h.getFitness1())*-1>=maxFit){
                        maxFit = (h.getFitness2()-h.getFitness1())*-1;
                        t = count;
                    }
                    break;
            }
        count++;
        }
        return t;
    }
}


