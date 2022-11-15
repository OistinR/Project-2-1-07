package com.mygdx.game.gametree;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.coordsystem.Hexagon.state;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class Node {
    ArrayList<Hexagon> field;
    ArrayList<Node> listOfChildren;
    Node parent;
    int depth;
    double combinedScore;
    Hexagon hexagonPlaced;
    double nodeScore;
    boolean root;
    int visitScore;
    ScoringEngine SEngine = new ScoringEngine();

    public Node(Node Parent, Hexagon HexagonPlaced){ // if node is child
        this.root = false;
        this.depth = parent.depth + 1; // should this be automated? option for setting it manually?
        this.parent = Parent;
        this.listOfChildren = new ArrayList<Node>();
        this.hexagonPlaced = HexagonPlaced;
        this.field = parent.field;
        this.field.add(HexagonPlaced); //get the state of the board from parent, then update with the hexagon
        this.visitScore = 0; // for monte carlo
        this.nodeScore = this.assignScore();
        this.combinedScore = this.parent.combinedScore + this.nodeScore;
    }

    public Node(Hexagon HexagonPlaced){ // if node is root
            this.root = true;
            this.parent = this;
            this.depth = 0;
            this.combinedScore = 0;
            this.listOfChildren = new ArrayList<Node>();
            this.hexagonPlaced = HexagonPlaced;
            this.field = parent.field;
            this.field.add(HexagonPlaced);
            this.visitScore = 0;
            this.nodeScore = this.assignScore();
            this.combinedScore = this.parent.combinedScore + this.nodeScore;
        
    }

    public void createChildren(){  // get children, children have the opposite colour of the parent
        for(Hexagon hex:this.field){
            if(hex.getMyState()==Hexagon.state.BLANK){
                Node newNode = new Node(this,hex);
                if(this.hexagonPlaced.getMyState()==state.RED){
                    hex.setMyState(state.BLUE);
                }
                else{
                    hex.setMyState(state.RED);
                }
                this.listOfChildren.add(newNode);
            }
        }
    }

    public double assignScore(){  // get score based on colour AFTER the hexagon in the node is placed
        if(this.hexagonPlaced.getMyState()==Hexagon.state.RED){
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

    public Hexagon getHexagonPlaced(){
        return this.hexagonPlaced;
    }

    public Node getParent(){
        return this.parent;
    }

    public List<Node> getChildArray(){
        return this.listOfChildren;
    }
    
    public String toString(){ // could be expanded
        return("Depth: " + this.depth + " Score: " + this.nodeScore + " Hexagon Placed: " + this.hexagonPlaced.toString());
    }
}
