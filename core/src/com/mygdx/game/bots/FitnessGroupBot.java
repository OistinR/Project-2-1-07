package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;

public class FitnessGroupBot extends Bot{
    private Hexagon.state myColor;
    private Hexagon.state opponentColor;
    private boolean random;

    private FitnessEngine FE;

    /**
     *  This constructor sets the two variablesm and starts the fitness engine.
     *
     *  <h3>Why is FitnessEngine(Hexagon.state.RED, Hexagon.state.BLUE) hard-coded?</h3>
     *  Right now the way it is set up the fitness engine always uses RED and BLUE in this order for both bots.
     *  This is intentional, and I can explain why: The bots share the same fitness values for each tile,
     *  so basically for player 1 placing a red tile to make a group of 3 is good but for player two it is bad.
     *  This is represented in the formula Laurent came up with. If we use a different scoring engine all of a sudden
     *  the formulas no longer work. I can explain better in person. -Oistín
     *
     * @param myColor the colour I want to maximise
     * @param opponentColor the colour I was to us to minimise
     */
    public FitnessGroupBot(Hexagon.state myColor, Hexagon.state opponentColor, boolean random){
        this.myColor = myColor;
        this.opponentColor = opponentColor;
        this.random = random;
        FE = new FitnessEngine(Hexagon.state.RED, Hexagon.state.BLUE, random);
    }

    /** This method calculates the best place for the first hexagon(red)
     * then the best place for a blue hexagon. It uses the getBestHexagon() method.
     * First it checks our colour, (player 1 is red and player 2 is blue), then it runs getBestHexagon() and
     * updates the field.
     *
     * there are various checks involved and im not sure if they are all required. It needs a review.
     *
     * @param field the arraylist of hexagons in our field.
     */
 @Override
    public void calculate(ArrayList<Hexagon> field) { //does not support more than 2 players
        FE.update(field);
        int one = -1;
        if (myColor== Hexagon.state.BLUE){
            one = getBestHexagon(field, GameScreen.state.P2P1);
        }else {
            one = getBestHexagon(field, GameScreen.state.P1P1);
        }

        field.get(one).setMyState(Hexagon.state.RED);


        FE.update(field);

        int two = -1;
        if (myColor== Hexagon.state.RED){
            two = getBestHexagon(field, GameScreen.state.P2P2);
        }else {
            two = getBestHexagon(field, GameScreen.state.P1P2);
        }
        field.get(two).setMyState(Hexagon.state.BLUE);

    }


    public int calculateTree(ArrayList<Hexagon> field, GameScreen.state state) { //does not support more than 2 players
        FE.update(field);
        return getBestHexagon(field, state);
    }



    /**
     * This method searches through the field and finds the best place(s) to put a hexagon.
     * This of course depends on who is playing so, it accounts for: P1P1, P1P2, P2P1 and P2P2.(see ugly switch statement)
     *
     * For example if we are player 2, and we are placing our second tile, the way we value hexagons is different from
     * player one placing their first tile. this Value comes from a combination of fitness1 and fitness2.
     * <i>(Laurent's Equation)</i>
     *
     * Sometimes it finds multiple good solutions with equal weight, if that occurs it saves all the indices of the
     * hexagons that it has found and picks a random one.
     *
     * @param field the game field.
     * @param stateGame the current state the game is in this affects how we value hexagons.
     * @return the index of the best hexagon we found.
     */
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
            //right now this could be improved and needs to be in order to optimize. (which I may do in the future)
            switch(stateGame){
                case P1P1:
                    if(h.getFitness1()>=maxFit){
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
                    if(h.getFitness2()>=maxFit){
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
                    if((h.getFitness1())*-1>=maxFit){
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
                    if((h.getFitness2())*-1>=maxFit){
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

//        if(bestList.size()>1){
//            int coordSum = 0;
//            int lowestCoords = Integer.MAX_VALUE;
//            Hexagon bestHex = bestList.get(0);
//
//            for(int select = 0; select < bestList.get(0) ; select++) {
//                coordSum = (Math.abs(bestMOVES.get(select).getQ()) + Math.abs(bestMOVES.get(select).getR()) + Math.abs(bestMOVES.get(select).getS()));
//                if (coordSum < lowestCoords){
//                    lowestCoords = coordSum;
//                    bestHex = bestMOVES.get(select);
//                }
//            }
//            for (int j = 0; j < field.size(); j++) {
//                if (field.get(j) == bestHex){
//                    return j;
//                }
//            }
//            return bestList.get(r.nextInt(bestList.size()));
//        }
//        else{
            return bestList.get(0);
//        }

    }
}


