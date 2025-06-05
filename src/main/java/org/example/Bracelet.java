package org.example;

import java.awt.*;
import java.util.Objects;

import static java.lang.System.exit;

public class Bracelet {
    private Color[] colorSet;
    private int[] colorOrder;
    private int[][] knotMatrix;
    private StringNode[][] bracelet;
    private int width, length, knotsWide;

    public Bracelet() {
        colorSet = null;
        knotMatrix = null;
        bracelet = null;
        width = -1;
        length = -1;
        knotsWide = -1;
    }

//    public void setColors( Color[] strings ) {
//        this.colors = strings;
//        this.colorSet = strings;
//        int l = colorSet.length;
//        this.colorOrder = new int[l];
//        for ( int i = 0; i < l; i++ ) this.colorOrder[i] = i;
//        this.width = colors.length;
//    }

    public void setColors( Color[] set, int[] order ) {
        this.colorSet = set;
        this.colorOrder = order;
        this.width = order.length;
    }

    /**
     * Creates a bracelet, with width determined by the length of colors and length by `knots`.
     * The knots parameter is assumed to contain only values 0–3, and any extra values width-wise are ignored.
     *
     * @param knots     a 2D array of ints representing the layout of knots.
     */
    public void buildBracelet( int[][] knots ) {
        if ( Objects.isNull( colorSet ) || Objects.isNull( colorOrder ) ) throw new IllegalComponentStateException();
        length = knots.length/2*2;
        knotsWide = colorOrder.length/2 + 1;
//        System.out.println( STR."width: \{width}; length: \{length}; knotsWide: \{knotsWide}\n");
        if ( knots[0].length + 1 < knotsWide ) throw new IllegalArgumentException();
        boolean oddWidth = width%2 == 1;

        knotMatrix = new int[length][knotsWide];
        bracelet = new StringNode[length][knotsWide];
//        extension = length < 25 ? new StringNode[25 - length][knotsWide] : null;

        int i, j;
        StringNode newNode = null;

        System.out.print( "0: " );
        for ( j = 0; j < knotsWide - 1; j++ ) {
//            System.out.print( STR." \{j}" );
            bracelet[0][j] = new StringKnot(
                    colorOrder[2*j],
                    colorOrder[2*j + 1],
                    KnotType.getKnotType( knots[0][j] )
            );
            knotMatrix[0][j] = knots[0][j];
        }
        if ( oddWidth ) {
            bracelet[0][j] = new StringStraight(colorOrder[2 * j], true);
//            System.out.print( STR." \{j}!" );
        }
        else bracelet[0][j] = null;
//        System.out.println();

        for ( i = 1; i < length; i++ ) {
//            System.out.print( STR."\{i}:" );
            int rowParity = i%2;
            if ( rowParity == 0 ) {
//                System.out.print( " " );
                for ( j = 0; j < knotsWide - 1; j++ ) {
//                    System.out.print( STR." \{j}" );
                    newNode = new StringKnot(
                            bracelet[i - 1][j].getStringOut(true),
                            bracelet[i - 1][j + 1].getStringOut(false),
                            KnotType.getKnotType(knots[i][j])
                    );
                    bracelet[i - 1][j].setChild(newNode, true);
                    bracelet[i - 1][j + 1].setChild(newNode, false);
                    bracelet[i][j] = newNode;
                    knotMatrix[i][j] = knots[i][j];
                }
                if ( oddWidth ) {
//                    System.out.print( STR." \{j}!" );
                    newNode = new StringStraight(
                            bracelet[i - 1][j].getStringOut(true),
                            true
                    );
                    bracelet[i - 1][j].setChild(newNode, true);
                    bracelet[i][j] = newNode;
                }
            } else {
//                System.out.print( " 0" );
                newNode = new StringStraight(
                        bracelet[i - 1][0].getStringOut(false),
                        false
                );
                bracelet[i - 1][0].setChild(newNode, false);
                bracelet[i][0] = newNode;
                for ( j = 1; j < (width + 1)/2; j++ ) {
//                    System.out.print(STR." \{j}");
                    newNode = new StringKnot(
                            bracelet[i - 1][j - 1].getStringOut(true),
                            bracelet[i - 1][j].getStringOut(false),
                            KnotType.getKnotType(knots[i][j - 1])
                    );
                    bracelet[i - 1][j - 1].setChild(newNode, true);
                    bracelet[i - 1][j].setChild(newNode, false);
                    bracelet[i][j] = newNode;
                }
                if ( !oddWidth ) {
//                    System.out.print( STR." \{j}!");
                    newNode = new StringStraight(
                            bracelet[i - 1][j - 1].getStringOut(true),
                            true
                    );
                    bracelet[i - 1][j - 1].setChild(newNode, true);
                    bracelet[i][j] = newNode;
                }
            }
//            System.out.println();
        }
    }

//    public int[][] getKnotMatrix() {
//        return knotMatrix;
//    }

