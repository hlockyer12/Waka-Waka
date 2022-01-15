package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
Represents the player's token in the Map.
<p>
The player will move around the map, collecting fruit and avoiding the Ghosts.*/
public class Waka {

    private int x;
    private int y;
    private PImage sprite;
    private PImage spriteClosed;
    private Coordinate coord;
    private int direction;

    /**
    Constructs a new Waka.
    
    @param sprite The starting sprite of the Waka.
    @param spriteClosed The closed version of the Waka.
    @param x The X axis value of the Waka's starting position.
    @param y The Y axis value of the Waka's staring position.
    */
    public Waka(PImage sprite, PImage spriteClosed, int x, int y) {
        this.sprite = sprite;
        this.spriteClosed = spriteClosed;
        this.x = x;
        this.y = y;
        this.coord = new Coordinate((this.x + 4), (this.y + 5));
        this.direction = 37;
    }
    /**
    Returns this Waka's X axis value.
    
    @return The Waka's X axis value.
    */
    public int getX() {
        return this.x;
    }

    /**
    Returns this Waka's Y axis value.
    
    @return The Waka's Y axis value.
    */
    public int getY() {
        return this.y;
    }

    /**
    Returns this Waka's coordinate.
    
    @return The Waka's Y coordinate.
    */
    public Coordinate getCoord() {
        return this.coord;
    }

    /**
    Returns this Waka's current direction.
    
    @return The integer value of the Waka's current direction.
    */
    public int getDirection() {
        return this.direction;
    }

    /**
    Sets the Waka's position to the coordinate provided.

    @param coord The position to set the Waka to.
    */
    public void resetPos(Coordinate coord) {
        this.x = coord.getX();
        this.y = coord.getY();
    }

    /**
    Draws the Waka's current sprite to the app.
    
    @param app The app which the Waka will be drawn to.
    */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    /**
    Change the Waka's image to the closed sprite.
    */
    public void changeToClosed() {
        this.sprite = this.spriteClosed;
    }

    /**
    Sets the sprite to the sprite that relates to the direction provided.
    
    @param direction The direction relating to the sprite to be assigned.
    @param app The app to which the sprite will be drawn to.
    */
    public void setSprite(int direction, PApplet app) {
        if (direction == 38){ // Moving Up
            this.sprite = app.loadImage("src/main/resources/playerUp.png");
        } else if (direction == 40) { // Moving Down
            this.sprite = app.loadImage("src/main/resources/playerDown.png");
        } else if (direction == 39) { // Moving Right
            this.sprite = app.loadImage("src/main/resources/playerRight.png");
        } else if (direction == 37) { // Moving Left
            this.sprite = app.loadImage("src/main/resources/playerLeft.png");
        }
            
    }

    /**
    Move the Waka in the direction and speed provided
    <p>
    There are 4 integers that relate to cardinal directions.
    <p> 37 - Left
    <p> 38 - Up
    <p> 39 - Right
    <p> 40 - Down
    
    @param direction The direction integer provided.
    @param speed The speed of movement.
    */
    public void move(int direction, int speed) {
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
        this.coord = new Coordinate((this.x + 4), (this.y + 5));
    }

    /**
    Returns if the Waka can move in the direction provided.

    @param direction The direction to check if the Waka can move into.
    @param app The app the Waka belongs to.
    @return True if the space in the direction is an EmptyPiece, 
        FruitPiece, SuperFruit or SodaCan, False otherwise.
    */
    public boolean canMove(int direction, App app) {
        if (direction == 0) {
            return false;
        }
        double x = (double) this.x;
        double y = (double) this.y;
        double currentPosY = (y + 5) / 16;
        double currentPosX = (x + 4) / 16;
        double modX = currentPosX % 1.0;
        double modY = currentPosY % 1.0;
        if (direction == 37 && modX != 0) {
            currentPosX = Math.ceil(currentPosX);
        } else if (direction == 38 && modY != 0) {
            currentPosY = Math.ceil(currentPosY);
        } else if (direction == 39 && modX != 0) {
            currentPosX = Math.floor(currentPosX);
        } else if (direction == 40 && modY != 0) {
            currentPosY = Math.floor(currentPosY);
        }
        double currentPos = (28 * currentPosY) + currentPosX;
        double cursorD = 0;
        int cursor = 0;
        if (direction == 39) {
            cursorD = currentPos + 1.0;
            cursor = (int) cursorD;
        } else if (direction == 37) {
            cursorD = currentPos - 1.0;
            cursor = (int) cursorD;
        } else if (direction == 38) {
            cursorD = currentPos - 28.0;
            cursor = (int) cursorD;
        } else if (direction == 40) {
            cursorD = currentPos + 28.0;
            cursor = (int) cursorD;
        }
        if (cursor < 0 || cursor > 1007) {
            return false;
        }
        MapPiece piece = app.getMapPieces().get(cursor);
        if (piece == null) {
            return false;
        } else if (piece.getClass().toString().equals("class ghost.WallPiece")) {
            return false;
        } else {
            return true;
        }
    }

    /**
    Returns if the Waka is aligned in the grid.
    
    @return True is the Waka is aligned, False otherwise.
    */
    public boolean aligned() {
        if ((this.x + 4) % 16 == 0 && (this.y + 5) % 16 == 0) {
            return true;
        }
        return false;
    }

    /**
    Returns the index which the Waka is currently in in the MapPiece list.

    @return The integer index which space the Waka currently occupies.
    */
    public int getIndex() {
        if (!this.aligned()) {
            return 0;
        }
        int index = (((this.y + 5) / 16) * 28) + ((this.x + 4) / 16);
        return index;
    }
}