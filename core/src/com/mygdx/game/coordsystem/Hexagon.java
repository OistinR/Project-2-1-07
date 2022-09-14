package com.mygdx.game.coordsystem;

public class Hexagon {
    private int size;

    private int q;
    private int r;
    private int s;

    public Hexagon (int q, int r, int size) {
        this.q = q;
        this.r = r;
        this.s = -q-r;
        this.size = size;
    }

    public int getX() {
        return q*size/2 - s*size/2;
    }

    public int getY() {
        return -r*size/2 + q*size/4 + s*size/4;
    }
}
