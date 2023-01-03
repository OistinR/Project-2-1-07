package com.mygdx.game.bots.MCST;

import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCST {
    // Plays a single game of board game and returns the winner (1 for first player, -1 for second player)

    public int playGame(ArrayList<Hexagon> field, List<Integer>moves) {
        ScoringEngine SEngine = new ScoringEngine();
        RandomBot play_random = new RandomBot();
        while (moves.size() >= 4) {
            play_random.calculate(field);
        }
        SEngine.calculate(field);
        int p1score=SEngine.getRedScore();
        int p2score=SEngine.getBlueScore();
        if(p1score>p2score){
            return 1;
        }
        else{
            return -1;
        }
    }
    // Find an available hexagon
    public List<Integer> available_moves(ArrayList<Hexagon> field){
        List<Integer> moves = null;
        for (int i = 0; i < field.size(); i++) {
            if(field.get(i).getMyState()==Hexagon.state.BLANK){
                moves.add(i);
            }
        }
        return moves;
    }

    // Runs the MCTS algorithm for a fixed number of iterations and returns the best move
    public List<Integer> runMCTS(ArrayList<Hexagon> field) {
        /*
        TODO find a way if it's better to use numIterations or to make it run for a certain amount of time
         */
        int numIterations = 10;
        List<Integer> moves = available_moves(field);
        Node_MCST rootNode = new Node_MCST(field, moves);


        for (int i = 0; i < numIterations; i++) {
            // Selection step: starting from the root node, traverse the tree using the UCB1 formula until a leaf node is reached
            Node_MCST currentNode = rootNode;
            while (!currentNode.isLeaf()) {
                currentNode = selectChild(currentNode);
            }

            // Expansion step: if the leaf node is not a terminal node, create child nodes for all possible moves and choose one at random
            if (!currentNode.isTerminal(moves)) {
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
        return selectBestChild(rootNode).moves;
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
        List<Integer> moves = available_moves(currentNode.boardState);

        // Create a child node for each move
        for (Integer move : moves) {
            Node_MCST child = new Node_MCST(currentNode.boardState,moves);

            /*
            TODO need to implement a way so that the hexagon colour changes with the current player
             */
            child.boardState = new ArrayList<Hexagon>(currentNode.boardState);
            child.boardState.get(move).setMyState(Hexagon.state.RED);

            child.moves = new ArrayList<Integer>(moves);
            child.moves.remove(move);

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

}
