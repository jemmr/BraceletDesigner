package org.example;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        JPanel mainPanel = new JPanel();
        InputPanel inputPanel = new InputPanel();
        ArtPanel artPanel = new ArtPanel( jFrame );
        BraceletPanel braceletPanel = new BraceletPanel();
        inputPanel.addPropertyChangeListener( "text", artPanel );
        inputPanel.addPropertyChangeListener( "image", artPanel );
        inputPanel.addPropertyChangeListener( "start", artPanel );
        artPanel.addPropertyChangeListener( "simann", braceletPanel );
        FlowLayout flowLayout = new FlowLayout();
        mainPanel.setLayout( flowLayout );
        mainPanel.setPreferredSize( new Dimension( 500, 656 ) );
        mainPanel.add( inputPanel );
        mainPanel.add( artPanel );
        mainPanel.add( braceletPanel );

        Bracelet b = getTestBracelet();
        braceletPanel.setBracelet( b );

        jFrame.setContentPane( mainPanel );
        jFrame.pack();
        jFrame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        jFrame.setVisible( true );
    }

    private static Bracelet getTestBracelet() {
        Color[] testColors = {
                Color.YELLOW,
                Color.BLUE,
                Color.ORANGE,
                Color.RED,
                Color.CYAN,
                Color.GREEN
        };
        int[][] testKnots = {
                {1, 1, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0},
                {1, 1, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0},
                {1, 1, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0},
                {1, 1, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0},
                {1, 1, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0},
                {1, 1, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0}
        };
        Bracelet b = new Bracelet();
        b.setColors( testColors, new int[]{0,1,2,3,4,5} );
        b.buildBracelet( testKnots );
//        b.printBracelet();
        return b;
    }
}