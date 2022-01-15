package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
Represents all of the walls in the map.
<p>
The player or ghosts cannot move through the walls.
*/
public class WallPiece implements MapPiece {

    private int x;
    private int y;
    private PImage sprite;
    private Coordinate coord;

    /**
    Constructs a new WallPiece instance.
    
    @param sprite The image of the wall to be drawn.
    @param x The X axis position of the piece.
    @param y The Y axis position of the piece.
    */
    public WallPiece(PImage sprite, int x, int y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.coord = new Coordinate(this.x, this.y);
    }

    /**
    Draws this pieces sprite to the app window.
    
    @param app The app the piece belongs to.
    */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    /**
    Returns the piece's coordinate. 
    
    @return The coordinate of the piece.
    */
    public Coordinate getCoord() {
        return this.coord;
    }

}