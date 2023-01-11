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
    public boolean player1;

    // Plays a single game of board game and returns the winner (1 for first player, -1 for second player)
    public int playGame(ArrayList<Hexagon> field, List<Integer>moves,GameScreen.state STATE) {
        ScoringEngine SEngine = new ScoringEngine();
        //System.out.println(moves.size());

        ArrayList<Hexagon> clone_field = new ArrayList<Hexagon>();
        List<Integer> clone_moves = new ArrayList<Integer>();

        try {
            for(Hexagon h : field) {
                clone_field.add(h.clone());
            }
        } catch (Exception e) {}

        for (Integer move: moves) {
            clone_moves.add(move);
        }

        //TODO need to check this, accurate enough
        while (clone_moves.size() > field.size()%4) {
            switch (STATE){
                case P1P1: STATE = GameScreen.state.P1P2;break;
                case P1P2: STATE = GameScreen.state.P2P1;break;
                case P2P1: STATE = GameScreen.state.P2P2;break;
                case P2P2: STATE = GameScreen.state.P1P1;break;
                default:
                    throw new IllegalStateException("Unexpected value in the state");
            }
            playRandom(clone_field,clone_moves,STATE);
        }
        SEngine.calculate(clone_field);
        int p1score=SEngine.getRedScore();
        int p2score=SEngine.getBlueScore();
        if(player1){
            if(p1score>p2score){
                return 1;
            }
            else{
                return -1;
            }
        }
        else{ //not player 1
            if(p1score<p2score){
                return 1;
            }
            else{
                return -1;
            }
        }
    }

    public void playRandom(ArrayList<Hexagon> clone_field, List<Integer>clone_moves, GameScreen.state STATE){
        Random r = new Random();
        if(STATE == GameScreen.state.P1P1 || STATE == GameScreen.state.P2P1){
            int rnum = r.nextInt(clone_moves.size());
            clone_field.get(clone_moves.get(rnum)).setMyState(Hexagon.state.RED);
            clone_moves.remove(rnum);
        }
        if(STATE == GameScreen.state.P1P2 || STATE == GameScreen.state.P2P2){
            int rnum = r.nextInt(clone_moves.size());
            clone_field.get(clone_moves.get(rnum)).setMyState(Hexagon.state.BLUE);
            clone_moves.remove(rnum);
        }
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
    public Node_MCST runMCST(ArrayList<Hexagon> field, GameScreen.state STATE, boolean Player1, int round) {
        this.player1 = Player1;
        /*
        TODO find a way if it's better to use numIterations or to make it run for a certain amount of time
         */
        //for this to work the root node needs to be the state before the hexagon we want to place, fastest solution
        switch (STATE){
            case P1P1: STATE = GameScreen.state.P2P2;break;
            case P1P2: STATE = GameScreen.state.P1P1;break;
            case P2P1: STATE = GameScreen.state.P1P2;break;
            case P2P2: STATE = GameScreen.state.P2P1;break;
            default:
                throw new IllegalStateException("Unexpected value of the STATE");
        }

        int numIterations = 2000;
        long start_time = System.nanoTime();
        long end_time = System.nanoTime();

        List<Integer> moves = available_moves(field);
        //here I assume the root node is always P1P1, we can change it when we call the method with different moves
        Node_MCST rootNode = new Node_MCST(field, moves,-1, STATE,round);
        int count=0;
        double convert_sec = 1000000000;
        double maxTime = 1;

        //for (int i = 0; i < numIterations; i++) {
        while((end_time-start_time)/convert_sec<maxTime){

            /*if(count%1000==0)
                System.out.println(count);
            count++;*/
            // Selection step: starting from the root node, traverse the tree using the UCB1 formula until a leaf node is reached
            end_time = System.nanoTime();


            Node_MCST currentNode = rootNode;


            while (!currentNode.isLeaf()) {
                currentNode = selectChild(currentNode);

            }


            // Expansion step: if the leaf node is not a terminal node, create child nodes for all possible moves and choose one at random
            if (!currentNode.isTerminal(currentNode.moves,field)) {
                currentNode = expandNode(currentNode);
                //have a second look at it
            }


            // Simulation step: play a game starting from the chosen node and determine the winner
            int winner = playGame(currentNode.boardState,currentNode.moves,currentNode.phase);


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
        double exploitation = (double) node.winCount / (double) node.visitCount;
        if (node.visitCount == 0) {
            exploitation = Double.POSITIVE_INFINITY;
        }
        //System.out.println("this is exploitation " + exploitation);

        // Calculate the exploration term
        double exploration = Math.sqrt(2 * Math.log(node.parent.visitCount) / node.visitCount);
        if (node.visitCount == 0) {
            exploration = Double.POSITIVE_INFINITY;
        }
        //System.out.println("this is exploration " + exploration);

        // Return the UCB1 score
        return exploitation + exploration;
    }

    Node_MCST expandNode(Node_MCST currentNode) {
        // Generate a list of all possible moves
        List<Integer> movescopy = new ArrayList<>();
        for (Integer move: currentNode.moves  ) {
            movescopy.add(move);
        }
        List<Integer> moves = movescopy;
        int round = currentNode.round;

        //System.out.println("different moves " + moves);
        GameScreen.state child_phase;
        switch (currentNode.phase){
            case P1P1: child_phase = GameScreen.state.P1P2;break;
            case P1P2: child_phase = GameScreen.state.P2P1;break;
            case P2P1: child_phase = GameScreen.state.P2P2;break;
            case P2P2: child_phase = GameScreen.state.P1P1; round++;break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentNode.phase);
        }

        for (Integer move_played : moves) {
            // Create a child node for each move
            ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
            //TODO can this method go outside of the for? to create it only ones?
            try {
                for(Hexagon h : currentNode.boardState) {
                    copy_field.add(h.clone());
                }
            } catch (Exception e) {}

            Node_MCST child = new Node_MCST(copy_field,moves,move_played,child_phase,round);

            //child.boardState = new ArrayList<Hexagon>(currentNode.boardState);
            if(child_phase==GameScreen.state.P1P1 || child_phase==GameScreen.state.P2P1)
                child.boardState.get(move_played).setMyState(Hexagon.state.RED);
            else if(child_phase==GameScreen.state.P1P2 || child_phase==GameScreen.state.P2P2){
                child.boardState.get(move_played).setMyState(Hexagon.state.BLUE);
            }
            else{
                throw new IllegalStateException("The children phase is not assign correctly: ");
            }
            //need to print out all the different moves per child
            child.moves = new ArrayList<Integer>(moves);
            //System.out.println(child.moves);
            child.moves.remove(move_played);

            child.parent = currentNode;
            currentNode.children.add(child);
        }
        //Pie rule for the expand case
        if(round==1 && child_phase == GameScreen.state.P2P1){
            ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
            //TODO can this method go outside of the for? to create it only ones?
            try {
                for(Hexagon h : currentNode.boardState) {
                    if(h.getMyState() == Hexagon.state.BLUE)
                        h.setMyState(Hexagon.state.RED);
                    else if(h.getMyState() == Hexagon.state.RED)
                        h.setMyState(Hexagon.state.BLUE);
                    copy_field.add(h.clone());
                }
            } catch (Exception e) {}

            Node_MCST child = new Node_MCST(copy_field,moves,-2,child_phase,round);
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
        int round=1;
        ArrayList<Hexagon> field = mcst.createHexagonFieldDefault();
        Node_MCST currentNode = new Node_MCST(field, new ArrayList<Integer>(),0, GameScreen.state.P1P1,round);
        System.out.println(currentNode.visitCount);
        System.out.println(currentNode.winCount);
        mcst.calcUCB1(currentNode);

        /** Node_MCST bestMove = mcst.runMCST(field, GameScreen.state.P1P1);
        System.out.println("the best move " + bestMove.move_played);

        if(bestMove.phase==GameScreen.state.P1P1 || bestMove.phase==GameScreen.state.P2P1)
            field.get(bestMove.move_played).setMyState(Hexagon.state.RED);
        else if(bestMove.phase==GameScreen.state.P1P2 || bestMove.phase==GameScreen.state.P2P2){
            field.get(bestMove.move_played).setMyState(Hexagon.state.BLUE);
        }
        else{
            throw new IllegalStateException("The children phase is not assign correctly: ");
        }
         **/


    }
    public ArrayList<Hexagon> createHexagonFieldDefault() {
        int s;
        int fieldsize = 5;
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
