/*
 * The purpose of the bot is too apply the maxN & paranoid algorithm using the score of the game and not the fitness score.
 */

package com.mygdx.game.bots;

import java.util.ArrayList;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;


public class MaxN_Paranoid_Bot extends Bot {

    private static boolean DEBUG = false;

    private Hexagon.state myColor;
    private Hexagon.state opponentColor;

    public MaxN_Paranoid_Bot(Hexagon.state myColor, Hexagon.state opponentColor){
        this.myColor = myColor;
        this.opponentColor = opponentColor;

        if (DEBUG) System.out.println("MYCOLOR: " + myColor);
    }
    
    /**
     * @param field Arraylist of Hexagons building our field
     * Decision making method to determine the best move depending on the turn and the tile to place.
     */
    public void calculate(ArrayList<Hexagon> field){

        //we create a copy of the current state of the game and we apply maxn on it.
        ArrayList<Hexagon> current_state_clone = new ArrayList<>();
        try {
            for(Hexagon hexagon : field) {
                current_state_clone.add(hexagon.clone());
            }
        } catch (Exception e) {}
        // if its our turn to play and next tile is pink/red


        if(myColor == Hexagon.state.RED){
            //Bot is player 1, so MaxN on RED and Paranoid on BLUE
            if (DEBUG) System.out.println("use maxN, Color RED");
            int pos = MaxN(current_state_clone, myColor);
            field.get(pos).setMyState(Hexagon.state.RED);


            ArrayList<Hexagon> current2_state_clone = new ArrayList<>();
            try {
                for(Hexagon hexagon : field) {
                    current2_state_clone.add(hexagon.clone());
                }
            } catch (Exception e) {}

            if (DEBUG) System.out.println("use paranoid, Color BLUE");
            int pos2 = Paranoid(current2_state_clone, myColor);
            field.get(pos2).setMyState(Hexagon.state.BLUE);
        }
        else{
            //Bot is Player 2, so Paranoid on RED and MaxN on BLUE
            if (DEBUG) System.out.println("use paranoid, Color RED");
            int pos = Paranoid(current_state_clone, myColor);
            field.get(pos).setMyState(Hexagon.state.RED);

            ArrayList<Hexagon> current2_state_clone = new ArrayList<>();
            try {
                for(Hexagon hexagon : field) {
                    current2_state_clone.add(hexagon.clone());
                }
            } catch (Exception e) {}

            if (DEBUG) System.out.println("use maxn, Color BLUE");
            int pos2 = MaxN(current2_state_clone, myColor);

            field.get(pos2).setMyState(Hexagon.state.BLUE);

        }
    }

    /**
     * @param field Arraylist of Hexagons building our field
     * @param color Color of the bot whose score needs to be maximized
     * returns the best hexagon maximizing our fitness score. The best move to play the tile of our own color.
     */

