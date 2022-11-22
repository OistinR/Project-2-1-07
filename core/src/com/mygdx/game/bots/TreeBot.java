package com.mygdx.game.bots;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.gametree.Tree;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;

public class TreeBot{
    Tree myTree;


    public TreeBot(int depth, int width){
        myTree = new Tree(depth,width);
    }

    public void calculate(ArrayList<Hexagon> field, GameScreen.state bState) {
        myTree.generateTree(field, bState, bState== GameScreen.state.P1P1 ||bState== GameScreen.state.P1P2 );
        int[] QR = myTree.getBestMove();

        for (Hexagon h :
             field) {
            if (h.getQ()==QR[0]&&h.getR()==QR[1]){
             h.setMyState(myTree.getNodes().get(1).getState());
            }
        }

        switch (bState){
            case P1P1: bState = GameScreen.state.P1P2;break;
            case P1P2: bState = GameScreen.state.P2P1;break;
            case P2P1: bState = GameScreen.state.P2P2;break;
            case P2P2: bState = GameScreen.state.P1P1;break;
        }
        //breaking here

        myTree.generateTree(field, bState, bState== GameScreen.state.P1P1 ||bState== GameScreen.state.P1P2 );

        QR = myTree.getBestMove();
        for (Hexagon h :
                field) {
            if (h.getQ()==QR[0]&&h.getR()==QR[1]){
                h.setMyState(myTree.getNodes().get(1).getState());
            }
        }

    }
}
