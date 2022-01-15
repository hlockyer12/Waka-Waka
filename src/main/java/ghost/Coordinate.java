package ghost;

import java.lang.Math;

/**
A class to represent X and Y coordinates within the game.
*/
public class Coordinate {

    private int x;
    private int y;

    /**
    Constructs a new Coordinate instance.
    <p>
    Takes in the X and Y values relating to it's position in the window.
    
    @param x The X axis value of the coordinate.
    @param y The Y axis value of the coordinate.
    */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
    Returns the X value of the coordinate.
    
    @return The X axis value of the coordinate.
    */
    public int getX() {
        return this.x;
    }

    /**
    Returns the Y value of the coordinate.
    
    @return The Y axis value of the coordinate.
    */
    public int getY() {
        return this.y;
    }

    /**
    Returns the straight line distance between two coordinates.
    
    @param a The first coordinate.
    @param b The second coordinate.
    @return The straight line distance between the two coordinates as an integer.
    */
    public static int eucliDist(Coordinate a, Coordinate b) {
        int xDistSqr = (a.getX() - b.getX()) * (a.getX() - b.getX());
        int yDistSqr = (a.getY() - b.getY()) * (a.getY() - b.getY());
        int dist = (int) Math.sqrt(xDistSqr + yDistSqr);
        return dist;
    }
    
    /**
    Checks if a different coordinate has the same X and Y values as the instance. 
    
    @param coord The coordinate to be compared.
    @return True if both the coordinates have the same X axis and Y axis values.
        Returns False otherwise.*/
    public boolean equals(Coordinate coord) {
        if (this.x == coord.getX() && this.y == coord.getY()) {
            return true;
        } else {
            return false;
        }
    }

    /**
    Converts the coordinate to an integer index value that relates to the 
    position in a Map objects MapPiece list.
    
    @return The index at which the coordinate lies within the Map object MapPiece list.
    */
    public int toIndex() {
        int x = this.x / 16;
        int y = this.y / 16;
        int index = (y * 28) + x;
        return index;
    }
}