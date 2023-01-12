package com.mygdx.game.bots.MCST.Learning;

import java.util.List;

public class WeightedSumFunction {

    public double collectOutput(List<Neuron_Connection> inputConnections) {
        double weightedSum = 0;
        for (Neuron_Connection connection : inputConnections){
            weightedSum += connection.getWeightInput();
        }
        return weightedSum;
    }
}
