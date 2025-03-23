package org.example;

import org.example.Bracelet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class BraceletPanel extends JPanel implements MouseListener, PropertyChangeListener {
    private Bracelet bracelet;
    private boolean showEditor = false;

    public BraceletPanel() {
        super();
        setPreferredSize( new Dimension( 500, 120 ) );
        setBackground( Color.lightGray );
        addMouseListener( this );
    }

    public void setBracelet( Bracelet bracelet ) {
        this.bracelet = bracelet;
        setPreferredSize( new Dimension( 500, bracelet.getWidth()*16 ) );
    }

    private void drawPreview( Graphics g ) {
        int botLeft = 16 * bracelet.getKnotsWide();
        for ( int i = 0; i < bracelet.getLength(); i++ ) {
            for ( int j = 0; j < bracelet.getKnotsWide(); j++ ) {
                if ( bracelet.getType( i, j ) == 1 ) {
                    Color c = bracelet.getColor( i, j );
                    g.setColor( c );
                    int x = 20*i;
                    int y = botLeft - j*32 + i%2*16;
                    g.fillOval( x, y, 32, 32);
                    g.setColor( Color.BLACK );
                    g.drawOval( x, y, 32, 32);
                    if ( showEditor ) {
                        if ( c.getRed() + c.getGreen() + c.getGreen() < 384 ) g.setColor( Color.WHITE );
                        int t = bracelet.getDir( i, j ).ordinal();
                        g.drawLine( x+8,y+24 - t%2*16, x+16, y+16);
                        g.drawLine( x+16,y+16, x+24, y+8 + (t+1)%4/2*16);
                    }
                }
            }
        }
//        if ( showEditor ) {
//            for (int i = 0; i < bracelet.getLength(); i++) {
//                for (int j = 0; j < bracelet.getKnotsWide(); j++) {
//                    if (bracelet.getType(i, j) == 1) {
//                        g.setColor(Color.RED);
//                        int x = 20 * i + 4;
//                        int y = botLeft - j * 32 + i % 2 * 16;
//                        g.drawRect(x, y, 20, 32);
//                    }
//                }
//            }
//        }
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        if ( bracelet != null ) {
            drawPreview( g );
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX(), y = e.getY();
        showEditor = true;
        int i = (x - 4)/20;
        int j = bracelet.getKnotsWide() - (y/16 + 4 - (bracelet.getWidth()+i)%2)/2;
        System.out.println(STR."Click! (\{x},\{y}) -> (\{i}, \{j})");
        bracelet.incrementKnot( i, j );
        repaint();
        revalidate();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        showEditor = true;
        repaint();
        revalidate();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        showEditor = false;
        repaint();
        revalidate();
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt ) {
        if ( !Objects.equals( evt.getPropertyName(), "simann") ) return;
        SimAnnealer simAnnealer = (SimAnnealer) evt.getNewValue();
        simAnnealer.makeBracelet();
        Bracelet b = simAnnealer.getBracelet();
        if ( !Objects.isNull(b) ) bracelet = b;
    }
}
