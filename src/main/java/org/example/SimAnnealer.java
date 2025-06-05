package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.*;

public class SimAnnealer {
    BufferedImage image;
    Color[] colorSet;
    Bracelet bracelet;
    int[][] ideals;

    public SimAnnealer() {
        this.bracelet = null;
    }

    public void setArgs( BufferedImage image, Color[] colorSet ) {
        this.image = image;
        this.colorSet = colorSet;
        this.bracelet = null;
    }

    public void starter() {
        int[][] testKnots = new int[][]{
                {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}
        };
        bracelet = new Bracelet();
        bracelet.setColors( colorSet, new int[]{0, 0, 1, 2, 2, 3} );
        bracelet.buildBracelet( testKnots );
        prepIdeals();
    }

    public void prepIdeals() {
        int w = bracelet.getKnotsWide(), l = bracelet.getLength();
        ideals = new int[l][w];
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if ( bracelet.getType( i, j ) != 1 ) continue;
                int x = 20 * i + 4;
                int y = 16* w - j * 32 + i % 2 * 16;
                int[] avg = new int[]{0, 0, 0};
                for (int p = 0; p < 1024; p++ ) {
                    Color c = new Color( image.getRGB( x+(p/32), y+(p%32) ) );
                    avg[0] += c.getRed();
                    avg[1] += c.getGreen();
                    avg[2] += c.getBlue();
                }
                avg[0] = avg[0]/1024;
                avg[1] = avg[1]/1024;
                avg[2] = avg[2]/1024;
                ideals[i][j] = getClosest(avg);
            }
        }
    }

    private int getClosest(int[] avg) {
        int closest = 0;
        double diff = Math.pow(colorSet[0].getRed()- avg[0], 2)
                + Math.pow(colorSet[0].getGreen()- avg[1], 2)
                + Math.pow(colorSet[0].getBlue()- avg[2], 2);
        for ( int comp = 1; comp < 4; comp++) {
            double diff2 = Math.pow(colorSet[comp].getRed()- avg[0], 2)
                    + Math.pow(colorSet[comp].getGreen()- avg[1], 2)
                    + Math.pow(colorSet[comp].getBlue()- avg[2], 2);
            if ( diff2 < diff ) {
                closest = comp;
                diff = diff2;
            }
        }
        return closest;
    }

    public Bracelet getBracelet() {
        if ( Objects.isNull( bracelet ) ) makeBracelet();
        return bracelet;
    }

    public int score() {
        int w = bracelet.getKnotsWide(), l = bracelet.getLength();
        double[][] diffs = new double[4][4];
        for ( int i = 0; i < 16; i++ ) {
            Color c1 = colorSet[i/4], c2 = colorSet[i%4];
            diffs[i/4][i%4] = Math.pow(c1.getRed()-c2.getRed(), 2)
                    + Math.pow(c1.getGreen()-c2.getGreen(), 2)
                    + Math.pow(c1.getBlue()-c2.getBlue(), 2);
        }
        int curveScore = 0;
        double colorScore = 0;
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if ( bracelet.getType( i, j ) != 1 ) continue;
                if ( bracelet.getDir(i,j).ordinal() > 1 ) curveScore += 1;
                colorScore += diffs[bracelet.getColorInd(i,j)][ideals[i][j]];
            }
        }
        return curveScore + (int) colorScore;
    }

    private void anneal() {  // code largely based on https://www.geeksforgeeks.org/simulated-annealing/
        double curTemp = 1, finTemp = 0.001, decTemp = 0.9;
        int iter = 1000, l = bracelet.getLength(), kw = bracelet.getKnotsWide(), w = bracelet.getWidth();
        double minScore = score(), curScore = minScore;
        while ( curTemp > finTemp ) {
            for ( int i = 0; i < iter; i++ ){
                if ( curScore < minScore ) minScore = curScore;

                int x, y, old = 0;
                x = (int) (Math.random()*(l+1)) - 1;
                if ( x == -1 ) {
                    y = (int) (Math.random()*w);
                    old = bracelet.colorOrderValue(y, (int) (Math.random()*4));
                } else {
                    y = (int) (Math.random()*kw);
                    bracelet.incrementKnot( x, y );
                }
                double newScore = score();
                double ap = Math.pow(Math.E, (curScore - newScore)/curTemp);
                if (ap <= Math.random()) {
                    if ( x == -1 ) bracelet.colorOrderValue( y, old );
                    else bracelet.decrementKnot( x, y );
                } else curScore = newScore;
            }
            curTemp *= decTemp; // Decreases T, cooling phase
        }
//        bracelet.printBracelet();
        System.out.printf( "solution created, score = %f\n", minScore );
    }

    public void makeBracelet() {
        starter();
        anneal();
    }
}
