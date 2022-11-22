package com.mygdx.game.gametree;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;

public class Node {
    private ArrayList<Hexagon> field;
    private ArrayList<Node> listOfChildren;
    private int hexQ;
    private int hexR;
    private Hexagon.state hexState;
    private Node parent;
    private int depth;
    private double combinedScore;
    private double nodeScore;
    private int visitScore;
    private ArrayList<Hexagon> usedHex;
    private ScoringEngine SEngine = new ScoringEngine();
    private GameScreen.state Phase;

    /**
     * This is the constructor for the root node.
     * a lot its fields are null since they are not needed.
     *
     * @param field the fields current state
     * @param Phase the games current state
     */

    public Node(ArrayList<Hexagon> field, GameScreen.state Phase){
        parent = null;
        depth = 0;
        listOfChildren = new ArrayList<Node>();
        hexState = null;
        visitScore = 0;
        nodeScore = 0;
        combinedScore = 0;
        this.Phase = Phase;

        ArrayList<Hexagon> clone = new ArrayList<Hexagon>();

        try {
            for(Hexagon h : field) {
                clone.add(h.clone());
            }
        } catch (Exception e) {}
        this.field = clone;
    }

    // if node is a non-root node:
    public Node(Node Parent, int hexQ, int hexR){
        parent = Parent;
        depth = parent.depth + 1;
        listOfChildren = new ArrayList<Node>();

        //get parent phase and make ours the next phase
        switch (Parent.getPhase()){
            case P1P1: this.Phase = GameScreen.state.P1P2;break;
            case P1P2: this.Phase = GameScreen.state.P2P1;break;
            case P2P1: this.Phase = GameScreen.state.P2P2;break;
            case P2P2: this.Phase = GameScreen.state.P1P1;break;
        }

        if (parent.getParent()==null){//if our parent is a root we take the roots phase since thats the phase we want to repersent on the first layer.
            this.Phase = parent.getPhase();
        }

        switch (this.Phase){// this declares which color we are placing depending on phase
            case P1P1:
            case P2P1:
                hexState = state.RED; break;
            case P1P2:
            case P2P2:
                hexState = state.BLUE; break;
        }

        ArrayList<Hexagon> clone = new ArrayList<Hexagon>();

        try {
            for(Hexagon h : parent.getField()) {
                clone.add(h.clone());
            }
        } catch (Exception e) {}

        this.field = clone;

        //actually place the piece on the board. (how can this be improved?) Hashmap?
        for(Hexagon hex:this.field){
            if(hex.getQ()==hexQ && hex.getR()==hexR){
                hex.setMyState(hexState);
            }
        }

        this.hexQ = hexQ;
        this.hexR = hexR;

    }

    public void assignScore(boolean playerOne){
        SEngine.calculate(field);
        //hard coded player 2
        //TODO implement score? based on evaluation function? also why does each node have a scoring engine we should change that
        if (playerOne){
            nodeScore = SEngine.getRedScore()-SEngine.getBlueScore();
        }
        else {
            nodeScore = SEngine.getBlueScore()-SEngine.getRedScore();

        }
        double childrenTotalScore = 0;
        for (Node n:listOfChildren) {
            childrenTotalScore+=n.getCombinedScore();
        }

        combinedScore = nodeScore+childrenTotalScore;
    }

    public GameScreen.state getPhase() {
        return Phase;
    }

    public ArrayList<Hexagon> getField(){
        return field;
    }

    public int getDepth(){
        return depth;
    }

    public Node getParent(){
        return parent;
    }

    public double getCombinedScore() {
        return combinedScore;
    }

    public List<Node> getChildArray(){
        return listOfChildren;
    }

    public boolean hasChildern(){
        return !listOfChildren.isEmpty();
    }
    
    public int getQ(){
        return hexQ;
    }

    public int getR(){
        return hexR;
    }

    public Hexagon.state getState(){
        return hexState;
    }

    public void setState(Hexagon.state newState){
        this.hexState = newState;
    }

    public String toString(){ // could be expanded
        return("Depth: " + depth + " Score: " + nodeScore + " score of leaf: "+ combinedScore +
                " Q: " + hexQ + " R: " + hexR + " S: " + (-hexQ-hexR) + " State: " + hexState);
    }

    public void listChildren(){
        int sum = 0;
        for(Node node:listOfChildren){
            System.out.println(node.toString());
            sum++;
        }
        System.out.println("Total children: " + sum);
        if(listOfChildren.size() == 0){
            System.out.println("Array is EMPTY");
        }
    }
}
