package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

/**
An abstract class to extend into the 4 ghost subclasses.
<p>
Acts as the enemy of the Waka.
Will change between Chase mode and Scatter mode.
*/
public abstract class Ghost {

    public PImage sprite;
    public int x;
    public int y;
    public Coordinate coord;
    public int direction;
    public int cameFrom;
    public Coordinate startingCoord;
    public Coordinate target;
    public boolean canMove;

    /**
    Constructs a new Ghost.
    <p>
    Cannot be implemented as a Ghost object, needs a subclass to be constructed.
    
    @param app The app which the ghost belongs to.
    @param x The X axis value of the Ghost's starting position.
    @param y The Y axis value of the Ghost's starying position.
    */
    public Ghost(PApplet app, int x, int y) {
        this.sprite = app.loadImage("src/main/resources/ghost.png");
        this.x = x;
        this.y = y;
        this.coord = new Coordinate(this.x + 6, this.y + 6);
        this.direction = 39;
        this.cameFrom = this.opposite(this.direction);
        this.startingCoord = new Coordinate(this.x + 6, this.y + 6);
        this.target = this.startingCoord;
        this.canMove = true;
    }

    /**
    Returns this Ghost's X axis value.
    
    @return The Ghost's X axis value.
    */
    public int getX() {
        return this.x;
    }

    /**
    Returns this Ghost's Y axis value.
    
    @return The Ghost's Y axis value.
    */
    public int getY() {
        return this.y;
    }

    /**
    Returns this Ghost's coordinate.
    
    @return The Ghost's Y coordinate.
    */
    public Coordinate getCoord() {
        return this.coord;
    }

    /**
    Returns this Ghost's target's coordinate.
    
    @return The Ghost's target's coordinate.
    */
    public Coordinate getTarget() {
        return this.target;
    }

    /**
    Returns this Ghost's current direction.
    
    @return The integer value of the Ghost's current direction.
    */
    public int getDirection() {
        return this.direction;
    }

    /**
    Returns the direction the Ghost came from.
    
    @return The integer value of the direction the Ghost came from.
    */
    public int getCameFrom() {
        return this.cameFrom;
    }

    /**
    Returns if the Ghost can currently move.
    
    @return True if the Ghost can move, False otherwise.
    */
    public boolean canGhostMove() {
        return this.canMove;
    }

    /**
    Sets the Ghost's image to the one provided.
    
    @param sprite The image the Ghost's sprite will be changed to.
    */
    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
    Draws the Ghost's sprite to the app window.
    
    @param app The app which the Ghost will be drawn to.
    */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    /**
    Moves the Ghost.
    
    @param direction The integer value for the Ghost to move.
    @param speed The speed at which to move the Ghost.
    */
    public void move(int direction, int speed) {
        if (this.canMove) {
            if (direction == 38){ // Moving Up
                this.y = this.y - speed;
            } else if (direction == 40) { // Moving Down
                this.y = this.y + speed;
            } else if (direction == 39) { // Moving Right
                this.x = this.x + speed;
            } else if (direction == 37) { // Moving Left
                this.x = this.x - speed;
            }
            this.direction = direction;   
            this.cameFrom = this.opposite(direction);
            this.coord = new Coordinate((this.x + 6), (this.y + 6));
        }
    }

    /**
    Resets the Ghost back to it's startting position.
    <p>
    Allows the Ghost to move again.
    */
    public void resetPos() {
        this.x = this.startingCoord.getX() - 6;
        this.y = this.startingCoord.getY() - 6;
        this.coord = this.startingCoord;
        this.canMove = true;
    }

    /**
    Checks if the Ghost is aligned with the map grid.
    
    @return True if the Ghost is aligned, False otherwise.
    */
    public boolean aligned() {
        if ((this.x + 6) % 16 == 0 && (this.y + 6) % 16 == 0) {
            return true;
        }
        return false;
    }

    /**
    Returns the integer index relating to the Ghost's current position in the map's MapPiece list.

    @return The integer value of the Ghost's location in the MapPiece list.
    */
    public int getIndex() {
        int index = (((this.y + 6) / 16) * 28) + ((this.x + 6) / 16);
        return index;
    }

