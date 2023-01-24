package com.mygdx.ann;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.mygdx.ann.layers.HidLayer;
import com.mygdx.ann.layers.OutLayer;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.bots.MCST.MCST;
import com.mygdx.game.bots.MCST.Node_MCST;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;
import com.mygdx.game.screens.GameScreen;


public class DQN {

    private ANN TARGETNET;
    private ANN MAINNET;

    private final double DISCOUNT = 0.99;

    private ScoringEngine SE;

    private ArrayList<Hexagon> state;

    private ArrayList<Double> data;
    private double MEAN;
    private double SD;

    private double EPSILON = 0.1;
    private final int trainamount=999999999;
    private final int summary = 100;

    private MCST botMCST;
    private Bot bot;

    private int boardsize;
    private int writeround;

    /**
     * Constructor for the DQN, sets all fields and instantiates the ANNs
     * @param writeround used for summarizing the progress after summary amount of games
     */
    public DQN(int writeround) {

        this.writeround = writeround;

        state = createState();
        boardsize = state.size();

        MAINNET = new ANN(boardsize+2, boardsize, 1, 128);
        TARGETNET = new ANN(boardsize+2, boardsize,1, 128);

        SE = new ScoringEngine();

        botMCST = new MCST();
        bot = new RandomBot();

        data = new ArrayList<>();

    }

    /**
     * Instantiates everything needed to move and performs a move for both colors
     * @param field is the board
     * @param round is the current round
     */
    public void execMove(ArrayList<Hexagon> field, int round) {
        ANN NeuralNet = new ANN(boardsize+2, boardsize, 1, 128);
        NeuralNet.init();
        NeuralNet.getWBFromCSV();

        ArrayList<Double> inputmove1;
        ArrayList<Double> inputmove2;

        ArrayList<Double> ymove1;
        ArrayList<Double> ymove2;

        int Qmove1;
        int Qmove2;

        inputmove1 = getInputfromState(field, Hexagon.state.RED,round);
        ymove1 = NeuralNet.execFP(inputmove1);
        Qmove1 = getLegalQmax(field,ymove1);
        field.get(Qmove1).setMyState(Hexagon.state.RED);

        inputmove2 = getInputfromState(field, Hexagon.state.BLUE,round);
        ymove2 = NeuralNet.execFP(inputmove2);
        Qmove2 = getLegalQmax(field,ymove2);
        field.get(Qmove2).setMyState(Hexagon.state.BLUE);
    }

