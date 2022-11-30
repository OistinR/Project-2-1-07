package com.mygdx.game.gametree;

import com.mygdx.game.rundev;
import com.mygdx.game.bots.FitnessGroupBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.GameScreen.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MonteCarloTree {
    ArrayList<Node> nodes;
    ArrayList<Node> firstLayerNodes;
    int depth, width;
    ArrayList<Integer> Layers;
    FitnessGroupBot fgb;
    int totalVisit;
    int searchBias;

    public MonteCarloTree(int depth, int width){
        nodes = new ArrayList<>();
        this.depth=depth;
        this.width=width;
        Layers = new ArrayList<>();
        for (int i = 0; i < depth+1; i++) {
           Layers.add(i*width);
        }
        totalVisit = 0;
        searchBias = 10;
        firstLayerNodes = new ArrayList<>();
    }
    //boolean player is not needed we can obtain same info from board state to lazy to make switch statement rn
    public void generateTree(ArrayList<Hexagon> field, GameScreen.state boardState, boolean player){

        

        if (player)
            fgb = new FitnessGroupBot(Hexagon.state.RED, Hexagon.state.BLUE,false);
        else
            fgb = new FitnessGroupBot(Hexagon.state.BLUE, Hexagon.state.RED,false);

        nodes.clear();
        nodes.add(new Node(field, boardState));
        

        if(depth >0) {
            generateChildrenRandomly(nodes.get(0));
        }

        if (depth>1) {
            for (int j = 0; j < nodes.size(); j++) { //do not use enhanced for loop.
                int c = 0;
                for (Hexagon he: nodes.get(j).getField()) {
                    if (he.getMyState()== Hexagon.state.BLANK){
                        c++;
                    }
                    if (c>3){
                        break;
                    }
                }
                //Goes in here when it should not.
                if (c>3){
                    if (!nodes.get(j).hasChildern()){//
                        if(!generateChildrenFitnessBot(nodes.get(j)))
                            break;
                    }
                }


            }

        }

        for (int k = nodes.size()-1; k >=0 ; k--) { // this adds up the score starting from the bottom and working its way up.
            if (nodes.get(k).hasChildern()){
                nodes.get(k).assignScore(player);
            }
        }
    }

    public boolean generateChildrenRandomly(Node parent){
        Hexagon hex = new Hexagon(0,0,0,0,0);
        Node temp;
        hex.setMyState(Hexagon.state.HOVER);
        ArrayList<Hexagon> listOfPositions = new ArrayList<>();
        Random r = new Random();

        for (int w = 0; w < width; w++) {

            try {

                //TODO check if game is over and if it is check if we lose
                // this selects a random position on the board, we can use a bot just as easily.
                while(hex.getMyState()!= Hexagon.state.BLANK){


                    hex = parent.getField().get(r.nextInt(parent.getField().size())).clone();

                    if (hex.getMyState()== Hexagon.state.BLANK&&!isDuplicateHex(hex,listOfPositions)){
                            listOfPositions.add(hex);}
                    else{
                        hex = new Hexagon(0,0,0,0,0);
                        hex.setMyState(Hexagon.state.HOVER);
                    }
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            temp = new Node(parent, hex.getQ(),hex.getR());
            if (temp.getDepth()>depth)
                return false;



            parent.getChildArray().add(temp);
            nodes.add(temp);
            hex = new Hexagon(0,0,0,0,0);
            hex.setMyState(Hexagon.state.HOVER);
        }
        return true;
    }
    //TODO FIX: THIS METHOD CREATES IDENTICAL NODES FOR DEPTH. BAD. NOT DONE, WORKS, BUT VERY BAD.
    public boolean generateChildrenFitnessBot(Node parent){
        Hexagon hex = new Hexagon(0,0,0,0,0);
        Node temp;
        hex.setMyState(Hexagon.state.HOVER);
        GameScreen.state Phase = GameScreen.state.P1P2;

        if (parent.getPhase()!= GameScreen.state.P1P1&&parent.getPhase()!= GameScreen.state.P1P2) {//DON'T CHANGE PLEASE

            return generateChildrenRandomly(parent);
        }

        switch (parent.getPhase()){
            case P1P1:
                break;
            case P1P2: Phase = GameScreen.state.P2P1;break;
            case P2P1: Phase = GameScreen.state.P2P2;break;
            case P2P2: Phase = GameScreen.state.P1P1;break;
        }

        for (int w = 0; w < width; w++) {

            try {
                //TODO check if game is over and if it is check if we lose
                // this selects a random position on the board, we can use a bot just as easily.
                hex = parent.getField().get(fgb.calculateTree(parent.getField(),Phase)).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            temp = new Node(parent, hex.getQ(),hex.getR());
            if (temp.getDepth()>depth)
                return false;

            parent.getChildArray().add(temp);
            nodes.add(temp);
            hex = new Hexagon(0,0,0,0,0);
            hex.setMyState(Hexagon.state.HOVER);
        }
        return true;
    }

    
    public boolean isDuplicateHex(Hexagon hex, ArrayList<Hexagon> listofHex){
        for (Hexagon h:listofHex){
            if (hex.getQ()== h.getQ()&& hex.getR() == h.getR()){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public String displayTree(boolean fullTree){
        StringBuilder out = new StringBuilder();
        if (fullTree){
            for (Node n:nodes) {
                out.append(n).append("\n\n");
                if(!n.getChildArray().isEmpty()){
                    for (Node no: n.getChildArray()) {
                        out.append("\t Child: ").append(no.toString()).append("\n");
                    }
                    out.append("\n");
                }
                else {
                    out.append("\t no childern \n");
                    out.append("\n");
                }
            }
        }
        out.append("\n Total nodes generated at depth ").append(depth).append(" and width ").append(width).append(": ").append(nodes.size());
        return out.toString();
    }

    public int[] getBestMove(){
        int bestQ = 0;
        int bestR = 0;
        double maxScore = 0;
        for (Node n0: nodes.get(0).getChildArray()) {
            if(maxScore<n0.getCombinedScore()){
                maxScore = n0.getCombinedScore();
                bestQ = n0.getQ();
                bestR = n0.getR();
            }
        }

        return new int[]{bestQ, bestR};
    }

    //public HashMap<String, Hexagon> getMap(){
    //    return this.hexagonMap;
    //}


    public double simulate(Node node){ // Take the field of the node, simulate games and take the winrate
        ArrayList<Hexagon> clone = new ArrayList<>(); 
        try{
            for(Hexagon hex:node.getField()){
                clone.add(hex.clone());
            }
        }
        catch(Exception e){}
        ArrayList<Hexagon> field = clone;
        rundev dev = new rundev();
        dev.init();
        dev.setField(field);
        dev.update();
        double winrate = dev.getWinRate();
        node.setScore(winrate);
        System.out.println(winrate + " was the winrate");
        return winrate;
        
    }

    public String calculateKey(int q, int r){ // using signed Cantor mapping function
        double x = q;
        double y = r;
        if(x >= 0){
            x = 2*x;
        }
        else{
            x = (-2*x)-1;
        }
        if(y >= 0){
            y = 2*y;
        }
        else{
            y = (-2*y)-1;
        }
        return String.valueOf((0.5 * (x + y) * (x + y + 1)) + y);
    }

    public Node getNextNode(){ // Select the node to be simulated with UCT selection
        double MaxUCT = 0;
        Node toReturn = null;
        for(Node n:this.getNodes()){
            if(n.getUctValue()>MaxUCT){
                MaxUCT = n.getUctValue();
                toReturn = n;
            }
        }
        return toReturn;
    }

    public void expand(Node node){ // add children of the expanded node to the tree
        node.createChildren();
        for(Node n:node.getChildArray()){
            this.nodes.add(n);
            if(n.getDepth()==1){
                this.firstLayerNodes.add(n);
            }
        }
    }

    public void backpropogate(Node node){ // update the scores and increment the visit counters
        Node currentNode = node;
        while(currentNode.getDepth()>0){
            currentNode.incrementVisitScore();
            currentNode.setCombinedScore(currentNode.getScore());
            currentNode.getParent().setCombinedScore(currentNode.getScore() + currentNode.getParent().getScore());
            currentNode = currentNode.getParent();
        }
        if(currentNode.getDepth()==0){
            currentNode.incrementVisitScore();
            currentNode.assignCombinedScore();
        }
    }

    public Node getNextBestMove(Node node){ // backtrack to layer one and
        double bestScore = 0;
        Node toReturn = node;
        for(Node n:this.firstLayerNodes){
            if(n.getCombinedScore()>bestScore){
                bestScore = n.getCombinedScore();
                toReturn = n;
            }
        }
        return toReturn;
    }

    public Node search(Node root){ // Returns a nodde at depth 1 to be played
        Node currentNode = root;
        Node bestNode = root;
        int cntr = 0;
        int current_depth = 0;
        root.createChildren();
        this.expand(root);
        this.simulate(root);
        this.backpropogate(root);
        System.out.println(root.toString());
        while(cntr < searchBias*10){            // TODO: currently using a cntr, but should be a timer instead
            currentNode = this.getNextNode(); 
            System.out.println(currentNode.toString());
            current_depth = currentNode.getDepth();
            this.expand(currentNode);
            this.simulate(currentNode);
            this.backpropogate(currentNode);
            if(currentNode.getScore()>bestNode.getScore()){
                bestNode = currentNode;
            }
            cntr++;
        }
        System.out.println("The best node at the end: \n" + bestNode.toString());
        return this.getNextBestMove(bestNode);
    }

    public static void main(String[] args) {
        rundev dev = new rundev();
        dev.init();
        ArrayList<Hexagon> field = dev.getField();
        for(Hexagon hex:field){
            if(hex.getQ()==0 && hex.getR()==1){
                hex.setMyState(Hexagon.state.BLUE);
            }
        }
        for(Hexagon hex:field){
            if(hex.getQ()==1 && hex.getR()==2){
                hex.setMyState(Hexagon.state.BLUE);
            }
        }
    
        for(Hexagon hex:field){
            if(hex.getQ()==-3 && hex.getR()==0){
                hex.setMyState(Hexagon.state.BLUE);
            }
        }
        MonteCarloTree mct = new MonteCarloTree(10, 20);
        Node root = new Node(field, GameScreen.state.P1P2);
        mct.getNodes().add(root);
        //System.out.println("Size: " + field.size());
        mct.search(new Node(field,GameScreen.state.P2P1));

    }
}

