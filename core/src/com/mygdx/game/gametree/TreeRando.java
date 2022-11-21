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

    public TreeRando(int depth, int width,  Hexagon.state myColor, Hexagon.state oppColor){
        nodes = new ArrayList<>();
        this.depth=depth;
        this.width=width;
        this.myColor = myColor;
        this.oppColor = oppColor;
    }

    public void generateTree(ArrayList<Hexagon> field){
        nodes.clear();
        nodes.add(new Node(field, GameScreen.state.P2P1));

        int d = 0;
        int i = 0;

        while(d<=depth) {
            d = generateChildernRandomly(i, d);
            i++;
        }

    }

    public int generateChildernRandomly(int indexOfParent, int Phase){

        Random r = new Random();
        Hexagon hex = new Hexagon(0,0,0,0,0);
        //amount of times its generated.
        Node temp = new Node(nodes.get(0),0,0,null,null);
        for (int w = 0; w < width; w++) {

            try {
                hex = nodes.get(indexOfParent).getField().get(r.nextInt(nodes.get(indexOfParent).getField().size())).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            GameScreen.state currentPhase = GameScreen.state.P1P1;
            Hexagon.state hexState = Hexagon.state.RED;
            switch (Phase){
                case 0:
                    break;
                case 1: currentPhase = GameScreen.state.P1P2; hexState = Hexagon.state.BLUE;break;
                case 2: currentPhase = GameScreen.state.P2P1; hexState = Hexagon.state.RED;break;
                case 3: currentPhase = GameScreen.state.P2P2; hexState = Hexagon.state.BLUE;break;
            }

            temp = new Node(nodes.get(indexOfParent), hex.getQ(),hex.getR(), hexState,currentPhase);
            nodes.get(indexOfParent).getChildArray().add(temp);
            nodes.add(temp);
            }
        return temp.getDepth();
    }

    public String displayTree(){
        StringBuilder out = new StringBuilder();
        for (Node n:nodes) {
            out.append(n.toString()+"\n");
        }
        return out.toString();
    }

}
