package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;
import java.lang.Math;

/**
One of the four Ghost variants in the system.
<p>
Extends from the Ghost Class. 
Acts as the enemy of the Waka. 
Will change between Scatter mode (aiming for the bottom corner) and Chase mode 
(aiming for the space that is double the vector from the Chaser's coordinate to 
two grid spaces ahead of the player).
*/
public class Whim extends Ghost {

    /**
    Constructs a new Whim object. 
    <p>
    The instance stores it's parameters as it's variables.

    @param app The App instance the Whim is part of.
    @param x The starting x coordinate of the Whim.
    @param y the starting y coordinate of the Whim.
    */
    public Whim(PApplet app, int x, int y) {
        super(app, x, y);
    }

    /** 
    Returns the direction that the Whim can move to get closer to it's Scatter target.
    <p>
    The Whim's Scatter Target is the bottom right corner of the map. 
    Will assign the Whim's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers 
        they are in at an intersection. 
    @param gameManager The GameManager instance the Whim is part of.
    @return The integer relating to the direction the Whim 
        should move to get to the bottom right corner.
    */
    public int wayToScatter(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        Coordinate target = gameManager.getApp().getMap().getBottomRight();
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
    Returns the direction that the Whim can move to get closer to it's Chase target.
    <p>
    The Whim's chase target is the space that is double the vector from the space the 
    Chaser Ghost occupies and 2 spaces in front of the player.  
    Will assgin the Whim's target as this space. 
    Takes in a HashMap of possible moves and a GameManager instance.

    @param possibleMoves HashMap of Coordinates and the Direction integers
        they are in at an intersection.
    @param gameManager The GameManager instance the Whim is part of.
    @return The integer relating to the direction the Whim should go to to get to the Chase target. 
    */
    public int wayToChase(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        Coordinate chaseCoord = null;
        ArrayList<Ghost> ghostList = gameManager.getApp().getGhosts();
        for (int i = 0; i < ghostList.size(); i++) {
            if (ghostList.get(i).getClass().toString().equals("class ghost.Chaser")) {
                chaseCoord = ghostList.get(i).getCoord();
                break;
            }
        }
        int wakaX = gameManager.getApp().getWaka().getX() - 4;
        int wakaY = gameManager.getApp().getWaka().getY() - 5;
        int wakaDirection = gameManager.getApp().getWaka().getDirection();
        if (wakaDirection == 37) {
            wakaX = wakaX - (16 * 2);
        } else if (wakaDirection == 38) {
            wakaY = wakaY - (16 * 2);
        } else if (wakaDirection == 39) {
            wakaX = wakaX + (16 * 2);
        } else if (wakaDirection == 40) {
            wakaY = wakaY + (16 * 2);
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
        Coordinate twoInFront = new Coordinate(wakaX, wakaY);
        int xLength = twoInFront.getX() - chaseCoord.getX();
        int yLength = twoInFront.getY() - chaseCoord.getY();
        int targetX = 0;
        int targetY = 0;
        if (twoInFront.getX() > chaseCoord.getX()) {
            targetX = twoInFront.getX() + xLength;
        } else {
            targetX = chaseCoord.getX() + xLength;
        }
        if (twoInFront.getY() > chaseCoord.getY()) {
            targetY = twoInFront.getY() + yLength;
        } else {
            targetY = chaseCoord.getY() + yLength;
        }
        Coordinate target = new Coordinate(targetX, targetY);
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
    Changes the Whim's sprite to it's original image.

    @param app The App instance the Whim is part of.
     */
    public void beBrave(PApplet app) {
        PImage sprite = app.loadImage("src/main/resources/whim.png");
        this.setSprite(sprite);
    }
}