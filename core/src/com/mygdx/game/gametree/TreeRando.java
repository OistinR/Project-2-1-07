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
            generateChildernRandomly(nodes.get(0), 0);
        }
        if (depth>1) {
            for (int j = 0; j < nodes.size(); j++) {
                if (!nodes.get(j).hasChildern()){
                    if(!generateChildernRandomly(nodes.get(j), 0))
                        break;
                }
            }
        }
    }
    //TODO get states working in nodes, apply a value to each node based on state and who is playing, use combined score to get combined score of nodes.
    //TODO Remove duplicate siblings
    public boolean generateChildernRandomly(Node parent, int Phase){

        Random r = new Random();
        Hexagon hex = new Hexagon(0,0,0,0,0);
        //amount of times its generated.
        Node temp = new Node(nodes.get(0),0,0,null,null);
        for (int w = 0; w < width; w++) {

            try {
                hex = parent.getField().get(r.nextInt(parent.getField().size())).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            GameScreen.state currentPhase = GameScreen.state.P1P1;
            Hexagon.state hexState = Hexagon.state.RED;
            switch (Phase){
                case 0:
                    break;
                case 1: currentPhase = GameScreen.state.P1P2; hexState = Hexagon.state.BLUE;break;
                case 2: currentPhase = GameScreen.state.P2P1; break;
                case 3: currentPhase = GameScreen.state.P2P2; hexState = Hexagon.state.BLUE;break;
            }

            temp = new Node(parent, hex.getQ(),hex.getR(), hexState,currentPhase);

            if (temp.getDepth()>depth)
                return false;
            parent.getChildArray().add(temp);
            nodes.add(temp);
        }
        return true;
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
