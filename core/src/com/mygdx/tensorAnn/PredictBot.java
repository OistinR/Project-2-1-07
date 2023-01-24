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

    public PredictBot(){
        bestBoy = SavedModelBundle.load("C:\\Users\\Oistín\\Documents\\Github\\Project-2-1-07\\core\\src\\com\\mygdx\\tensorAnn\\model47", "serve");
    }

    //Whoosplaying = 1 for red -1 for blue
    @Override
    public void execMove(ArrayList<Hexagon> field) {
        runtime=0;
        long startTime = System.nanoTime();
        calculate(field);
        long endTime = System.nanoTime();
        //test test test test test test
        long duration = (endTime - startTime);
        runtime += duration/1000;
    }
    @Override
    public void calculate(ArrayList<Hexagon> field) {
        GameState gs = new GameState();
        gs.update(field);
        Session s = bestBoy.session();
        float[] features = new float[38];
        for (int i = 0; i < 36; i++) {
            features[i]= (float)(double)gs.getState().get(i);
        }

        features[37] = 1.0f;
//        System.out.println(features);

        FloatBuffer db = FloatBuffer.wrap(features);

        Tensor<Float> input = Tensor.create(new long[]{1,38}, db);
        List<Tensor<?>> result = s.runner().feed("serving_default_dense_input:0",input).fetch("StatefulPartitionedCall:0").run();
//        System.out.println(Arrays.deepToString(result.get(0).copyTo(new float[1][37])));
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

        gs = new GameState();
        gs.update(field);
        s = bestBoy.session();
        features = new float[38];
        for (int i = 0; i < 36; i++) {
            features[i]= (float)(double)gs.getState().get(i);
        }

        features[37] = -1.0f;
//        System.out.println(features);

        db = FloatBuffer.wrap(features);

        input = Tensor.create(new long[]{1,38}, db);
        result = s.runner().feed("serving_default_dense_input:0",input).fetch("StatefulPartitionedCall:0").run();
//        System.out.println(Arrays.deepToString(result.get(0).copyTo(new float[1][37])));
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

// code used for getting tensor names, not original
//    public static void main(String[] args) {
//        /*
//        final MetaGraphDef metaGraphDef;
//        try {
//            metaGraphDef = MetaGraphDef.parseFrom(bestBoy.metaGraphDef());
//            final SignatureDef signatureDef = metaGraphDef.getSignatureDefMap().get("serving_default");
//
//            final TensorInfo inputTensorInfo = signatureDef.getInputsMap()
//                    .values()
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .findFirst()
//                    .orElseThrow(ArithmeticException::new);
//            final TensorInfo outputTensorInfo = signatureDef.getOutputsMap()
//                    .values()
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .findFirst()
//                    .orElseThrow(ArithmeticException::new);
//            System.out.println(inputTensorInfo.getName());
//            System.out.println(outputTensorInfo.getName());
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//
//         */
//    }
}
