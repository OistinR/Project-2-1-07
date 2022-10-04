package com.mygdx.game.bots;

import java.util.ArrayList;

import com.mygdx.game.coordsystem.Hexagon;

public abstract class Bot {

    protected long runtime;

    public Bot() {
        this.runtime=0;
    }

    public void execMove(ArrayList<Hexagon> field) {
        runtime=0;
        long startTime = System.nanoTime();
        calculate(field);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        runtime += duration/1000;
    }

    //Empty method, to be implemented by the class who extends this one
    public void calculate(ArrayList<Hexagon> field) {}

    public long getRuntime() {
        return runtime;
    }
}
