package com.mygdx.game.experiment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StateWrite {
    File fileWrite;
    FileWriter writer;
    public StateWrite(){
        fileWrite = new File("core\\src\\com\\mygdx\\trainingData.csv");
        try {
            writer = new FileWriter(fileWrite);
        } catch (IOException e) {
            System.out.println("error with IO: ");
            e.printStackTrace();
        }
    }

    public void writeTo(ArrayList<Double> features) throws IOException {
        writer= new FileWriter(fileWrite);
        StringBuilder sb = new StringBuilder();
        for (Double feature:features) {
            sb.append(feature.toString()+"\n");
        }
        writer.write(sb.toString());
        writer.close();

    }

    public String readFrom(){
        StringBuilder out = new StringBuilder();
        try{
        Scanner myReader = new Scanner(fileWrite);
        while (myReader.hasNextLine()) {
            out.append(myReader.nextLine());
        }
        myReader.close();
    } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
        return out.toString();
    }
}
