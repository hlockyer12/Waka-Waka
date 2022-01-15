package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
Represents one of three collectable objects on the map.
<p>
The player must collect all of the FruitPieces in order to win the game.
Will dissappear after collected.
*/
public class FruitPiece implements MapPiece {

    private int x;
    private int y;
    private PImage sprite;
    private Coordinate coord;

    /**
    Constructs a new FruitPiece instance.
    
    @param sprite The image of the fruit to be drawn.
    @param x The X axis position of the piece.
    @param y The Y axis position of the piece.
    */
    public FruitPiece(PImage sprite, int x, int y) {
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