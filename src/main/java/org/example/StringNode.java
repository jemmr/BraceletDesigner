package org.example;

import java.awt.*;

public abstract class StringNode {

    public abstract int getType();
    public abstract KnotType getDir();
    public abstract void setString( int string, boolean rightString );
    public abstract void setChild( StringNode node, boolean rightChild );
    public abstract int getColor();
    public abstract int getStringOut( boolean rightString );
    public abstract void incrementDir();
    public abstract void propagate( boolean rightEdge );
}
