package com.mygdx.game.gametree;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.screens.GameScreen;
import java.util.random.*;;

/**
 * NOTE: this class is undone and currently Vince has experimented with some stuff
 * Might not be needed 
 */

public class Tree{
    Node root;
    static ArrayList<Hexagon> field;

    public Tree(Node root, ArrayList<Hexagon> Field){
        this.root = root;
        this.field = Field;
    }

    public void createTree(int maxDepth, Hexagon root){
        Node savedHex = new Node(null,root); // create root Node saved hex = root node
        int currentDepth = 0; // initial depth
        recursive(currentDepth, maxDepth, savedHex);
    }

    public void recursive(int currentDepth, int maxDepth,  Node savedHex){
        while(currentDepth >= maxDepth){
            savedHex.createChildren(); 
            Node nextHex = getNextChild(savedHex); // get random child
            this.field.add(nextHex.hexagonPlaced); // add child's hexagon to the field
            currentDepth++;
            savedHex = nextHex;
            recursive(currentDepth, maxDepth, savedHex);
        }
    }

    public Node getNextChild(Node savedHex){
        int random = ThreadLocalRandom.current().nextInt(0, savedHex.getChildArray().size()-1);
        return savedHex.getChildArray().get(random);
    }

  
}