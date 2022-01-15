package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

/**
One of the four Ghost variants in the system.
<p>
Extends from the Ghost class.
Acts as the enemy of the Waka. 
Will change between Scatter mode (aiming for the top right corner) and 
Chase mode (aiming for 4 grid spaces ahead of the player).
*/
public class Ambusher extends Ghost {

    /**
    Constructs a new Ambusher object. 
    <p>
    The instance stores it's parameters as it's variables.

    @param app The App instance the Chaser is part of.
    @param x The starting x coordinate of the Chaser.
    @param y the starting y coordinate of the Chaser.
    */
    public Ambusher(PApplet app, int x, int y) {
        super(app, x, y);
    }

    /** 
    Returns the direction that the Ambusher can move to get closer to it's Scatter target.
    <p>
    The Ambusher's Scatter Target is the top right corner of the map. 
    Will assgin the Ambusher's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers 
        they are in at an intersection. 
    @param gameManager The GameManager instance the Ambusher is part of.
    @return The integer relating to the direction the Ambusher 
        should move to get to the top right corner.
    */
    public int wayToScatter(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        Coordinate target = gameManager.getApp().getMap().getTopRight();
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
    Returns the direction that the Ambusher can move to get closer to it's Chase target.
    <p>
    The Ambusher's chase target is 4 grid spaces in front of the direction the player is facing. 
    Will assgin the Ambusher's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers
        they are in at an intersection.
    @param gameManager The GameManager instance the Ambusher is part of.
    @return The integer relating to the direction the Ambusher should go to to get to the Chase target. 
    */
    public int wayToChase(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        int wakaX = gameManager.getApp().getWaka().getX() - 4;
        int wakaY = gameManager.getApp().getWaka().getY() - 5;
        int wakaDirection = gameManager.getApp().getWaka().getDirection();
        if (wakaDirection == 37) {
            wakaX = wakaX - (16 * 4);
        } else if (wakaDirection == 38) {
            wakaY = wakaY - (16 * 4);
        } else if (wakaDirection == 39) {
            wakaX = wakaX + (16 * 4);
        } else if (wakaDirection == 40) {
            wakaY = wakaY + (16 * 4);
        }
        if (wakaX < 0) {
            wakaX = 0;
        } else if (wakaX > 448) {
            wakaX = 448;
        } else if (wakaY < 0) {
            wakaY = 0;
        } else if (wakaY > 576) {
            wakaY = 576;
        }
        Coordinate target = new Coordinate(wakaX, wakaY);
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
    Changes the Ambusher's sprite to it's original image.
    
    @param app The App instance the Ambusher is part of.
    */
    public void beBrave(PApplet app) {
        PImage sprite = app.loadImage("src/main/resources/ambusher.png");
        this.setSprite(sprite);
    }

}