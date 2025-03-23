package org.example;

import java.awt.*;

public class StringKnot extends StringNode {
    private int leftString, rightString;
    private KnotType dir;
    private StringNode leftChild = null, rightChild = null;

    public StringKnot( int l, int r ) {
        leftString = l;
        rightString = r;
        dir = KnotType.CROSS_RIGHT;
    }

    public StringKnot( int l, int r, KnotType d ) {
        leftString = l;
        rightString = r;
        dir = d;
    }

    public void setDir( KnotType dir ) {
        this.dir = dir;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public KnotType getDir() {
        return dir;
    }

    @Override
    public void setString( int string, boolean rightString ) {
        if ( rightString ) this.rightString = string;
        else this.leftString = string;
    }

    @Override
    public void setChild( StringNode node, boolean rightChild ) {
        if ( rightChild ) this.rightChild = node;
        else this.leftChild = node;
    }

    @Override
    public int getColor() {
        return dir.ordinal() % 2 == 0 ? leftString : rightString;
    }

    @Override
    public int getStringOut( boolean rightString ) {
        return ( dir.ordinal() < 2 ^ rightString ) ? this.rightString : this.leftString;
    }

    @Override
    public void incrementDir() {
        this.dir = KnotType.getKnotType( ( dir.ordinal() + 1 ) % 4 );
        propagate( true );
    }

    @Override
    public void propagate( boolean rightEdge ) {
        if ( this.leftChild == null ) return;
        if ( this.rightChild == null ) return;
        int left_out = leftString, right_out = leftString;
        if ( dir.ordinal() < 2 ) left_out = rightString;
        else right_out = rightString;

        leftChild.setString( left_out, true );
        leftChild.propagate( false );
        rightChild.setString( right_out, false );
        if ( rightEdge ) rightChild.propagate( true );
    }
}
