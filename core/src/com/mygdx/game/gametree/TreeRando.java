package com.mygdx.game.gametree;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;
import java.util.Random;

public class TreeRando {
    ArrayList<Node> nodes;
    int depth, width;
    Hexagon.state myColor;
    Hexagon.state oppColor;
    ArrayList<Integer> Layers;


    public TreeRando(int depth, int width,  Hexagon.state myColor, Hexagon.state oppColor){
        nodes = new ArrayList<>();
        this.depth=depth;
        this.width=width;
        this.myColor = myColor;
        this.oppColor = oppColor;
        Layers = new ArrayList<>();
        for (int i = 0; i < depth+1; i++) {
           Layers.add(i*width);
        }
    }

    public void generateTree(ArrayList<Hexagon> field){
        nodes.clear();
        nodes.add(new Node(field, GameScreen.state.P2P1));
        if(depth >0) {
            generateChildernRandomly(nodes.get(0), GameScreen.state.P1P1);
        }

        if (depth>1) {
            for (int j = 0; j < nodes.size(); j++) {//do not use enhanced for loop.
                if (!nodes.get(j).hasChildern()){
                    if(!generateChildernRandomly(nodes.get(j), depthToPhase(nodes.get(j).getDepth())))
                        break;
                }
            }
        }

        for (int k = nodes.size()-1; k >=0 ; k--) { // this added up the score starting from the bottom and working its way up.
            if (nodes.get(k).hasChildern()){
                nodes.get(k).assignScore();
            }
        }
    }

    /** Helper method for node generation.
     * converts the depth to phase
     * 0 = P1P1
     * etc TODO Finish this description
     *
     * @param currentDepth current depth of node
     * @return
     */

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
    public boolean generateChildernRandomly(Node parent, GameScreen.state currentPhase){


        Hexagon hex = new Hexagon(0,0,0,0,0);
        //amount of times its generated.
        Node temp = new Node(nodes.get(0),0,0, GameScreen.state.P1P1);
        hex.setMyState(Hexagon.state.HOVER);
        ArrayList<Hexagon> listOfPositions = new ArrayList<>();
        Random r = new Random();
        int randInt = 0;
        for (int w = 0; w < width; w++) {

            try {
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
            temp = new Node(parent, hex.getQ(),hex.getR(), currentPhase);

            if (temp.getDepth()>depth)
                return false;
            parent.getChildArray().add(temp);
            nodes.add(temp);
            hex = new Hexagon(0,0,0,0,0);
            hex.setMyState(Hexagon.state.HOVER);
        }
        parent.assignScore();
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
