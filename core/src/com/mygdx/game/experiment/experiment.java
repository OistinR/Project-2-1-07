package com.mygdx.game.experiment;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class experiment {

    File file;
    BufferedReader br;
    String datainString;

    // default file path:
    // C:/Users/Fred/Documents/GitHub/Project-2-1-07/core/src/com/mygdx/game/experiment

    public void writeDatatoCSV(String filepath, ArrayList<Float> data) throws IOException {

        /*
         * Structure goes as follows (for the CSV file), first number is for BOT1,
         * second number is for BOT2
         */

        try (PrintWriter writeToFile = new PrintWriter(filepath);) {

            for (int i = 0; i < data.size(); i++) {
                if (i < data.size() - 1) {
                    writeToFile.write(data.get(i) + ", ");
                } else
                    writeToFile.write(data.get(i) + "");

            }

            /*
             * Which agent offers the best performance based on the heuristic value of his
             * wins and losses?
             */

            /*
             * which agents offer the best performance, the one based on the fitness score
             * such as the rule based bot, or the ones based on the game score such as the
             * Maxn-Paranoid bot and the One Look Ahead Bot ?
             */

            /*
             * israndomness in game trees, thus a monte-carlo simulation a significant
             * improvement for the performances of our bots ?
             */

            /*
             * test: example
             * writeToFile.write("283,");
             * writeToFile.write("69,");
             * writeToFile.write("420");
             * make sure the last number doesnt have a comma or else there will always be a
             * zero at the end
             */

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // actually we really dont need this method since we can read the file directly
    // from MATLAB
    /*
     * 
     * public void readDataFromCSV(String filepath) throws IOException {
     * try {
     * br = new BufferedReader(new FileReader(filepath));
     * String line = "";
     * 
     * while ((line = br.readLine()) != null) {
     * System.out.println(line);
     * }
     * br.close();
     * } catch (Exception e) {
     * // TODO: handle exception
     * }
     * finally{
     * br.close();
     * }
     * }
     */
}
