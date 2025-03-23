package org.example;

public enum KnotType {
    CROSS_RIGHT,    // left thread knots, crossing over to right
    CROSS_LEFT,     // right thread knots, crossing over to left
    BEND_LEFT,      // left thread knots, bending back to left
    BEND_RIGHT;     // right thread knots, bending back to right

    public static KnotType getKnotType( int i ) {
        return switch ( i ) {
            case 0 -> CROSS_RIGHT;
            case 1 -> CROSS_LEFT;
            case 2 -> BEND_LEFT;
            case 3 -> BEND_RIGHT;
            default -> throw new IllegalArgumentException();
        };
    }
}