    public static int MaxN(ArrayList<Hexagon> field, Hexagon.state color){

        ScoringEngine se = new ScoringEngine();
        Hexagon bestMove = field.get(0);
        ArrayList<Hexagon> bestMOVES = new ArrayList<>(); //List of bestMoves
        ArrayList<Integer> bestScores = new ArrayList<>(); //List of bestScores


        if(color == Hexagon.state.BLUE) {
            // We initialize the bestMove for this turn as being the first blank tile encountered.
            for (int start = 0; start < field.size(); start++) {
                if (field.get(start).getMyState() == Hexagon.state.BLANK) {
                    bestMove = field.get(start); // take the default first blank tile of the board we encounter
                    bestMove.setMyState(Hexagon.state.BLUE); // Change its color to red
                    bestScores.add((Integer) se.getBlueScore()); // Save the score to simulate one turn.
                    bestMove.setMyState(Hexagon.state.BLANK); // Change it again to blank.
                    bestMOVES.add(bestMove);
                    break;
                }
            }

            // loop through game and select move with best game score
            for (int i = 0; i < field.size(); i++) {
                if (field.get(i).getMyState() == Hexagon.state.BLANK) {

                    field.get(i).setMyState(Hexagon.state.BLUE);

                    if ((Integer) se.getBlueScore() > bestScores.get(0)) {
                        for (Integer j = 0; j < bestScores.size(); j++) {
                            bestScores.remove(j);
                        }
                        bestScores.add((Integer) se.getBlueScore());

                        for (int z = 0; z < bestMOVES.size(); z++) {
                            bestMOVES.remove(z);
                        }
                        bestMove = field.get(i);
                        bestMOVES.add(bestMove);
                    }
                    if ((Integer) se.getBlueScore() == bestScores.get(0)) {
                        bestScores.add((Integer) se.getBlueScore());
                        bestMOVES.add(field.get(i));
                    }
                    field.get(i).setMyState(Hexagon.state.BLANK);
                }
            }
        }
        else if (color == Hexagon.state.RED){
            // We initialize the bestMove for this turn as being the first blank tile encountered.
            for(int start = 0; start<field.size(); start++){
                if(field.get(start).getMyState() == Hexagon.state.BLANK) {
                    bestMove = field.get(start); // take the default first blank tile of the board we encounter
                    bestMove.setMyState(Hexagon.state.RED); // Change its color to red
                    bestScores.add((Integer) se.getRedScore()); // Save the score to simulate one turn.
                    bestMove.setMyState(Hexagon.state.BLANK); // Change it again to blank.
                    bestMOVES.add(bestMove);
                    break;
                }
            }

            // loop through game and select move with best game score
            for(int i = 0; i<field.size(); i++){
                if(field.get(i).getMyState() == Hexagon.state.BLANK){

                    field.get(i).setMyState(Hexagon.state.RED);

                    if( (Integer) se.getRedScore() > bestScores.get(0)){
                        for(Integer j = 0; j < bestScores.size(); j++){
                            bestScores.remove(j);
                        }
                        bestScores.add( (Integer) se.getRedScore());

                        for(int z = 0; z < bestMOVES.size(); z++){
                            bestMOVES.remove(z);
                        }
                        bestMove = field.get(i);
                        bestMOVES.add(bestMove);
                    }
                    if( (Integer) se.getRedScore() == bestScores.get(0)){
                        bestScores.add( (Integer) se.getRedScore());
                        bestMOVES.add(field.get(i));
                    }
                    field.get(i).setMyState(Hexagon.state.BLANK);
                }
            }
        }
        else{
            if(DEBUG) System.out.println("Impossible ELSE statement");
        }

        return getBestHex(field, bestMOVES, bestScores, "MaxN");
    }

    /**
     * @param field Arraylist of Hexagons building our field
     * @param color Color of the opponent whose score needs to be minimized
     * returns the best hexagon maximizing our fitness score. The best move to play the tile of the opponent's color.
     */ 