    /**
    Creates a HashMap of possible directions the Ghost can move at an intersection.
    <p>
    Will not allow the Ghost to travel back the way it came from.
    
    @param gameManager The GameManager the ghost is part of.
    @return A HashMap of coordinates and integers relating to the directions the Ghost can possibly move.
    */
    public HashMap<Coordinate, Integer> intersectionDirection(GameManager gameManager) {
        int cursor = this.getIndex();
        ArrayList<Integer> directions = new ArrayList<Integer>(Arrays.asList(37, 38, 39, 40));
        HashMap<Coordinate, Integer> possibleMoves = new HashMap<Coordinate, Integer>();
        MapPiece cursorPiece = null;
        for (int i = 0; i < directions.size(); i++) {
            if (directions.get(i) == this.cameFrom) {
                 continue;
            } else {
                if (directions.get(i) == 37) {
                    cursorPiece = gameManager.getMapPieces().get(cursor - 1);
                } else if (directions.get(i) == 38) {
                    cursorPiece = gameManager.getMapPieces().get(cursor - 28);
                } else if (directions.get(i) == 39) {
                    cursorPiece = gameManager.getMapPieces().get(cursor + 1);
                } else if (directions.get(i) == 40) {
                    cursorPiece = gameManager.getMapPieces().get(cursor + 28);
                }
                if (!cursorPiece.getClass().toString().equals("class ghost.WallPiece")) {
                    possibleMoves.put(cursorPiece.getCoord(), directions.get(i));
                }
            }
        }
        return possibleMoves;
    }

    /**
    Returns the integer direction value towards the Ghost's Scatter target.

    @param possibleMoves HashMap of Coordinates and the Direction integers 
        they are in at an intersection. 
    @param gameManager The GameManager instance the Ghostr is part of.
    @return The integer relating to the direction the Ghost should move towards the Scatter target.
    */
    public abstract int wayToScatter(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager);

    /**
    Returns the integer direction value towards the Ghost's Chase target.

    @param possibleMoves HashMap of Coordinates and the Direction integers
        they are in at an intersection.
    @param gameManager The GameManager instance the Ghost is part of.
    @return The integer relating to the direction the Ghost should go to to get to the Chase target. 
    */
    public abstract int wayToChase(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager);

    /**
    Returns a random direction integer out of the possible directions the Ghost can move.

    @param possibleMoves HashMap of Coordinates and the Direction integers
        they are in at an intersection.
    @param gameManager The GameManager instance the Ghost is part of.
    @return A random direction out of the possible directions the Ghost can move.
    */
    public int wayToFrighten(HashMap<Coordinate, Integer> possibleMoves, GameManager gameManager) {
        int length = possibleMoves.size();
        if (length == 0) {
            return this.direction;
        }
        Random rand = new Random();
        int randomMoveIndex = rand.nextInt(length);
        Collection<Integer> possibleMoveList = possibleMoves.values();
        Integer[] x = possibleMoveList.toArray(new Integer[length]);
        int directionToGo = x[randomMoveIndex];
        return directionToGo;
    }

    /**
    Frightens the Ghost.
    <p>
    Changes the Ghost's image to the frightened one if it can move.
    @param app The app the Ghost is part of.
    */
    public void frighten(PApplet app) {
        if (this.canMove) {
            PImage scared = app.loadImage("src/main/resources/frightened.png");
            this.setSprite(scared);
        }
    }

    /**
    Changes the Ghost's image back to it's normal sprite.
    
    @param app The app this Ghost is part of.
    */
    public abstract void beBrave(PApplet app);

    /**
    Changes the Ghost's image to wavy sprite.
    <p>
    @param app The app this Ghost is part of.
    */
    public void wavyTime(PApplet app) {
        if (this.canMove) {
            PImage wavy = app.loadImage("src/main/resources/wavy.png");
            this.setSprite(wavy);
        }
    }

    /**
    Moves the Ghost to the bottom right corner.
    <p>
    Puts the Ghost into Ghost jail if it is frightened and collides with the Waka.
    Stops the Ghost from moving and changes the sprite to it's normal image.
    
    @param app The app this Ghost is part of.
    @param numberDead The number of Ghosts currently in Ghost jail.
    */
    public void destroy(PApplet app, int numberDead) {
        this.x = 412 - (32 * numberDead);
        this.y = 542;
        this.coord = new Coordinate(this.x + 6, this.y + 6);
        this.canMove = false;
        this.beBrave(app);
    }

    /**
    Returns the opposite direction of the one provided.
    
    @param direction The direction one is travelling.
    @return The opposite direction to the one provided.
    */
    public int opposite(int direction) {
        if (direction == 37) {
            return 39;
        } else if (direction == 38) {
            return 40;
        } else if (direction == 39) {
            return 37;
        } else {
            return 38;
        }
    }
}