    /**
     * Makes the ANN run through games, update the weights according to the Bellman-Equation.
     * It sets all Q values and performs Deep Q-Learning
     */
    public void learn() {

        MAINNET.init();
        TARGETNET.init();

        //MAINNET.getWBFromCSV();
        //TARGETNET.getWBFromCSV();

        int game = 1;
        int annwon=0;
        int mcstwon=0;

        int tmp=0;
        int test2=0;
        int round=0;
        
        double gameaverage=0;
        double summaryaverage=0;

        double topwp = 10;

        int mcstit = 10;
        double winperc = 0;


        
        while(game<trainamount) {
            while(numHexLeft(state)>=4) {
                round++;
                //bot.execMove(state);
                MCSTmove(GameScreen.state.P1P1, true, state, mcstit);
                MCSTmove(GameScreen.state.P1P2, true, state, mcstit);
                tmp = state.size()-numHexLeft(state);
                Episode(Hexagon.state.RED, round);
                Episode(Hexagon.state.BLUE, round);
                test2 = state.size()-numHexLeft(state) - tmp;
                gameaverage = gameaverage+test2;
            }
            gameaverage = gameaverage/round;
            summaryaverage = summaryaverage+gameaverage;

            SE.calculate(state);
            if(SE.getBlueScore()>SE.getRedScore()) {
                annwon++;
            } else mcstwon++;

            if(game%10==0) {
                TARGETNET.copyWB(MAINNET);
            }

            if(game%writeround==0) {
                File LL = new File("core\\src\\com\\mygdx\\ann\\data\\LearnLog.txt");

                int num = game-writeround;
                System.out.println("Summary of game... "+num+" - "+game);
                try {
                    FileWriter fileWriter = new FileWriter(LL,true);
                    StringBuilder line = new StringBuilder();
                    line.append("Summary of game... "+num+" - "+game);   
                    line.append("\n");
                    fileWriter.write(line.toString());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(annwon==0) {
                    num = game-summary;
                    System.out.println("Win percentage BOB (Double DQN): 0.0%");
                    try {
                        
                        FileWriter fileWriter = new FileWriter(LL,true);
                        StringBuilder line = new StringBuilder();
                        line.append("Win percentage BOB (Double DQN): 0.0%");   
                        line.append("\n");
                        fileWriter.write(line.toString());
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    num = game-summary;
                    winperc =((double)annwon / ((double)annwon + (double)mcstwon)) *100;
                    System.out.println("Win percentage BOB (Double DQN): "+winperc +"%");
                    try {
                        FileWriter fileWriter = new FileWriter(LL,true);
                        StringBuilder line = new StringBuilder();
                        line.append("Win percentage BOB (Double DQN): "+winperc +"%");   
                        line.append("\n");
                        fileWriter.write(line.toString());
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(winperc>topwp) {
                        topwp = winperc;
                        System.out.println("Writing to .csv ...");
                        writeBWCSV(MAINNET.HLAYERS, MAINNET.OLAYER);
                    }
                    if(winperc>=50.0) {
                        mcstit = mcstit + 10;
                        int temp = mcstit-10;
                        System.out.println("MCSTS has been beaten, its iterations has ben increased from... "+temp+" to... "+mcstit);
                        try {
                            FileWriter fileWriter = new FileWriter(LL,true);
                            StringBuilder line = new StringBuilder();
                            line.append("MCSTS has been beaten, its iterations has ben increased from... "+temp+" to... "+mcstit);   
                            line.append("\n");
                            fileWriter.write(line.toString());
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        topwp=10;
                    }

                }

                double a = summaryaverage/writeround;
                System.out.println("Average legal moves played... "+a);
                try {
                    FileWriter fileWriter = new FileWriter(LL,true);
                    StringBuilder line = new StringBuilder();
                    line.append("Average legal moves played... "+a);   
                    line.append("\n");
                    fileWriter.write(line.toString());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<Hexagon> teststate = createState();
                teststate.get(1).setMyState(Hexagon.state.RED); teststate.get(4).setMyState(Hexagon.state.BLUE);
                teststate.get(2).setMyState(Hexagon.state.RED); teststate.get(7).setMyState(Hexagon.state.BLUE);
                ArrayList<Double> testlabels = createLabels2(teststate);
                ArrayList<Double> fwp = MAINNET.execFP(testlabels);
                double error = MAINNET.computeError(fwp, testlabels).get(MAINNET.computeError(fwp, testlabels).size()-1);
                System.out.println("Error of ANN: "+error);

                try {
                    FileWriter fileWriter = new FileWriter(LL,true);
                    StringBuilder line = new StringBuilder();
                    line.append("Error of ANN: "+error);   
                    line.append("\n");
                    fileWriter.write(line.toString());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Playing against MCST with "+mcstit+" iterations.");

                try {
                    FileWriter fileWriter = new FileWriter(LL,true);
                    StringBuilder line = new StringBuilder();
                    line.append("Playing versus MCST running "+mcstit+" iterations");   
                    line.append("\n");
                    line.append(" ");
                    line.append("\n");
                    fileWriter.write(line.toString());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                annwon=0;
                mcstwon=0;
                summaryaverage=0;
                System.out.println(" ");
                if(winperc>=80) {
                    EPSILON = 0;
                    if(winperc>=99) {
                        //game=trainamount;
                    }
                }
                datawriter(winperc, game);
            }
    
            gameaverage=0;
            round=0;
            state = createState();
            SE = new ScoringEngine();

            game++;
        }

    }

    /**
     * Computes the mean and standard deviation of the rewards, used for testing
     */
    public void StandardMD() {
        double mean=0;
        for(int i=0; i<data.size(); i++) {
            mean=mean+data.get(i);
        }
        MEAN=mean/data.size();

        double sd=0;
        for(int i=0; i<data.size(); i++) {
            sd = sd+(data.get(i)-mean)*(data.get(i)-mean);
        }
        SD=sd/(data.size()-1);

        System.out.println("Mean of rewards: ..."+MEAN+" Standard deviation of rewards: ..."+SD);
    }

    /**
     * Places a colour in the colour passed and calculates the reward for ever Q value
     * @param colour is the colour that should be played next
     * @param round is the current round in the game
     */
    public void Episode(Hexagon.state colour, int round) {
        ArrayList<Double> labels = new ArrayList<>();

        double reward = 0;
        int Qm = 0;
        int Qt = 0;

        while(Qm<state.size()) {
            // ! Copy the state so it will only be used for evaluation
            ArrayList<Hexagon> clonestate = new ArrayList<Hexagon>();
            try {
                for(Hexagon h : state) {
                    clonestate.add(h.clone());
                }
            } catch (Exception e) {}

            // * Play the Q move at the Qm index
            clonestate.get(Qm).setMyState(colour);

            // * Get reward at state st
            SE.calculate(clonestate);
            reward = reward + (SE.getBlueScore() - SE.getRedScore());

            ArrayList<Double> input = new ArrayList<>();
            ArrayList<Double> ytarget = new ArrayList<>();

            int testround = round;

            int count=0;
            if(colour==Hexagon.state.RED) {
                count=1;
            } else if(colour==Hexagon.state.BLUE) {
                count=1;
            }
            for(int i=0; i<count; i++) {
                Hexagon.state loopcolour;
                if(colour==Hexagon.state.RED && i%2==0) {
                    loopcolour = Hexagon.state.BLUE;
                } else if(colour==Hexagon.state.BLUE && i%2!=0) {
                    loopcolour = Hexagon.state.BLUE;
                } else  loopcolour = Hexagon.state.RED;

                // * Perform action at to create state st+1 and evaluate it at the TARGET Network
                input = getInputfromState(clonestate,loopcolour,testround);
                ytarget = TARGETNET.execFP(input);

                // * Get max LEGAL Q value of output of TARGET Network
                Qt = getLegalQmax(clonestate, ytarget);
                clonestate.get(Qt).setMyState(loopcolour);

                // * Get reward at state st+1
                SE.calculate(clonestate);
                reward = (SE.getBlueScore() - SE.getRedScore());

                // * Play bot if in correct state
                if(numHexLeft(clonestate)>=4 && i!=count-1) {
                    if(colour==Hexagon.state.RED && i%2==0) {
                        MCSTmove(GameScreen.state.P1P1, true, clonestate,80);
                        MCSTmove(GameScreen.state.P1P2, true, clonestate,80);     
                        //bot.execMove(clonestate);               
                    } else if(colour==Hexagon.state.BLUE && i%2!=0) {
                        MCSTmove(GameScreen.state.P1P1, true, clonestate,80);
                        MCSTmove(GameScreen.state.P1P2, true, clonestate,80);
                        //bot.execMove(clonestate);
                    }
                }
                testround++;
            }
            // * Compute the loss for the Q(s,a) value chosen
            double loss=0;
            if(reward>0) {
                loss = 1+DISCOUNT*ytarget.get(Qt);
            } else loss = -1+DISCOUNT*ytarget.get(Qt);
            //double loss = (reward+4)/70+DISCOUNT*ytarget.get(Qt);
            labels.add(loss);

            // * Update Qm index and reset reward
            Qm++;
            reward = 0;
        }

        labels = createLabels(state, labels);
        
        // ! Back propegate using the labels created
        ArrayList<Double> input = getInputfromState(state,colour,round);
        ArrayList<Double> ymain = MAINNET.execFP(input);
        MAINNET.execBP(ymain, labels);
                
        // ! Play the move with the highest Q value to progress the game
        ymain = MAINNET.execFP(input);
        int Q = getLegalQmax(state, ymain);
        double U = ThreadLocalRandom.current().nextDouble();

        if(U>EPSILON) {
            Q = getLegalQmax(state,ymain);
        } else {
            Q = getLegalQ(state, ymain).get(ThreadLocalRandom.current().nextInt(0,getLegalQ(state, ymain).size()));
        }

        state.get(Q).setMyState(colour);
        
    }

    /**
     * Creates labels for the Episode method
     * @param field is the board
     * @return returns all of the labels
     */
    public ArrayList<Double> createLabels2(ArrayList<Hexagon> field) {
        ArrayList<Double> toreturn = new ArrayList<>();
        for(int i=0; i<field.size(); i++) {
            toreturn.add(0.0);
        }
        return toreturn;
    }


    /**
     * Creates labels for computing the error
     * @param field is the board
     * @param label
     * @return returns all of the labels
     */
    public ArrayList<Double> createLabels(ArrayList<Hexagon> field, ArrayList<Double> label) {
        for(int i=0; i<field.size(); i++) {
            if(field.get(i).getMyState()==Hexagon.state.BLANK){
                label.set(i, 0.0);
            }
        }
        return label;
    }

    /**
     * Returns q values that belong to blank hexagons, removing the ones where a tile is already placed
     * @param field is the board
     * @param list of all of the hexagons' Q values
     * @return the Q values of nodes that are still blank
     */
    public ArrayList<Integer> getLegalQ(ArrayList<Hexagon> field, ArrayList<Double> list) {
        ArrayList<Integer> a = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            if((field.get(i).getMyState()==Hexagon.state.BLANK)) {
                a.add(i);
            }
        }
        return a;
    }

    /**
     * Returns the Q value that is blank with the highest Q value, to be colored next
     * @param field is the board
     * @param list is the list of all Q values
     * @return the highest Q value for blank tile
     */
    public int getLegalQmax(ArrayList<Hexagon> field, ArrayList<Double> list) {
        double max=-999999999;
        int maxi=0;
        for(int i=0; i<list.size(); i++) {
            if((list.get(i)>max)&&(field.get(i).getMyState()==Hexagon.state.BLANK)) {
                max=list.get(i);
                maxi=i;
            }
        }
        return maxi;
    }

    /**
     * Returns the inputs for the ANN from the current board state based on the color of each hexagon
     * @param statefield is the current state of te board
     * @param col is the color of the hexagon to determine whether it gets 1, 0 or -1
     * @param round is the current round of the game
     * @return the board with inputs 1, 0, -1 depending on the state of each tile
     */
    public ArrayList<Double> getInputfromState(ArrayList<Hexagon> statefield, Hexagon.state col, int round) {
        ArrayList<Double> inutreturn = new ArrayList<>();
        for(int i=0; i<statefield.size(); i++) {
            if(statefield.get(i).getMyState()==Hexagon.state.BLANK) {
                inutreturn.add(0.0);
            } else if(statefield.get(i).getMyState()==Hexagon.state.RED) {
                inutreturn.add(1.0);
            } else if(statefield.get(i).getMyState()==Hexagon.state.BLUE){
                inutreturn.add(-1.0);
            } else System.out.println("An error has occurred when reading the board");
        }


        if(col==Hexagon.state.RED) {
            inutreturn.add(1.0);
        } else if(col==Hexagon.state.BLUE) {
            inutreturn.add(-1.0);
        }

        int maxround = statefield.size()/4;
        double part = 1.0/(double)maxround;
        inutreturn.add(part*round);

        return inutreturn;
    }

    /**
     * Creates a field with empty states
     * @return a field with empty states
     */
    public ArrayList<Hexagon> createState() {
        int s;
        int fieldsize = 3;
        ArrayList<Hexagon> field = new ArrayList<>();
        for (int q = -fieldsize; q <= fieldsize; q++) {
            for (int r = fieldsize; r >= -fieldsize; r--) {
                s = -q - r;
                if (s <= fieldsize && s >= -fieldsize) {
                    field.add(new Hexagon(q, r, 50, 0, 0));
                }
            }
        }
        return field;
    }

    /**
     * Performs a move from the MCST
     * @param STATE is the current state of the game
     * @param player1 determines wether or not the bot is player 1
     * @param field is the current board
     * @param numiteration is the amount of iterations the MCST should perform
     */
    private void MCSTmove(GameScreen.state STATE, boolean player1, ArrayList<Hexagon> field, int numiteration){
        ArrayList<Hexagon> copy_field = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                copy_field.add(h.clone());
            }
        } catch (Exception e) {}

        Node_MCST bestMove = botMCST.runMCST(copy_field,STATE,player1, numiteration);

        if(bestMove.phase==GameScreen.state.P1P1 || bestMove.phase==GameScreen.state.P2P1)
            field.get(bestMove.move_played).setMyState(Hexagon.state.RED);
        else if(bestMove.phase==GameScreen.state.P1P2 || bestMove.phase==GameScreen.state.P2P2){
            field.get(bestMove.move_played).setMyState(Hexagon.state.BLUE);
        }
        else{
            throw new IllegalStateException("The children phase is not assign correctly: ");
        }
    }

    /**
     * Calculates how many blank hexagons are left on the field
     * @param field is the current board
     * @return the amount of blank hexagons
     */
    public int numHexLeft(ArrayList<Hexagon> field) {
        int num=0;
        for(Hexagon h:field) {
            if((h.getMyState()==Hexagon.state.BLANK)) {
                num++;
            }
        }
        return num;
    }

    /**
     * Instantiates the DQN
     * @param args is for potential user inputs while learning
     */
    public static void main(String[] args) {
        DQN dqn = new DQN(50);
        dqn.learn();
    }

    /**
     * Writes all the biases and weights into a CSV for saving an ANN and later use
     * @param hiddenlayers is the list of hidden layers
     * @param outputlayer is the list of output layers
     */
    public void writeBWCSV(ArrayList<HidLayer> hiddenlayers, OutLayer outputlayer) {

        ArrayList<String> writeable = new ArrayList<>();

        for(int i=0; i<hiddenlayers.size(); i++) {
            for(int j=0; j<hiddenlayers.get(i).getNeurons().size(); j++) {
                for(int k=0; k<hiddenlayers.get(i).getNeurons().get(j).getSynapses().size(); k++) {
                    writeable.add(Double.toString(hiddenlayers.get(i).getNeurons().get(j).getSynapses().get(k).getWeight()));
                }
            }
        }
        for(int i=0; i<hiddenlayers.size(); i++) {
            for(int j=0; j<hiddenlayers.get(i).getNeurons().size(); j++) {
                writeable.add(Double.toString(hiddenlayers.get(i).getNeurons().get(j).getBias()));
            }
        }
        for(int i=0; i<outputlayer.getNeurons().size(); i++) {
            for(int j=0; j<outputlayer.getNeurons().get(i).getSynapses().size(); j++) {
                writeable.add(Double.toString(outputlayer.getNeurons().get(i).getSynapses().get(j).getWeight()));
            }
        }
        for(int i=0; i<outputlayer.getNeurons().size(); i++) {
            writeable.add(Double.toString(outputlayer.getNeurons().get(i).getBias()));
        }

        File csvFile = new File("core\\src\\com\\mygdx\\ann\\data\\NNBW.csv");

        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            for (String data : writeable) {
                StringBuilder line = new StringBuilder();
                line.append(data);
                
                line.append("\n");
                fileWriter.write(line.toString());
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the data used for experiments into a CSV file
     * @param winrate is the winrate the past numround of games
     * @param numround is the number of games
     */
    public void datawriter(double winrate, int numround) {
        File LL = new File("core\\src\\com\\mygdx\\ann\\data\\EXP.csv");
        try {
            FileWriter fileWriter = new FileWriter(LL,true);
            StringBuilder line = new StringBuilder();
            line.append(winrate);  
            line.append(",");
            line.append(numround) ;
            line.append("\n");
            fileWriter.write(line.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
