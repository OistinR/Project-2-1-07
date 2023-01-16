package com.mygdx.game.experiment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StateWrite {
    File fileWrite;

    public StateWrite(){
        fileWrite = new File("core\\src\\com\\mygdx\\trainingData.csv");
    }
    //TODO:
    /** parse second half of data to remove duplicates
     *  merge mcts
     *  collect data.
     */

    ArrayList<ArrayList<ArrayList<Double>>> preMoveStates = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<Double>>> postMoveStates = new ArrayList<>();
    ArrayList<ArrayList<Double>> gamePreMoveStates = new ArrayList<>();
    ArrayList<ArrayList<Double>> gamePostMoveStates = new ArrayList<>();
    ArrayList<Double> Temp = new ArrayList<>();
    ArrayList<ArrayList<Double>> TempPreGameStates = new ArrayList<>();
    ArrayList<ArrayList<Double>> TempPostGameStates = new ArrayList<>();

    public void readFrom(){

            try{
            Scanner myReader = new Scanner(fileWrite);
            boolean Post = true;
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] game = line.split(",");
                for (int i = 0; i < game.length; i++) {
                    if(game[i].equals("999.0")&&Post){
                        if(!Temp.isEmpty())
                            gamePostMoveStates.add(new ArrayList<>(Temp));
                        Temp.clear();
                        Post = false;
                        continue;
                    }
                    if(game[i].equals("999.0")&&!Post){
                        if(!Temp.isEmpty())
                            gamePreMoveStates.add(new ArrayList<>(Temp));
                        Temp.clear();
                        Post= true;
                        continue;
                    }

                    Temp.add(Double.parseDouble(game[i]));
                }

                preMoveStates.add(new ArrayList<>(gamePreMoveStates));
                postMoveStates.add(new ArrayList<>(gamePostMoveStates));

                gamePostMoveStates.clear();
                gamePreMoveStates.clear();
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        for (int i = 0; i < preMoveStates.size(); i++) {
            for (int j = 0; j < preMoveStates.get(i).size(); j++) {
                duplicateRemover(preMoveStates.get(i).get(j), postMoveStates.get(i).get(j));
                System.out.println(preMoveStates.get(i).get(j) +"||"+ postMoveStates.get(i).get(j));
            }
        }



        }

        private void duplicateRemover(ArrayList<Double> before, ArrayList<Double> after ){
            if(before.size()!= after.size()){
                throw new RuntimeException("length of arraylists is not the equal");
            }
            for (int i = 0; i < after.size(); i++) {
                if(after.get(i).equals(before.get(i))){
                    after.set(i, 0.0);
                }
            }
        }
}
