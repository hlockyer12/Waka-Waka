package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

/**
The main class for the game. 
<p>
Will control the generating of the game window and creating the instances of each 
element within the system.
Extends from PApplet and utilises the Processing library.
The goal of the player is to collect all of the fruit in the map without losing all of their lives.
The player will lose a life if it collides with a ghost.
The game ends if the player loses all of their lives.
*/
public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;

    private GameManager gameManager;
    private Waka waka;
    private Map map;
    private ArrayList<Ghost> ghosts;
    private ArrayList<MapPiece> mapPieces;

    /**
    Constructs a new App instance.
    */
    public App() {
        this.gameManager = new GameManager(this);
        this.map = new Map(this.gameManager.getMapFilePath());
        this.ghosts = new ArrayList<Ghost>();
    }

    /**
    Loads all images required for the game to be played.
    <p>
    Will be called before the draw loop begins.
    Outlines the framerate for the game.
    */
    public void setup() {
        frameRate(60);
        this.mapPieces = this.map.makeMapPieces(this.map.getMapList(), this);
        Coordinate playerStart = this.map.getPlayerStart();
        PImage wakaLeft = this.loadImage("src/main/resources/playerLeft.png");
        PImage wakaClosed = this.loadImage("src/main/resources/playerClosed.png");
        this.waka = new Waka(wakaLeft, wakaClosed, playerStart.getX(), playerStart.getY());
        this.ghosts = this.map.getGhosts(this);        
    }

    /**
    Outlines the settings for the App.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
    Displays the images of each element created by the App within the application window.
    */
    public void draw() {
        background(0, 0, 0);
        this.gameManager.draw();
        
    }

    /**
    Returns this instances Waka.

    @return The app's Waka.
    */
    public Waka getWaka() {
        return this.waka;
    }

    /**
    Returns the total number of fruit originally available in the map.

    @return The total number of fruit originally on the map.
    */
    public int getFruitCount() {
        return this.map.getFruitCount();
    }

    /**
    Returns a list of the Ghosts created in the map.

    @return The list of Ghosts in the app.
    */
    public ArrayList<Ghost> getGhosts() {
        return this.ghosts;
    }

    /**
    Returns a list of the MapPieces created within the Map class.

    @return The list of MapPieces for the Map.
    */
    public ArrayList<MapPiece> getMapPieces() {
        return this.mapPieces;
    }

    /**
    Returns this instance's Map object.

    @return The Map object for this instance.
    */
    public Map getMap() {
        return this.map;
    }

    /**
    Returns this instance's GameManager object.

    @return The GameManager object for this instance.
    */
    public GameManager getGameManager() {
        return this.gameManager;
    }

    /**
    Resets the current instances Map, Waka, Ghosts and Fruit.
    */
    public void resetGame() {
        this.mapPieces = this.map.makeMapPieces(this.map.getMapList(), this);
        Coordinate playerStart = this.map.getPlayerStart();
        PImage wakaLeft = this.loadImage("src/main/resources/playerLeft.png");
        PImage wakaClosed = this.loadImage("src/main/resources/playerClosed.png");
        this.waka = new Waka(wakaLeft, wakaClosed, playerStart.getX(), playerStart.getY());
        this.ghosts = this.map.getGhosts(this); 
    }

    /**
    The main method of the App program.
    
    @param args Command line arguments.
    */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}