package com.company;

import java.awt.image.BufferedImage;

public class DataManager {
    BufferedImage img;
    Node rootNode;

    int width = 600;
    int height = 330;

    int[][] pixels;
    Forest[][] forest;
    Forest.Condition[][] forestCondition;

    int choice = 0;

    public DataManager(){
        pixels = new int [width][height];
        forest =new Forest[width][height];
        forestCondition =new Forest.Condition[width][height];
    }

}
