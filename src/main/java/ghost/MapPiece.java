package ghost;

import processing.core.PApplet;

/**
An interface for all MapPieces in the game.
<p>
Outline the required methods for the game to run.
*/
interface MapPiece {

    /**
    Draws the piece's sprite to the app window.
    
    @param app The app the piece belongs to.
    */
    public void draw(PApplet app);

    /**
    Returns the piece's coordinate. 
    
    @return The coordinate of the piece.
    */
    public Coordinate getCoord();

}