    public int colorOrderValue( int i, int v ) {
        if ( i >= colorOrder.length ) return -1;
        if ( v >= colorSet.length ) return -1;
        int out = colorOrder[i];
        colorOrder[i] = v;
        return out;
    }

    public void incrementKnot( int i, int j ) {
        if ( length == -1 ) return;
        if ( i < 0 ) return;
        if ( j < 0 ) return;
        if ( i >= length ) return;
        if ( j >= knotsWide ) return;
        if ( bracelet[i][j] == null ) return;
        if ( bracelet[i][j].getType() == 1 ) bracelet[i][j].incrementDir();
    }

    public void decrementKnot( int i, int j ) {
        if ( length == -1 ) return;
        if ( i < 0 ) return;
        if ( j < 0 ) return;
        if ( i >= length ) return;
        if ( j >= knotsWide ) return;
        if ( bracelet[i][j] == null ) return;
        if ( bracelet[i][j].getType() == 1 ) {
            bracelet[i][j].incrementDir();
            bracelet[i][j].incrementDir();
            bracelet[i][j].incrementDir();
        }
    }

    public int getColorInd( int i, int j ) {
        if ( i >= bracelet.length ) {
//            System.out.println( STR."Error. Invalid i given: \{i}, \{j}" );
            throw new IllegalArgumentException();
        }
        if ( j >= (colorOrder.length + j%2)/2 ) {
//            System.out.println( STR."Error. Invalid x given: \{i}, \{j}" );
            throw new IllegalArgumentException();
        }
        return bracelet[i][j].getColor();
    }

    public Color getColor( int i, int j ) {
        if ( i >= bracelet.length ) {
//            System.out.println( STR."Error. Invalid i given: \{i}, \{j}" );
            throw new IllegalArgumentException();
        }
        if ( j >= (colorOrder.length + j%2)/2 ) {
//            System.out.println( STR."Error. Invalid x given: \{i}, \{j}" );
            throw new IllegalArgumentException();
        }
        return colorSet[bracelet[i][j].getColor()];
    }

    public int getType( int i, int j ) {
        if ( i >= length ) throw new IllegalArgumentException();
        if ( j >= knotsWide ) throw new IllegalArgumentException();
        if ( bracelet[i][j] == null ) return -1;
        return bracelet[i][j].getType();
    }

    public KnotType getDir( int i, int j ) {
        if ( i >= length ) throw new IllegalArgumentException();
        if ( j >= knotsWide ) throw new IllegalArgumentException();
        if ( bracelet[i][j] == null ) throw new IllegalArgumentException();
        return bracelet[i][j].getDir();
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getKnotsWide() {
        return knotsWide;
    }

    public void printBracelet() {;
        for ( int i = 0; i < length; i++ ) {
            if ( i%2 == 1 ) System.out.print( "   " );
            for ( int j = 0; j < knotsWide; j++ ) {
                if ( getType( i, j ) == 1 ) {
                    Color c = getColor( i, j );
                    System.out.printf( " #%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue() );
                }
                System.out.println( bracelet[i][j] );
            }
            System.out.println();
        }
    }

}
