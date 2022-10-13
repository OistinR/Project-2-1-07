package com.mygdx.game.bots;

import java.util.ArrayList;

import com.mygdx.game.coordsystem.Hexagon;

/** This abstract class is the class that all future bots will inherit.
 *  The methods will be shared as they allow the bots to interact with the game.
 *  It is also to track the runtime of a bot.
 */
public abstract class Bot {

    protected long runtime;

    /**
     * constructer for intialising the runtime of the bot.
     */
    public Bot() {
        this.runtime=0;
    }


    /**
     * @param field This is the list of hexagons for the both to analyse.
     * This is the general method for performing a move for a bot.
     */
    public void execMove(ArrayList<Hexagon> field) {
        runtime=0;
        long startTime = System.nanoTime();
        calculate(field);
        long endTime = System.nanoTime();
        //test test test test test test
        long duration = (endTime - startTime);
        runtime += duration/1000;
    }

    /**
     * @param field
     * To be implemented by the class who extends this one affects the field in some way.
     */

    public void calculate(ArrayList<Hexagon> field) {}


    /**
     * @return runtime of the bot
     */
    public long getRuntime() {
        return runtime;
    }
}
