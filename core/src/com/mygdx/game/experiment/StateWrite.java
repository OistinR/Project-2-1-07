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

    ArrayList<ArrayList<ArrayList<Double>>> preMoveStates = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<Double>>> postMoveStates = new ArrayList<>();
    ArrayList<ArrayList<Double>> gamePreMoveStates = new ArrayList<>();
    ArrayList<ArrayList<Double>> gamePostMoveStates = new ArrayList<>();
    ArrayList<Double> Temp = new ArrayList<>();
    public String readFrom(){

        try{
        Scanner myReader = new Scanner(fileWrite);
        boolean Post = true;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] game = line.split(",");
            for (int i = 0; i < game.length; i++) {

                if(game[i].equals("999.0")&&Post){
                    gamePostMoveStates.add(Temp);

                    Temp.clear();
                    Post = false;
                    continue;
                }
                else if(game[i].equals("999.0")&&!Post){
                    gamePreMoveStates.add(Temp);
//                    System.out.println(Temp);
                    Temp.clear();
                    Post= true;
                    continue;
                }

                if (!Post)
                    Temp.add(Double.parseDouble(game[i]));
                else Temp.add(Double.parseDouble(game[i]));
            }
            preMoveStates.add(gamePreMoveStates);
            postMoveStates.add(gamePostMoveStates);

            System.out.println(gamePreMoveStates);
//
//            System.out.println(gamePostMoveStates);
            gamePostMoveStates.clear();
            gamePreMoveStates.clear();
        }
        myReader.close();
    } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
        return "null";
    }



//    public String readFrom(){
//        StringBuilder out = new StringBuilder();
//        try{
//            Scanner myReader = new Scanner(fileWrite);
//            while (myReader.hasNextLine()) {
//                out.append(myReader.nextLine());
//                out.append("\n");
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return out.toString();
//    }
}
