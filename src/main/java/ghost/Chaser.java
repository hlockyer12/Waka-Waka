package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

/**
One of the four Ghost variants in the system. 
<p>
Extends from the Ghost Class. 
Acts as the enemy of the Waka. 
Will change between Scatter mode (aiming for the top left corner) and 
Chase mode (aiming for the space the player occupies).
*/
public class Chaser extends Ghost {

    /**
    Constructs a new Chaser object. 
    <p>
    The instance stores it's parameters as it's variables.

    @param app The App instance the Chaser is part of.
    @param x The starting x coordinate of the Chaser.
    @param y the starting y coordinate of the Chaser.
    */
    public Chaser(PApplet app, int x, int y) {
        super(app, x, y);
    }

    /** 
    Returns the direction that the Chaser can move to get closer to it's Scatter target.
    <p>
    The Chaser's Scatter Target is the top left corner of the map. 
    Will assign the Chaser's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers 
        they are in at an intersection. 
    @param gameManager The GameManager instance the Chaser is part of.
    @return The integer relating to the direction the Chaser 
        should move to get to the top left corner.
    */
    public int wayToScatter(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        Coordinate target = gameManager.getApp().getMap().getTopLeft();
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
    Returns the direction that the Chaser can move to get closer to it's Chase target.
    <p>
    The Chaser's chase target is the space the player occupies. 
    Will assgin the Chaser's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers
        they are in at an intersection.
    @param gameManager The GameManager instance the Chaser is part of.
    @return The integer relating to the direction the Chaser should go to to get to the Chase target. 
    */
    public int wayToChase(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        Coordinate target = gameManager.getApp().getWaka().getCoord();
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
    Changes the Chaser's sprite to it's original image.

    @param app The App instance the Chaser is part of.
    */
    public void beBrave(PApplet app) {
        PImage sprite = app.loadImage("src/main/resources/chaser.png");
        this.setSprite(sprite);
    }
}