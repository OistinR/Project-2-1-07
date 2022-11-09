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

    private static boolean DEBUG = true;

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
        if (DEBUG) System.out.println("Arrived at calculate");

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
            int pos = MaxNRed(current_state_clone);
            field.get(pos).setMyState(Hexagon.state.RED);


            ArrayList<Hexagon> current2_state_clone = new ArrayList<>();
            try {
                for(Hexagon hexagon : field) {
                    current2_state_clone.add(hexagon.clone());
                }
            } catch (Exception e) {}

            if (DEBUG) System.out.println("use paranoid, Color BLUE");
            int pos2 = ParanoidBlue(current2_state_clone);
            field.get(pos2).setMyState(Hexagon.state.BLUE);
        }
        else{
            //Bot is Player 2, so Paranoid on RED and MaxN on BLUE
            if (DEBUG) System.out.println("use paranoid, Color RED");
            int pos = ParanoidRed(current_state_clone);
            field.get(pos).setMyState(Hexagon.state.RED);

            ArrayList<Hexagon> current2_state_clone = new ArrayList<>();
            try {
                for(Hexagon hexagon : field) {
                    current2_state_clone.add(hexagon.clone());
                }
            } catch (Exception e) {}

            if (DEBUG) System.out.println("use maxn, Color BLUE");
            int pos2 = MaxNBlue(current2_state_clone);
            field.get(pos2).setMyState(Hexagon.state.BLUE);

        }
    }

    /**
     * @param field Arraylist of Hexagons building our field
     * returns the best hexagon maximizing our fitness score. The best move to play the tile of our own color.
     */
    public static int MaxNRed(ArrayList<Hexagon> field){

        if (DEBUG) System.out.println("MaxN Reached for RED");

        ScoringEngine se = new ScoringEngine();
        Hexagon bestMove = field.get(0);
        ArrayList<Hexagon> bestMOVES = new ArrayList<>(); //List of bestMoves
        ArrayList<Integer> bestScores = new ArrayList<>(); //List of bestScores


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

        if ( (bestMOVES.size() > 1) && (bestScores.size() > 1) ){
            if (DEBUG) System.out.println("Multiple Optimal Moves");
            int coordSum = 0;
            int highestCoords = Integer.MIN_VALUE;
            Hexagon bestHex = bestMOVES.get(0);

            for(int select = 0; select < bestMOVES.size() ; select++) {
                coordSum = (Math.abs(bestMOVES.get(select).getQ()) + Math.abs(bestMOVES.get(select).getR()) + Math.abs(bestMOVES.get(select).getS()));
                if (coordSum > highestCoords) {
                    highestCoords = coordSum;
                    bestHex = bestMOVES.get(select);
                    if (DEBUG) System.out.println("New Highest: " + highestCoords);
                }
            }
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestHex){
                    return j;
                }
            }
        }
        else{
            System.out.println("Only 1 Optimal Move!");
            // We just play the best move as there is only one available.
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestMOVES.get(0)){
                    return j;
                }
            }
        }
        //unreachable
        return 0;
    }

    public static int MaxNBlue(ArrayList<Hexagon> field){

        if (DEBUG) System.out.println("MaxN Reached For BLUE");

        ScoringEngine se = new ScoringEngine();
        Hexagon bestMove = field.get(0);
        ArrayList<Hexagon> bestMOVES = new ArrayList<>(); //List of bestMoves
        ArrayList<Integer> bestScores = new ArrayList<>(); //List of bestScores


        // We initialize the bestMove for this turn as being the first blank tile encountered.
        for(int start = 0; start<field.size(); start++){
            if(field.get(start).getMyState() == Hexagon.state.BLANK) {
                bestMove = field.get(start); // take the default first blank tile of the board we encounter
                bestMove.setMyState(Hexagon.state.BLUE); // Change its color to red
                bestScores.add((Integer) se.getBlueScore()); // Save the score to simulate one turn.
                bestMove.setMyState(Hexagon.state.BLANK); // Change it again to blank.
                bestMOVES.add(bestMove);
                break;
            }
        }

        // loop through game and select move with best game score
        for(int i = 0; i<field.size(); i++){
            if(field.get(i).getMyState() == Hexagon.state.BLANK){

                field.get(i).setMyState(Hexagon.state.BLUE);

                if( (Integer) se.getBlueScore() > bestScores.get(0)){
                    for(Integer j = 0; j < bestScores.size(); j++){
                        bestScores.remove(j);
                    }
                    bestScores.add( (Integer) se.getBlueScore());

                    for(int z = 0; z < bestMOVES.size(); z++){
                        bestMOVES.remove(z);
                    }
                    bestMove = field.get(i);
                    bestMOVES.add(bestMove);
                }
                if( (Integer) se.getBlueScore() == bestScores.get(0)){
                    bestScores.add( (Integer) se.getBlueScore());
                    bestMOVES.add(field.get(i));
                }
                field.get(i).setMyState(Hexagon.state.BLANK);
            }
        }

        if ( (bestMOVES.size() > 1) && (bestScores.size() > 1) ){
            if (DEBUG) System.out.println("Multiple Optimal Moves");
            int coordSum = 0;
            int highestCoords = Integer.MIN_VALUE;
            Hexagon bestHex = bestMOVES.get(0);

            for(int select = 0; select < bestMOVES.size() ; select++) {
                coordSum = (Math.abs(bestMOVES.get(select).getQ()) + Math.abs(bestMOVES.get(select).getR()) + Math.abs(bestMOVES.get(select).getS()));
                if (coordSum > highestCoords){
                    highestCoords = coordSum;
                    bestHex = bestMOVES.get(select);
                    if (DEBUG) System.out.println("New Highest: " + highestCoords);
                }
            }
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestHex){
                    return j;
                }
            }
        }

        else {
            System.out.println("Only 1 Optimal Move!");
            // We just play the best move as there is only one available.
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestMOVES.get(0)) {
                    return j;
                }
            }
        }
        //unreachable
        return 0;
    }

    /**
     * @param field Arraylist of Hexagons building our field
     * returns the best hexagon maximizing our fitness score. The best move to play the tile of the opponent's color.
     */ 

    public static int ParanoidBlue(ArrayList<Hexagon> field){

        if (DEBUG) System.out.println("Paranoid Reached for BLUE");
        ScoringEngine se = new ScoringEngine();
        Hexagon bestMove = field.get(0);
        ArrayList<Hexagon> bestMOVES = new ArrayList<>(); //List of bestMoves
        ArrayList<Integer> bestScores = new ArrayList<>(); //List of bestScores

        // loop through game and select move with best fitness score, GOAL : Minimize score
        for(int start = 0; start<field.size(); start++){
            if(field.get(start).getMyState() == Hexagon.state.BLANK) {
                bestMove = field.get(start);
                bestMove.setMyState(Hexagon.state.BLUE); // Change its color to red
                bestScores.add((Integer) se.getBlueScore()); // Save the score to simulate one turn.
                bestMove.setMyState(Hexagon.state.BLANK); // Change it again to blank.
                bestMOVES.add(bestMove);
                break;
            }
        }

        for(int i = 0; i<field.size(); i++){
            if(field.get(i).getMyState() == Hexagon.state.BLANK){

                field.get(i).setMyState(Hexagon.state.BLUE);

            if( (Integer) se.getBlueScore() < bestScores.get(0)){
                for(Integer j = 0; j < bestScores.size(); j++){
                        bestScores.remove(j);
                }
                bestScores.add( (Integer) se.getBlueScore());
                
                for(int z = 0; z < bestMOVES.size(); z++){
                    bestMOVES.remove(z);
                }
                bestMove = field.get(i);
                bestMOVES.add(bestMove);
            }
            if( (Integer) se.getBlueScore() == bestScores.get(0)){
                bestScores.add( (Integer) se.getBlueScore());
                bestMOVES.add(field.get(i));
            }
            field.get(i).setMyState(Hexagon.state.BLANK);
            }
        }

        if ( (bestMOVES.size() > 1) && (bestScores.size() > 1) ){
            if (DEBUG) System.out.println("Multiple Optimal Moves");
            int coordSum = 0;
            int lowestCoords = Integer.MAX_VALUE;
            Hexagon bestHex = bestMOVES.get(0);

            for(int select = 0; select < bestMOVES.size() ; select++) {
                coordSum = (Math.abs(bestMOVES.get(select).getQ()) + Math.abs(bestMOVES.get(select).getR()) + Math.abs(bestMOVES.get(select).getS()));
                if (coordSum < lowestCoords){
                    lowestCoords = coordSum;
                    bestHex = bestMOVES.get(select);
                    if (DEBUG) System.out.println("New Lowest: " + lowestCoords);
                }
            }
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestHex){
                    return j;
                }
            }
        }
        else{
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestMOVES.get(0)){
                    return j;
                }
            }
        }
        //unreachable
        return 0;
    }

    public static int ParanoidRed(ArrayList<Hexagon> field){

        if (DEBUG) System.out.println("Paranoid Reached for RED");
        ScoringEngine se = new ScoringEngine();
        Hexagon bestMove = field.get(0);
        ArrayList<Hexagon> bestMOVES = new ArrayList<>(); //List of bestMoves
        ArrayList<Integer> bestScores = new ArrayList<>(); //List of bestScores

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

        if ( (bestMOVES.size() > 1) && (bestScores.size() > 1) ){
            if (DEBUG) System.out.println("Multiple Optimal Moves");
            int coordSum = 0;
            int lowestCoords = Integer.MAX_VALUE;
            Hexagon bestHex = bestMOVES.get(0);

            for(int select = 0; select < bestMOVES.size() ; select++) {
                coordSum = (Math.abs(bestMOVES.get(select).getQ()) + Math.abs(bestMOVES.get(select).getR()) + Math.abs(bestMOVES.get(select).getS()));
                if (coordSum < lowestCoords){
                    lowestCoords = coordSum;
                    bestHex = bestMOVES.get(select);
                    if (DEBUG) System.out.println("New Lowest: " + lowestCoords);
                }
            }
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestHex){
                    return j;
                }
            }
        }

        else{
            for (int j = 0; j < field.size(); j++) {
                if (field.get(j) == bestMOVES.get(0)){
                    return j;
                }
            }
        }
        //unreachable
        return 0;
    }
}
