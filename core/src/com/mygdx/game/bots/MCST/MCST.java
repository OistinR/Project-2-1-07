package com.mygdx.game.bots.MCST;

import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.rundev;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCST {
    private static final boolean DEBUG = true;

    // Plays a single game of board game and returns the winner (1 for first player, -1 for second player)
    public int playGame(ArrayList<Hexagon> field, List<Integer>moves) {
        ScoringEngine SEngine = new ScoringEngine();
        System.out.println(moves.size());

        ArrayList<Hexagon> clone_field = new ArrayList<Hexagon>(field);
        List<Integer>clone_moves = new ArrayList<Integer>(moves);

        while (clone_moves.size() >= 4) {
            playRandom(clone_field,clone_moves);
        }
        SEngine.calculate(clone_field);
        int p1score=SEngine.getRedScore();
        int p2score=SEngine.getBlueScore();
        if(p1score<p2score){
            return 1;
        }
        else{
            return -1;
        }
    }

    public void playRandom(ArrayList<Hexagon> clone_field, List<Integer>clone_moves){
        Random r = new Random();
        int rnum = r.nextInt(clone_moves.size());
        clone_field.get(rnum).setMyState(Hexagon.state.RED);
        clone_moves.remove(rnum);

        rnum = r.nextInt(clone_moves.size());
        clone_field.get(rnum).setMyState(Hexagon.state.BLUE);
        clone_moves.remove(rnum);
    }

    // Find an available hexagon
    public List<Integer> available_moves(ArrayList<Hexagon> field){
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            if(field.get(i).getMyState()==Hexagon.state.BLANK){
                moves.add(i);
            }
        }
        return moves;
    }

    // Runs the MCTS algorithm for a fixed number of iterations and returns the best move
    public Node_MCST runMCST(ArrayList<Hexagon> field) {
        /*
        TODO find a way if it's better to use numIterations or to make it run for a certain amount of time
         */
        int numIterations = 5000;
        List<Integer> moves = available_moves(field);
        //here I assume the root node is always P1P1, we can change it when we call the method with different moves
        Node_MCST rootNode = new Node_MCST(field, moves,-1, GameScreen.state.P1P1);


        for (int i = 0; i < numIterations; i++) {
            // Selection step: starting from the root node, traverse the tree using the UCB1 formula until a leaf node is reached
            System.out.println("number of iterations : " + i);
            Node_MCST currentNode = rootNode;
            while (!currentNode.isLeaf()) {
                currentNode = selectChild(currentNode);

            }


            // Expansion step: if the leaf node is not a terminal node, create child nodes for all possible moves and choose one at random
            if (!currentNode.isTerminal(currentNode.moves)) {

                currentNode = expandNode(currentNode);
            }


            // Simulation step: play a game starting from the chosen node and determine the winner
            int winner = playGame(currentNode.boardState,currentNode.moves);


            // Backpropagation step: update the win counts and visit counts of all nodes on the path from the leaf to the root
            while (currentNode != null) {
                currentNode.visitCount++;
                currentNode.winCount += winner;
                currentNode = currentNode.parent;
            }
        }

        // Return the move corresponding to the child node with the highest win rate

        return selectBestChild(rootNode);
    }

    public Node_MCST selectChild(Node_MCST currentNode) {
        Node_MCST selectedChild = null;
        double bestUCB1 = Double.NEGATIVE_INFINITY;

        for (Node_MCST child : currentNode.children) {

            double ucb1 = calcUCB1(child);
            if (ucb1 > bestUCB1) {

                bestUCB1 = ucb1;
                selectedChild = child;
            }
        }
        return selectedChild;
    }
    double calcUCB1(Node_MCST node) {
        // Calculate the exploitation term
        double exploitation = (double) node.winCount / node.visitCount;

        // Calculate the exploration term
        double exploration = Math.sqrt(2 * Math.log(node.parent.visitCount) / node.visitCount);

        // Return the UCB1 score
        return exploitation + exploration;
    }

    Node_MCST expandNode(Node_MCST currentNode) {
        // Generate a list of all possible moves
        List<Integer> moves = currentNode.moves;
        System.out.println("different moves " + moves);
        GameScreen.state child_phase;
        switch (currentNode.phase){
            case P1P1: child_phase = GameScreen.state.P1P2;break;
            case P1P2: child_phase = GameScreen.state.P2P1;break;
            case P2P1: child_phase = GameScreen.state.P2P2;break;
            case P2P2: child_phase = GameScreen.state.P1P1;break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentNode.phase);
        }

        // Create a child node for each move
        for (Integer move_played : moves) {

            Node_MCST child = new Node_MCST(currentNode.boardState,moves,move_played,child_phase);

            /*
            TODO test to see if it works
             */
            child.boardState = new ArrayList<Hexagon>(currentNode.boardState);
            if(child_phase==GameScreen.state.P1P1 || child_phase==GameScreen.state.P1P2)
                child.boardState.get(move_played).setMyState(Hexagon.state.RED);
            else if(child_phase==GameScreen.state.P2P1 || child_phase==GameScreen.state.P2P2){
                child.boardState.get(move_played).setMyState(Hexagon.state.BLUE);
            }
            else{
                throw new IllegalStateException("The children phase is not assign correctly: ");
            }

            child.moves = new ArrayList<Integer>(moves);
            child.moves.remove(move_played);

            child.parent = currentNode;
            currentNode.children.add(child);
        }

        // Choose a random child node to return
        Random rng = new Random();
        int idx = rng.nextInt(currentNode.children.size());
        return currentNode.children.get(idx);
    }

    Node_MCST selectBestChild(Node_MCST currentNode) {
        Node_MCST bestChild = null;
        double bestWinRate = Double.NEGATIVE_INFINITY;
        for (Node_MCST child : currentNode.children) {
            double winRate = (double) child.winCount / child.visitCount;
            if (winRate > bestWinRate) {
                bestWinRate = winRate;
                bestChild = child;
            }
        }
        return bestChild;
    }
    public static void main(String[] args) {


        MCST mcst = new MCST();
        ArrayList<Hexagon> field = mcst.createHexagonFieldDefault();

        Node_MCST bestMove = mcst.runMCST(field);
        System.out.println("the best move " + bestMove.move_played);

        if(bestMove.phase==GameScreen.state.P1P1 || bestMove.phase==GameScreen.state.P1P2)
            field.get(bestMove.move_played).setMyState(Hexagon.state.RED);
        else if(bestMove.phase==GameScreen.state.P2P1 || bestMove.phase==GameScreen.state.P2P2){
            field.get(bestMove.move_played).setMyState(Hexagon.state.BLUE);
        }
        else{
            throw new IllegalStateException("The children phase is not assign correctly: ");
        }


    }
    public ArrayList<Hexagon> createHexagonFieldDefault() {
        int s;
        int fieldsize = 3;
        ArrayList<Hexagon> field = new ArrayList<>();
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50,0,0));
                }
            }
        }
        return field;
    }



}
