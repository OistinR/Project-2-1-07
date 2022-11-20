package com.mygdx.game.gametree;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class Node {
    ArrayList<Hexagon> field;
    ArrayList<Node> listOfChildren;
    int hexQ;
    int hexR;
    Hexagon.state hexState;
    Node parent;
    int depth;
    double combinedScore;
    double nodeScore;
    boolean root;
    int visitScore;
    ArrayList<Hexagon> usedHex;
    ScoringEngine SEngine = new ScoringEngine();

    public Node(Node Parent, Hexagon HexagonPlaced, boolean root){ // if node is child //create node if parent null
        this.root = root;
        this.parent = Parent;
        this.depth = parent.depth + 1; // should this be automated? option for setting it manually?
        this.listOfChildren = new ArrayList<Node>();
        this.usedHex = new ArrayList<Hexagon>();
        if(parent.hexState==state.RED){
            this.hexState = Hexagon.state.BLUE;
        }
        else{
            this.hexState = Hexagon.state.RED;
        }
        ArrayList<Hexagon> clone = new ArrayList<Hexagon>();
        ArrayList<Hexagon> usedClone = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : parent.field) {
                clone.add(h.clone());
            }
        } catch (Exception e) {}
        try {
            for(Hexagon h : parent.usedHex) {
                usedClone.add(h.clone());
            }
        } catch (Exception e) {}
        this.field = clone;
        for(Hexagon hex:this.field){
            if(hex.getQ()==HexagonPlaced.getQ() && hex.getR()==HexagonPlaced.getR()){
                hex.setMyState(hexState);
            }
        }
        this.usedHex = usedClone;
        this.hexQ = HexagonPlaced.getQ();
        this.hexR = HexagonPlaced.getR();
        this.hexState = HexagonPlaced.getMyState(); //get the state of the board from parent, then update with the hexagon
        this.visitScore = 0; // for monte carlo
        try{
            this.usedHex.add(HexagonPlaced.clone());
        }
        catch(Exception e){}
        this.nodeScore = this.assignScore();
        this.combinedScore = this.parent.combinedScore + this.nodeScore;
    }

    public Node(ArrayList<Hexagon> field, Hexagon HexagonPlaced, Hexagon.state state){ // if node is root
            this.root = true;
            this.parent = this;
            this.usedHex = new ArrayList<Hexagon>();
            this.depth = 0;
            this.combinedScore = 0;
            this.listOfChildren = new ArrayList<Node>();
            this.hexState = state;
            ArrayList<Hexagon> clone = new ArrayList<Hexagon>();
            try {
                for(Hexagon h : field) {
                    clone.add(h.clone());
                }
            } catch (Exception e) {}         
            this.field = clone;
            for(Hexagon hex:this.field){
                if(hex.getQ()==HexagonPlaced.getQ() && hex.getR()==HexagonPlaced.getR()){
                    hex.setMyState(hexState);
                }
            }
            this.hexQ = HexagonPlaced.getQ();
            this.hexR = HexagonPlaced.getR();
            try {
                this.usedHex.add(HexagonPlaced.clone());
            } catch (Exception e) {} 
            this.visitScore = 0;
            this.nodeScore = this.assignScore();
            this.combinedScore = this.parent.combinedScore + this.nodeScore;
        
    }

    public void createChildren(){  // get children, children have the opposite colour of the parent
        for(Hexagon hex:this.field){
            for(Hexagon hexUsed:this.usedHex){
            if((hex.getMyState()==Hexagon.state.BLANK)&&((hexUsed.getQ()!=hex.getQ()) || (hexUsed.getR()!=hex.getR()))){
                Node newNode = new Node(this,hex,false);
                //System.out.println(newNode.toString());
                //System.out.println("created node");
                if(this.getState()==Hexagon.state.RED){
                   newNode.setState(Hexagon.state.BLUE);
                }
                else{
                   newNode.setState(Hexagon.state.RED);
                }
                this.listOfChildren.add(newNode);
            }
        }}
    }

    public void createNewPly(){
        for(Node n:this.listOfChildren){
            n.createChildren();
        }
    }

    public Node getNextNode(){
        int random = ThreadLocalRandom.current().nextInt(0, this.getChildArray().size()-1);
        if(this.usedHex.contains(this.getChildArray().get(random))){
            return this.getNextNode();
        }
        return this.getChildArray().get(random);
    }

    public double assignScore(){  // get score based on colour AFTER the hexagon in the node is placed
        SEngine.calculate(this.field);
        if(this.hexState==Hexagon.state.RED){
            return SEngine.getRedScore();
        
        }
        else{
            return SEngine.getBlueScore();
        }
    }

    public ArrayList<Hexagon> getField(){
        return this.field;
    }

    public int getDepth(){
        return this.depth;
    }


    public Node getParent(){
        return this.parent;
    }

    public List<Node> getChildArray(){
        return this.listOfChildren;
    }
    
    public int getQ(){
        return this.hexQ;
    }

    public int getR(){
        return this.hexR;
    }

    public Hexagon.state getState(){
        return this.hexState;
    }

    public void setState(Hexagon.state newState){
        this.hexState = newState;
    }


    public String toString(){ // could be expanded
        return("Depth: " + this.depth + " Score: " + this.nodeScore + " Q: " + this.hexQ + " R: " + this.hexR + " S: " + (-this.hexQ-this.hexR) + " State: " + this.hexState);
    }

    public void listChildren(){
        int sum = 0;
        for(Node node:this.listOfChildren){
            System.out.println(node.toString());
            sum++;
        }
        System.out.println("Total children: " + sum);
        if(this.listOfChildren.size() == 0){
            System.out.println("Array is null and Miel is a cunt");
        }
    }
}
