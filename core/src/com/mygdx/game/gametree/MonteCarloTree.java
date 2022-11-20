package com.mygdx.game.gametree;

import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloTree {
    Node root;
    int maxDepth;

    public MonteCarloTree(Node root, int maxDepth){
        this.maxDepth = maxDepth;
        this.root = root;
    }

    public static void createMonteCarloTree(Node root, int maxDepth){
        root.createChildren();
        int random = ThreadLocalRandom.current().nextInt(0, root.getChildArray().size()-1);
        System.out.println("The root: " + root.toString());
        Node nextChild = root.getNextNode();
        int currentDepth = 1;
        while((currentDepth<=maxDepth) && (nextChild.usedHex.size()<nextChild.field.size()) ){
            System.out.println(nextChild.toString());
            System.out.println("Current depth: " + currentDepth);
            nextChild.createChildren();
            Node clone = nextChild;
            nextChild = clone.getNextNode();
            currentDepth = nextChild.getDepth();
            
        }
    }
}
