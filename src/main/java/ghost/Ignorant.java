package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

/**
One of the four Ghost variants in the system. 
<p>
Extends from the Ghost Class. 
Acts as the enemy of the Waka. 
Will change between Scatter mode (aiming for the bottom left corner) and Chase mode 
(aiming for the space the player occupies if it is more than 8 grid spaces away, 
otherwise aiming for it's Scatter target).
*/
public class Ignorant extends Ghost {

    /**
    Constructs a new Ignorant object. 
    <p>
    The instance stores it's parameters as it's variables.

    @param app The App instance the Ignorant is part of.
    @param x The starting x coordinate of the Ignorant.
    @param y the starting y coordinate of the Ignorant.
    */
    public Ignorant(PApplet app, int x, int y) {
        super(app, x, y);
    }

    /** 
    Returns the direction that the Ignorant can move to get closer to it's Scatter target.
    <p>
    The Ignorant's Scatter Target is the bottom left corner of the map. 
    Will assign the Ignorant's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers 
        they are in at an intersection. 
    @param gameManager The GameManager instance the Ignorant is part of.
    @return The integer relating to the direction the Ignorant 
        should move to get to the bottom left corner.
    */
    public int wayToScatter(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        Coordinate target = gameManager.getApp().getMap().getBottomLeft();
        int distance = 1000000;
        int directionToGo = 0;
        for (Coordinate coord : possibleMoves.keySet()) {
            if (Coordinate.eucliDist(coord, target) < distance) {
                directionToGo = possibleMoves.get(coord);
                distance = Coordinate.eucliDist(coord, target);
            } 
        }
        this.target = target;
        return directionToGo;
    }

    /**
    Returns the direction that the Ignorant can move to get closer to it's Chase target.
    <p>
    The Ignorant's chase target is the space the player occupies if the Ignorant is more than 8 grid spaces away. 
    Otherwise, it will target the bottom left corner. 
    Will assgin the Ignorant's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers
        they are in at an intersection.
    @param gameManager The GameManager instance the Ignorant is part of.
    @return The integer relating to the direction the Ignorant should go to to get to the Chase target. 
    */
    public int wayToChase(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        Coordinate wakaCoord = gameManager.getApp().getWaka().getCoord();
        Coordinate bottomLeft = gameManager.getApp().getMap().getBottomLeft();
        Coordinate target = null;
        int distance = 1000000;
        int distFromWaka = 8 * 16;
        int directionToGo = 0;
        for (Coordinate coord : possibleMoves.keySet()) {
            if (Coordinate.eucliDist(coord, wakaCoord) > distFromWaka) {
                target = wakaCoord;
            } else {
                target = bottomLeft;
            }
            if (Coordinate.eucliDist(coord, target) < distance) {
                directionToGo = possibleMoves.get(coord);
                distance = Coordinate.eucliDist(coord, target);
            } 
        }
        this.target = target;
        return directionToGo;
    }

    /**
    Changes the Ignorant's sprite to it's original image.

    @param app The App instance the Ignorant is part of.
     */
    public void beBrave(PApplet app) {
        PImage sprite = app.loadImage("src/main/resources/ignorant.png");
        this.setSprite(sprite);
    }
}