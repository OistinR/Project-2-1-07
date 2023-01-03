package com.mygdx.game.bots.MCST;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.gametreemc.Node;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node_MCST {

    protected ArrayList<Hexagon> boardState;
    protected Node_MCST parent;
    protected List<Integer> moves;
    protected int winCount;
    protected int visitCount;
    protected List<Node_MCST> children;
    protected GameScreen.state phase;

    Node_MCST(ArrayList<Hexagon> field, List<Integer> moves, GameScreen.state phase){
            this.boardState = field;
            this.parent = null;
            this.moves = moves;
            winCount = 0;
            this.phase = phase;

            visitCount = 0;
            children = null;
    }

    public boolean isLeaf(){
        return children == null;
    }
    public boolean isTerminal(List<Integer> move){
        return move.size() < 4;
    }
}
