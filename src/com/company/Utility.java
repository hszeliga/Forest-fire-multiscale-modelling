package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Utility {
    DataManager dm;

    public Utility(DataManager dm){
        this.dm = dm;
    }

//x - prog binaryzacji, b - color channel (=0 dla grayscale 8bit img)
// jesli wartosc jest <= to s=0 (czarny), a jak nie to s=255 (bialy)
    void tresholding(int x){
       for(int i=0; i<dm.img.getWidth(); i++){
           for(int j=0; j<dm.img.getHeight(); j++){
               if(dm.img.getRaster().getSample(i,j,0)<=x)
                   dm.img.getRaster().setSample(i,j,0,0);
               else
                   dm.img.getRaster().setSample(i,j,0,255);
           }
       }
    }

    int[] neighbours(int width, int height) {
        int[] neighbours = new int[8];
        int k=0;
        for (int i = width-1; i <= width+1; i++) {
            for (int j = height-1; j <= height+1; j++) {
                if(i==width && j==height)
                    continue;
                else{
                    neighbours[k]=dm.pixels[i][j];
                    k++;
                }
            }
        }
        return neighbours;
    }

    Forest[] neighboursForest(int width, int height) {
        Forest[] neighbours = new Forest[8];
        int k = 0;
        try{
            for (int i = width - 1; i <= width + 1; i++) {
                for (int j = height - 1; j <= height + 1; j++) {
                    if (i == width && j == height)
                        continue;
                    else {
                        neighbours[k] = dm.forest[i][j];
                        k++;
                    }
                }
            }
        }
        catch (Exception e) {
            if(width==0 && height==0) {
                neighbours[0]=new Forest();
                neighbours[1]=new Forest();
                neighbours[2]=new Forest();
                neighbours[3]=new Forest();
                assert dm.forest != null;
                neighbours[4]=dm.forest[width][height+1];
                neighbours[5]=new Forest();
                neighbours[6]=dm.forest[width+1][height];
                neighbours[7]=dm.forest[width+1][height+1];
            }

            else if(width==0 && height==dm.height-1) {
                neighbours[0]=new Forest();
                neighbours[1]=new Forest();
                neighbours[2]=new Forest();
                assert dm.forest != null;
                neighbours[3]=dm.forest[width][height-1];
                neighbours[4]=new Forest();
                neighbours[5]=dm.forest[width+1][height-1];
                neighbours[6]=dm.forest[width+1][height];
                neighbours[7]=new Forest();
            }

            else if(width==dm.width-1 && height==0) {
                neighbours[0]=new Forest();
                assert dm.forest != null;
                neighbours[1]=dm.forest[width-1][height];
                neighbours[2]=dm.forest[width-1][height+1];
                neighbours[3]=new Forest();
                neighbours[4]=dm.forest[width][height+1];
                neighbours[5]=new Forest();
                neighbours[6]=new Forest();
                neighbours[7]=new Forest();
            }

            else if(width==dm.width-1&&height == dm.height-1) {
                assert dm.forest != null;
                neighbours[0]=dm.forest[width-1][height-1];
                neighbours[1]=dm.forest[width-1][height];
                neighbours[2]=new Forest();
                neighbours[3]=dm.forest[width][height-1];
                neighbours[4]=new Forest();
                neighbours[5]=new Forest();
                neighbours[6]=new Forest();
                neighbours[7]=new Forest();
            }

            else if(width== 0) {
                neighbours[0]=new Forest();
                neighbours[1]=new Forest();
                neighbours[2]=new Forest();
                assert dm.forest != null;
                neighbours[3]=dm.forest[width][height-1];
                neighbours[4]=dm.forest[width][height+1];
                neighbours[5]=dm.forest[width+1][height-1];
                neighbours[6]=dm.forest[width+1][height];
                neighbours[7]=dm.forest[width+1][height+1];
            }

            else if(width== dm.width-1) {
                assert dm.forest != null;
                neighbours[0]=dm.forest[width-1][height-1];
                neighbours[1]=dm.forest[width-1][height];
                neighbours[2]=dm.forest[width-1][height+1];
                neighbours[3]=dm.forest[width][height-1];
                neighbours[4]=dm.forest[width][height+1];
                neighbours[5]=new Forest();
                neighbours[6]=new Forest();
                neighbours[7]=new Forest();
            }

            else if(height== 0) {
                neighbours[0]=new Forest();
                assert dm.forest != null;
                neighbours[1]=dm.forest[width-1][height];
                neighbours[2]=dm.forest[width-1][height+1];
                neighbours[3]=new Forest();
                neighbours[4]=dm.forest[width][height+1];
                neighbours[5]=new Forest();
                neighbours[6]=dm.forest[width+1][height];
                neighbours[7]=dm.forest[width+1][height+1];
            }

            else if(height== dm.height-1) {
                assert dm.forest != null;
                neighbours[0]=dm.forest[width-1][height-1];
                neighbours[1]=dm.forest[width-1][height];
                neighbours[2]=new Forest();
                neighbours[3]=dm.forest[width][height-1];
                neighbours[4]=new Forest();
                neighbours[5]=dm.forest[width+1][height-1];
                neighbours[6]=dm.forest[width+1][height];
                neighbours[7]=new Forest();
            }
        }
        return neighbours;
    }


    void dilation(){
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
                dm.pixels[i][j]= dm.img.getRaster().getSample(i,j,0);
            }
        }
        for(int i=1; i<dm.img.getWidth()-1; i++){
            for(int j=1; j<dm.img.getHeight()-1; j++){
                int[] middlePixelNeighbour = neighbours(i, j);
                for (int value : middlePixelNeighbour) {
                    if (value == 0) { //jesli jakikolwiek sasiad srodkowego piksela jest czarny no to srodkowy ustawiamy na czarny
                        dm.img.getRaster().setSample(i, j, 0, 0);
                        break;
                    }
                }
            }
        }
    }

    void erosion() {
        for(int i=0; i<dm.img.getWidth(); i++){
            for(int j=0; j<dm.img.getHeight(); j++){
                dm.pixels[i][j]= dm.img.getRaster().getSample(i,j,0);
            }
        }
        for(int i=1; i<dm.img.getWidth()-1; i++){
            for(int j=1; j<dm.img.getHeight()-1; j++){
                int[] middlePixelNeighbour = neighbours(i, j);
                for (int value : middlePixelNeighbour) {
                    if (value == 255) { //jesli jakikolwiek sasiad srodkowego piksela jest bialy no to srodkowy ustawiamy na bialy
                        dm.img.getRaster().setSample(i, j, 0, 255);
                        break;
                    }
                }
            }
        }
    }

    void setForest(int x, int airHumidity, int wind){
        tresholding(x);
        dilation();
        dilation();
        erosion();
        erosion();

        BufferedImage rgbImage = new BufferedImage(dm.img.getWidth(), dm.img.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.getGraphics().drawImage(dm.img, 0, 0, null);
        dm.img=rgbImage;

        for(int i=0; i<dm.img.getWidth(); i++) {
            for (int j = 0; j < dm.img.getHeight(); j++) {
                if (dm.img.getRGB(i, j) == Color.WHITE.getRGB()) {
                    dm.img.setRGB(i, j, new Color(1, 68, 33).getRGB());
                    dm.forest[i][j] = new Forest(airHumidity, wind, Forest.Condition.NORMAL, true);
                }
                else {
                    dm.img.setRGB(i, j, new Color(30, 91, 110).getRGB());
                    dm.forest[i][j] = new Forest(-10000, -10000, Forest.Condition.BURNT, false);
                }
            }
        }

        for (int i = 0; i < dm.width; i++) {
            for (int j = 0; j < dm.height; j++) {
                dm.forestCondition[i][j]=dm.forest[i][j].condition;
            }
        }
    }

    void setFire(){
        while(true) {
            for (int i = 0; i < dm.height ; i++) {
                for (int j = 0; j < dm.width; j++) {
                    Random random = new Random();
                    Forest[] forests = neighboursForest(j, i);
                    Forest middlePixel = dm.forest[j][i];
                    int k = 0, l=0, humidity = 0, wind = 0;

                    if (middlePixel.condition == Forest.Condition.NORMAL)
                        if(random.nextInt(999999)==999)
                            dm.forestCondition[j][i]= Forest.Condition.FIRE;

                    if (middlePixel.condition == Forest.Condition.BURNT)
                        if(random.nextInt(999999)==999)
                            dm.forestCondition[j][i]= Forest.Condition.NORMAL;

                    if (middlePixel.condition == Forest.Condition.NORMAL) {
                        for (Forest value : forests) {
                            if (value.condition == Forest.Condition.FIRE)
                                k++;
                        }

                        for (Forest forest : forests) {
                            int y = random.nextInt(1000) % 100;
                            l += y;
                            humidity += forest.airHumidity * y;
                            wind += forest.wind*9;
                        }
                        humidity=humidity/(8+l);
                        wind=wind/(100+l);

                        if ((k == 1 && humidity >= 0 && humidity <= random.nextInt(100))||(k==1 && wind>0))
                            dm.forestCondition[j][i] = Forest.Condition.FIRE;
                    }

                    else if (middlePixel.condition == Forest.Condition.FIRE)
                        dm.forestCondition[j][i] = Forest.Condition.BURNT;
                }
            }
            for (int i = 0; i < dm.width; i++) {
                for (int j = 0; j < dm.height; j++) {
                    dm.forest[i][j].condition = dm.forestCondition[i][j];
                    if (dm.forest[i][j].check) {
                        if (dm.forest[i][j].condition == Forest.Condition.BURNT)
                            dm.img.setRGB(i, j, new Color(255,90,0).getRGB());
                        else if (dm.forest[i][j].condition == Forest.Condition.FIRE)
                            dm.img.setRGB(i, j, new Color(255,206,0).getRGB());
                    }
                }
            }
        }
    }

    void constructQT(){
        int width = 600;
        int height = 330;

        Rectangle initialRectangle = new Rectangle(new Point(0,0), width, height);
        dm.rootNode = new Node(initialRectangle, null, null, null, null);
    }

}
