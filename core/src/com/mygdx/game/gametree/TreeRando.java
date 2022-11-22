package com.mygdx.game.gametree;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;
import java.util.Random;

//TODO add method for getting best move, we would return Q R and color.

public class TreeRando {
    ArrayList<Node> nodes;
    int depth, width;
    ArrayList<Integer> Layers;

    public TreeRando(int depth, int width){
        nodes = new ArrayList<>();
        this.depth=depth;
        this.width=width;
        Layers = new ArrayList<>();
        for (int i = 0; i < depth+1; i++) {
           Layers.add(i*width);
        }
    }
//boolean player is not needed we can obtain same info from board state to lazy to make switch statment rn
    public void generateTree(ArrayList<Hexagon> field, GameScreen.state boardState, boolean player){
        nodes.clear();
        nodes.add(new Node(field, boardState));

        if(depth >0) {
            generateChildernRandomly(nodes.get(0));
        }

        if (depth>1) {
            for (int j = 0; j < nodes.size(); j++) {//do not use enhanced for loop.
                if (!nodes.get(j).hasChildern()){
                    if(!generateChildernRandomly(nodes.get(j)))
                        break;
                }
            }
        }

        for (int k = nodes.size()-1; k >=0 ; k--) { // this adds up the score starting from the bottom and working its way up.
            if (nodes.get(k).hasChildern()){
                nodes.get(k).assignScore(player);
            }
        }
    }

    /** Helper method for node generation. not done not modular
     * converts the depth to phase
     * 0 = P1P1
     * etc TODO Finish this description
     *
     * @param currentDepth current depth of node
     * @return
     */
    //TODO make dynamic so 0 = current state and so on.
    public GameScreen.state depthToPhase(int currentDepth){
        int t = 0;
        for (int i = 0; i < currentDepth; i++) {
            t++;
            if (t==4){
                t=0;
            }
        }

        GameScreen.state currentPhase = GameScreen.state.P1P1;
        switch (t){
            case 0:
                break;
            case 1: currentPhase = GameScreen.state.P1P2; break;
            case 2: currentPhase = GameScreen.state.P2P1; break;
            case 3: currentPhase = GameScreen.state.P2P2; break;
        }
        return currentPhase;
    }

    //TODO apply a value to each node based on state and who is playing(WIP)
    public boolean generateChildernRandomly(Node parent){
        Hexagon hex = new Hexagon(0,0,0,0,0);
        Node temp = new Node(nodes.get(0),0,0);
        hex.setMyState(Hexagon.state.HOVER);
        ArrayList<Hexagon> listOfPositions = new ArrayList<>();
        Random r = new Random();

        for (int w = 0; w < width; w++) {
            try {
                //TODO check if game is over and if it is check if we lose
                //TODO this selects a random position on the board, we can use a bot just as easily.

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
                out.append(n +"\n\n");
                if(!n.getChildArray().isEmpty()){
                    for (Node no: n.getChildArray()) {
                        out.append("\t Child: "+no.toString()+"\n");
                    }
                    out.append("\n");
                }
                else {
                    out.append("\t no childern \n");
                    out.append("\n");
                }
            }
        }
        out.append("\n Total nodes generated at depth "+depth+" and width "+width+": "+nodes.size());
        return out.toString();
    }

}