    public static int Paranoid(ArrayList<Hexagon> field, Hexagon.state color){

        ScoringEngine se = new ScoringEngine();
        Hexagon bestMove = field.get(0);
        ArrayList<Hexagon> bestMOVES = new ArrayList<>(); //List of bestMoves
        ArrayList<Integer> bestScores = new ArrayList<>(); //List of bestScores

        if (color == Hexagon.state.BLUE) {

            // loop through game and select move with best fitness score, GOAL : Minimize score
            for (int start = 0; start < field.size(); start++) {
                if (field.get(start).getMyState() == Hexagon.state.BLANK) {
                    bestMove = field.get(start);
                    bestMove.setMyState(Hexagon.state.BLUE); // Change its color to red
                    bestScores.add((Integer) se.getBlueScore()); // Save the score to simulate one turn.
                    bestMove.setMyState(Hexagon.state.BLANK); // Change it again to blank.
                    bestMOVES.add(bestMove);
                    break;
                }
            }

            for (int i = 0; i < field.size(); i++) {
                if (field.get(i).getMyState() == Hexagon.state.BLANK) {

                    field.get(i).setMyState(Hexagon.state.BLUE);

                    if ((Integer) se.getBlueScore() < bestScores.get(0)) {
                        for (Integer j = 0; j < bestScores.size(); j++) {
                            bestScores.remove(j);
                        }
                        bestScores.add((Integer) se.getBlueScore());

                        for (int z = 0; z < bestMOVES.size(); z++) {
                            bestMOVES.remove(z);
                        }
                        bestMove = field.get(i);
                        bestMOVES.add(bestMove);
                    }
                    if ((Integer) se.getBlueScore() == bestScores.get(0)) {
                        bestScores.add((Integer) se.getBlueScore());
                        bestMOVES.add(field.get(i));
                    }
                    field.get(i).setMyState(Hexagon.state.BLANK);
                }
            }
        }
        else if (color == Hexagon.state.RED){
            // loop through game and select move with best fitness score, GOAL : Minimize score
            for(int start = 0; start<field.size(); start++){
                if(field.get(start).getMyState() == Hexagon.state.BLANK) {
                    bestMove = field.get(start);
                    bestMove.setMyState(Hexagon.state.RED); // Change its color to red
                    bestScores.add((Integer) se.getRedScore()); // Save the score to simulate one turn.
                    bestMove.setMyState(Hexagon.state.BLANK); // Change it again to blank.
                    bestMOVES.add(bestMove);
                    break;
                }
            }

            for(int i = 0; i<field.size(); i++){
                if(field.get(i).getMyState() == Hexagon.state.BLANK){

                    field.get(i).setMyState(Hexagon.state.RED);

                    if( (Integer) se.getRedScore() < bestScores.get(0)){
                        for(Integer j = 0; j < bestScores.size(); j++){
                            bestScores.remove(j);
                        }
                        bestScores.add( (Integer) se.getRedScore());

                        for(int z = 0; z < bestMOVES.size(); z++){
                            bestMOVES.remove(z);
                        }
                        bestMove = field.get(i);
                        bestMOVES.add(bestMove);
                    }
                    if( (Integer) se.getRedScore() == bestScores.get(0)){
                        bestScores.add( (Integer) se.getRedScore());
                        bestMOVES.add(field.get(i));
                    }
                    field.get(i).setMyState(Hexagon.state.BLANK);
                }
            }
        }
        else{
            if(DEBUG)System.out.println("Impossible ELSE statement");
        }

        return getBestHex(field, bestMOVES, bestScores, "Paranoid");
    }

    public static int getBestHex(ArrayList<Hexagon> field, ArrayList<Hexagon> bestMOVES, ArrayList<Integer> bestScores, String whichAlg){
        if ( (bestMOVES.size() > 1) && (bestScores.size() > 1) ){
            if (DEBUG) System.out.println("Multiple Optimal Moves");
            int coordSum = 0;
            int highestCoords = Integer.MIN_VALUE;
            int lowestCoords = Integer.MAX_VALUE;
            Hexagon bestHex = bestMOVES.get(0);


            for(int select = 0; select < bestMOVES.size() ; select++) {
                coordSum = (Math.abs(bestMOVES.get(select).getQ()) + Math.abs(bestMOVES.get(select).getR()) + Math.abs(bestMOVES.get(select).getS()));
                if (whichAlg == "MaxN" ) {
                    if (coordSum > highestCoords) {
                        highestCoords = coordSum;
                        bestHex = bestMOVES.get(select);
                        if (DEBUG) System.out.println("New Highest: " + highestCoords);
                    }
                }
                else if (whichAlg == "Paranoid"){
                    if (coordSum < lowestCoords){
                        lowestCoords = coordSum;
                        bestHex = bestMOVES.get(select);
                        if (DEBUG) System.out.println("New Lowest: " + lowestCoords);
                    }
                }
                else{
                    if (DEBUG) System.out.println("Wrong algorithm name entered");
                }
            }
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestHex){
                    return j;
                }
            }
        }

        else {
            if (DEBUG) System.out.println("Only 1 Optimal Move!");
            // We just play the best move as there is only one available.
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestMOVES.get(0)) {
                    return j;
                }
            }
        }
        return 0; //cant be reached
    }
}
