package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
Represents an empty space within the map.
<p>
Implements the MapPiece interface.
*/
public class EmptyPiece implements MapPiece {

    private int x;
    private int y;
    private Coordinate coord;

    /**
    Constructs a new EmptyPiece instance.
    
    @param x The X axis position of the piece.
    @param y The Y axis position of the piece.
    */
    public EmptyPiece(int x, int y) {
        this.x = x;
        this.y = y;
        this.coord = new Coordinate(this.x, this.y);
    }

    /**
    Will not draw anything in the app.
    
    @param app The app the piece belongs to.
    */
    public void draw(PApplet app) {
        return;
    }

    /**
    Returns the piece's coordinate. 
    
    @return The coordinate of the piece.
    */
    public Coordinate getCoord() {
        return this.coord;
    }
    
}