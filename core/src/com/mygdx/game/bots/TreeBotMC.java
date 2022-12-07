package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.gametreemc.MonteCarloTree;
import com.mygdx.game.gametreemc.Node;
import com.mygdx.game.gametreemc.Tree;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;

public class TreeBotMC{
    Tree myTree;


    public TreeBotMC(int depth, int width){
        myTree = new Tree(depth,width);
    }

    public void calculate(ArrayList<Hexagon> field, GameScreen.state bState) {
        int[] QR;
        MonteCarloTree mct = new MonteCarloTree(10, 20);
        Node root = new Node(field, GameScreen.state.P2P1);
        mct.getNodes().add(root);
        Node bestNode = mct.search(root);

        QR = new int[]{bestNode.getQ(), bestNode.getR()};

        for (Hexagon h :
                field) {
            if (h.getQ()==QR[0]&&h.getR()==QR[1]){
                h.setMyState(bestNode.getState());
            }
        }

        mct = new MonteCarloTree(10, 20);
        root = new Node(field, GameScreen.state.P2P2);
        mct.getNodes().add(root);
        bestNode = mct.search(root);

        QR = new int[]{bestNode.getQ(), bestNode.getR()};

        for (Hexagon h :
                field) {
            if (h.getQ()==QR[0]&&h.getR()==QR[1]){
                h.setMyState(bestNode.getState());
            }
        }
    }
}
