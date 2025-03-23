package org.example;

import java.awt.*;

public class StringStraight extends StringNode {
    private int color;
    private StringNode child = null;
    private final boolean isRightEdge;

    public StringStraight( int c, boolean rightEdge ) {
        color = c;
        this.isRightEdge = rightEdge;
    }

    public void setChild( StringNode child) {
        this.child = child;
    }

    @Override
    public int getType() {
        return isRightEdge ? 2 : 0;
    }

    @Override
    public KnotType getDir() {
        return isRightEdge ? KnotType.BEND_LEFT : KnotType.BEND_RIGHT;
    }

    @Override
    public void setString( int string, boolean rightString ) {
        this.color = string;
    }

    @Override
    public void setChild( StringNode node, boolean rightChild ) {
        this.child = node;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getStringOut( boolean rightString ) {
        return this.color;
    }

    @Override
    public void incrementDir() {;}

    @Override
    public void propagate( boolean rightEdge ) {
        if ( this.child == null ) return;
        this.child.setString( this.color, this.isRightEdge );
        if ( rightEdge ) this.child.propagate( true );
    }
}
