package com.mygdx.tensorAnn;
import com.mygdx.game.bots.Bot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.experiment.GameState;
import org.tensorflow.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PredictBot extends Bot {
    SavedModelBundle bestBoy;
    //init model
    public PredictBot(){
        bestBoy = SavedModelBundle.load("core\\src\\com\\mygdx\\tensorAnn\\model47", "serve");
    }


    @Override
    public void execMove(ArrayList<Hexagon> field) {
        runtime=0;
        long startTime = System.nanoTime();
        calculate(field);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        runtime += duration/1000;
    }

    @Override
    public void calculate(ArrayList<Hexagon> field) {
        // gets the current featues from the state and then converts them to a tensor then it gives this tensor to the model
        GameState gs = new GameState();
        gs.update(field);
        Session s = bestBoy.session();
        float[] features = new float[38];
        for (int i = 0; i < 36; i++) {
            features[i]= (float)(double)gs.getState().get(i);
        }

        features[37] = 1.0f;

        FloatBuffer db = FloatBuffer.wrap(features);

        Tensor<Float> input = Tensor.create(new long[]{1,38}, db);
        //retrieves the outpur from the model
        List<Tensor<?>> result = s.runner().feed("serving_default_dense_input:0",input).fetch("StatefulPartitionedCall:0").run();

        //processes the output to get the 3 best moves, if one is a blank hexagon, place that piece. otherwise, pick a random place
        //this is to overcome hexagons being overwritten(extremely low likelihood but not 0.)

        float[][] output1 = result.get(0).copyTo(new float[1][37]);
        float max = -2;
        int maxindex = 0;
        int secondindex = 1;
        int thirdindex = 1;

        for (int i = 0; i <output1[0].length ; i++) {
            if (output1[0][i]>max){
                thirdindex = secondindex;
                secondindex = i;
                max = output1[0][i];
                maxindex = i;
            }
        }
        Random r = new Random();
        if(field.get(maxindex).getMyState() == Hexagon.state.BLANK){
            field.get(maxindex).setMyState(Hexagon.state.RED);
        }
        else if(field.get(secondindex).getMyState() == Hexagon.state.BLANK){
            field.get(secondindex).setMyState(Hexagon.state.RED);
        }
        else if(field.get(thirdindex).getMyState() == Hexagon.state.BLANK){
            field.get(thirdindex).setMyState(Hexagon.state.RED);
        }
        else {
            int i = r.nextInt(field.size());
            while (field.get(i).getMyState() != Hexagon.state.BLANK){
                field.get(i).setMyState(Hexagon.state.RED);
                i = r.nextInt(field.size());
            }
        }
        //the above is repeated for blue.

        gs = new GameState();
        gs.update(field);
        s = bestBoy.session();
        features = new float[38];
        for (int i = 0; i < 36; i++) {
            features[i]= (float)(double)gs.getState().get(i);
        }

        features[37] = -1.0f;

        db = FloatBuffer.wrap(features);

        input = Tensor.create(new long[]{1,38}, db);
        result = s.runner().feed("serving_default_dense_input:0",input).fetch("StatefulPartitionedCall:0").run();

        output1 = result.get(0).copyTo(new float[1][37]);
        max = -2;
        maxindex = 0;
        for (int i = 0; i <output1[0].length ; i++) {
            if (output1[0][i]>max){
                thirdindex = secondindex;
                secondindex = i;
                max = output1[0][i];
                maxindex = i;
            }
        };
        if(field.get(maxindex).getMyState() == Hexagon.state.BLANK){
            field.get(maxindex).setMyState(Hexagon.state.BLUE);
        }
        else if(field.get(secondindex).getMyState() == Hexagon.state.BLANK){
            field.get(secondindex).setMyState(Hexagon.state.BLUE);
        }
        else if(field.get(thirdindex).getMyState() == Hexagon.state.BLANK){
            field.get(thirdindex).setMyState(Hexagon.state.BLUE);
        }
        else {
            int i = r.nextInt(field.size());
            while (field.get(i).getMyState() != Hexagon.state.BLANK){
                field.get(i).setMyState(Hexagon.state.BLUE);
                i = r.nextInt(field.size());
            }
        }
    }

}
