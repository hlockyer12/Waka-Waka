package ghost;

import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import processing.core.PImage;

/**
Manages all of the logic and variables required for the game app to run properly.
*/
public class GameManager {

    private App app;
    private Waka waka;
    private ArrayList<Ghost> ghosts;
    private ArrayList<MapPiece> mapPieces;
    private int wakaCounter;
    private int counter;
    private int frightenCount;
    private int frightenedLength;
    private int endGameCounter;
    private boolean frightened;
    private boolean wavyGhost;
    private int wavyCount;
    private int wavyLength;
    private int speed;
    private int lastKey;
    private int newKey;
    private int lives;
    private int[] modeLengths;
    private String mapFilePath;
    private int fruitCount;
    private boolean endGame;
    private boolean debug;
    private boolean firstPress;
    private int deadGhosts;

    /**
    Constructs a new GameManager object.

    @param app The App object this instance reports to.
    */
    public GameManager(App app) {
        this.app = app;
        this.wakaCounter = 0;
        this.counter = 0;
        this.frightenCount = 0;
        this.endGameCounter = 0;
        this.frightened = false;
        this.wavyGhost = false;
        this.wavyCount = 0;
        this.wavyLength = 300;
        this.lastKey = 37;
        this.newKey = 0;
        this.readJson("config.json");
        this.fruitCount = 0;
        this.endGame = false;
        this.debug = false;
        this.firstPress = false;
        this.deadGhosts = 0;
    }

    /**
    Returns the App object for this instance.
    
    @return The App object for this instance.
    */
    public App getApp() {
        return this.app;
    }

    /**
    Returns the current counter.
    
    @return The current counter.
    */
    public int getCounter() {
        return this.counter;
    }

    /**
    Returns the Waka object for this instance.
    
    @return The Waka object for this instance.
    */
    public Waka getWaka() {
        return this.waka;
    }

    /**
    Returns whether the Ghosts are currently frightened.
    
    @return True if the Ghosts are currently frightened.
        Otherwise False.*/
    public boolean getFrightened() {
        return this.frightened;
    }

    /**
    Returns the current amount of fruit the player has collected.
    
    @return The current amount of fruit the player has collected.
    */
    public int getFruitCount() {
        return this.fruitCount;
    }

    /**
    Retrieves the Waka, Ghosts and MapPieces from the instance's App object.
    */
    public void getApptributes() {
        this.waka = this.app.getWaka();
        this.ghosts = this.app.getGhosts();
        this.mapPieces = this.app.getMapPieces();
    }

    /**
    Returns the file path relating to the map's text file.

    @return The file path relating to the map's text file.
    */
    public String getMapFilePath() {
        return this.mapFilePath;
    }

    /**
    Returns the list of MapPieces for the game.
    
    @return The list of MapPieces for the game.
    */
    public ArrayList<MapPiece> getMapPieces() {
        return this.mapPieces;
    }

    /**
    Controls the flow of logic for each instance.
    <p>
    Calls every method needed to properly run the game app.
    Will move the Waka, all Ghosts, check if Fruit has been collected, check if the 
    Ghosts are frightened or "Wavy", check if there is any collision between any Ghost and
    the Waka, draw all elements of the system, increment all counters within the instance,
    read in the input from the user (checking if debug mode is active) and finally checks
    if the player has won or lost this game.
    */
    public void draw() {
        this.getApptributes();
        List<Integer> keyCodes = new ArrayList<Integer>(Arrays.asList(37, 38, 39, 40));
        if (!this.endGame) {
            this.moveWaka();
            this.moveGhosts();
            this.fruitCheck();
            this.checkFrightenEnd();
            this.checkWavyEnd();
            this.checkCollision();
            this.drawMap();
            this.drawWaka();
            this.drawGhosts();
            this.drawLives();
        }
        this.incrementCount();
        int key = this.app.keyCode;
        if (keyCodes.contains(key)) {
            this.newKey = key;
        } 
        this.debuggerCheck(key);
        this.checkWinOrLoss();
    }

    /**
    Moves this instance's Waka.
    <p>
    Checks if the waka can move with the class's new input and moves the Waka in that direction.
    Otherwise, will check if the Waka can move with the class's previous input, 
    and moves in that direction.
    */
    public void moveWaka() {
        boolean canMoveLast = this.waka.canMove(this.lastKey, this.app);
        boolean canMoveNew = this.waka.canMove(this.newKey, this.app);
        if (canMoveNew && this.waka.aligned()) {
            this.lastKey = this.newKey;
            this.newKey = 0;
            this.waka.move(this.lastKey, this.speed);
        } else if (canMoveLast) {
            this.waka.move(this.lastKey, this.speed);
        }
    }
    /**
    Moves all of the instance's Ghosts.
    <p>
    Loops through the list of Ghosts in the game, checks if they are aligned 
    within the grid and moves them depending on which mode they are currently in.
    If not aligned, the ghosts will continue to move in their current direction.
    */
    public void moveGhosts() {
        for (int i = 0; i < this.ghosts.size(); i++) {
            Ghost ghost = this.ghosts.get(i);
            HashMap<Coordinate, Integer> interDirections = new HashMap<Coordinate, Integer>();
            int directionToGo = 0;
            interDirections = ghost.intersectionDirection(this);
            if (ghost.aligned()) {
                if (this.frightened) {
                    directionToGo = ghost.wayToFrighten(interDirections, this);
                } else {
                    for (int j = 0; j < this.modeLengths.length; j++) {
                        if (this.counter < this.modeLengths[j]) {
                            if ((j + 1) % 2 == 0) {
                                directionToGo = ghost.wayToChase(interDirections, this);      
                            } else if ((j + 1) % 2 == 1) {
                                directionToGo = ghost.wayToScatter(interDirections, this);
                            }
                            break;
                        }
                    }
                }
                ghost.move(directionToGo, this.speed);
            } else {
                ghost.move(ghost.getDirection(), this.speed);
            }  
        }
    }

    /**
    Checks if the player has collected one of the three types of collectables.
    <p>
    Checks what space the Waka currently occupies and what sort of MapPiece is in that space.
    Checks if the player has collected a fruit, super fruit or soda can and increments the collected count if so.
    If a super fruit was collected, frightens all of the Ghosts and sets the frightened count to 0.
    If a soda can was collected, makes all of the Ghosts wavy and sets the wavy count to 0.
    */
    public void fruitCheck() {
        int playerIndex = 0;
        if (this.waka.aligned()) {
            playerIndex = this.waka.getIndex();
            String pieceClass = this.mapPieces.get(playerIndex).getClass().toString();
            if (pieceClass.equals("class ghost.FruitPiece") || pieceClass.equals("class ghost.SuperFruit") 
                || (pieceClass.equals("class ghost.SodaCan"))) {
                int emptyX = this.waka.getX() + 4;
                int emptyY = this.waka.getY() + 5;
                MapPiece empty = new EmptyPiece(emptyX, emptyY);
                this.mapPieces.set(playerIndex, empty);
                this.fruitCount++;
                if (pieceClass.equals("class ghost.SuperFruit")) {
                    for (int i = 0; i < this.ghosts.size(); i++) {
                        this.ghosts.get(i).frighten(this.app);
                    }
                    this.frightened = true;
                    this.frightenCount = 0;
                } else if (pieceClass.equals("class ghost.SodaCan")) {
                    for (int i = 0; i < this.ghosts.size(); i++) {
                        this.ghosts.get(i).wavyTime(this.app);
                    }
                    this.wavyGhost = true;
                    this.wavyCount = 0;
                }
            }
        }
    }

    /**
    Checks if the Ghosts are to remain frightened.
    <p>
    If the current frightened counter is the same as the total frightened length,
    returns the Ghosts back to their normal look and behaviour.
    Otherwise continues.*/
    public void checkFrightenEnd() {
        if (this.frightenCount == this.frightenedLength) {
            for (int i = 0; i < this.ghosts.size(); i++) {
                this.ghosts.get(i).beBrave(this.app);
            }
            this.frightened = false;
            this.frightenCount = 0;
        }
    }

    /**
    Checks if the Ghosts are to remain wavy.
    <p>
    If the current wavy counter is the same as the total wavy length,
    returns the Ghosts back to their normal look.
    Otherwise continues.*/
    public void checkWavyEnd() {
        if (this.wavyCount == this.wavyLength) {
            for (int i = 0; i < this.ghosts.size(); i++) {
                this.ghosts.get(i).beBrave(this.app);
            }
            this.wavyGhost = false;
            this.wavyCount = 0;
        }
    }

    /**
    Checks if there is any collision between the Waka and any Ghosts.
    <p>
    If there is a collision and the Ghosts are not frightened, removes a life 
    from Waka and resets the position of the Waka and all the Ghosts.
    If the Ghosts are frightened, removes that Ghost off the board and continues.
    */
    public void checkCollision() {
        boolean collision = false;
        int wakaXLeft = this.waka.getX() + 7;
        int wakaXRight = wakaXLeft + 23;
        int wakaYTop = this.waka.getY() + 8;
        int wakaYBottom = wakaYTop + 23;
        for (int i = 0; i < this.ghosts.size(); i++) {
            int ghostXLeft = this.ghosts.get(i).getX() + 8;
            int ghostXRight = ghostXLeft + 26;
            int ghostYTop = this.ghosts.get(i).getY() + 8;
            int ghostYBottom = ghostYTop + 26;
            boolean xLeft = (wakaXLeft > ghostXLeft && wakaXLeft < ghostXRight);
            boolean xRight = (wakaXRight > ghostXLeft && wakaXRight < ghostXRight);
            boolean yLeft = (wakaYTop > ghostYTop && wakaYTop < ghostYBottom);
            boolean yRight = (wakaYBottom > ghostYTop && wakaYBottom < ghostYBottom);
            if (xLeft && yLeft) {
                collision = true;
            } else if (xRight && yLeft) {
                collision = true;
            } else if (xLeft && yRight) {
                collision = true;
            } else if (xRight && yRight) {
                collision = true;
            }
            if (collision && !this.frightened) {
                this.lives--;
                this.waka.resetPos(this.app.getMap().getPlayerStart());
                for (int j = 0; j < this.ghosts.size(); j++) {
                    this.ghosts.get(j).resetPos();
                    this.ghosts.get(j).beBrave(this.app);
                }
                this.deadGhosts = 0;
                collision = false;
            } else if (collision && this.frightened){
                this.ghosts.get(i).destroy(this.app, this.deadGhosts);
                this.deadGhosts++;
                collision = false;
            }
        }
    }

    /**
    Draws all elements of the map to the window.
    */
    public void drawMap() {
        for (int i = 0; i < this.mapPieces.size(); i++) {
            if (this.mapPieces.get(i) == null) {
                continue;
            } else if (this.mapPieces.get(i).getClass().toString().equals("class ghost.EmptyPiece")) {
                continue;
            } else {
                this.mapPieces.get(i).draw(this.app);
            }
        }
    }

    /**
    Draws the Waka to the window.
    <p>
    Changes between the open and closed image of the Waka every 8 frames.
    */
    public void drawWaka() {
        if (this.wakaCounter <= 8) {
            this.waka.changeToClosed();
            this.waka.draw(this.app);
        } else if (this.wakaCounter > 8 && this.wakaCounter <= 16) {
            this.waka.setSprite(this.lastKey, this.app);
            this.waka.draw(this.app);
        } else {
            this.waka.setSprite(this.lastKey, this.app);
            this.waka.draw(this.app);
            this.wakaCounter = 0;
        }
    }

    /**
    Draws all of the Ghosts to the game window.
    */
    public void drawGhosts() {
        for (int i = 0; i < this.ghosts.size(); i++) {
            if (this.ghosts.get(i) == null) {
                continue;
            } else {
                this.ghosts.get(i).draw(this.app);
            }
        }
    }

    /**
    Draws the player's remaining lives to the game window.
    */
    public void drawLives() {
        PImage life = this.app.loadImage("src/main/resources/playerRight.png");
        for (int i = 0; i < this.lives; i++) {
            this.app.image(life, (8 + (i * 28)), 544);
        }
    }

    /**
    Increments each of the active counters in the game.
    */
    public void incrementCount() {
        this.wakaCounter++;
        this.counter++;
        if (this.frightened) {
            this.frightenCount++;
        }
        if (this.endGame) {
            this.endGameCounter++;
        }
        if (this.wavyGhost) {
            this.wavyCount++;
        }
    }

    /**
    Checks if Debug Mode has been activated and calls the debug method if so.

    @param key The most recent key pressed by the user.
    */
    public void debuggerCheck(int key) {
        if (key == 0 && !this.debug) {
            this.firstPress = true;
        } else if (key == 0 && this.debug) {
            this.firstPress = false;
        }
        if (key == 32 && this.firstPress) {
            this.debug = true;
        } else if (key == 32 && !this.firstPress) {
            this.debug = false;
        }
        if (this.debug) {
            this.debugMode(this.app);
        }
    }

    /**
    Checks if the player has won or lost the game.
    <p>
    Resets the game if an endgame screen has been displayed for 10 seconds.
    */
    public void checkWinOrLoss() {
        if (this.fruitCount == this.app.getMap().getFruitCount()) {
            this.winner();
            this.endGame = true;
        } else if (this.lives == 0) {
            this.loser();
            this.endGame = true;
        }
        if (this.endGameCounter == 600 && this.endGame) {
            this.resetGame();
        }
    }

    /**
    Resets the current game.
    */
    public void resetGame() {
        this.endGame = false;
        this.endGameCounter = 0;
        this.counter = 0;
        this.wavyCount = 0;
        this.wavyGhost = false;
        this.fruitCount = 0;
        this.deadGhosts = 0;
        this.readJson("config.json");
        this.app.resetGame();

    }

    /**
    Reads in parameters from the provided JSON file.
    <p>
    Will assign this instances speed, mapFilePath, lives, frightenedLength and modeLengths.
    
    @param filePath The file path to the JSON file needed.
    */
    public void readJson(String filePath) {
        JSONParser configParse = new JSONParser();
        try {
            Object obj = configParse.parse(new FileReader(filePath));
            JSONObject configJson = (JSONObject) obj;
            this.speed = (int) (long) configJson.get("speed"); //longSpeed;
            this.mapFilePath = (String) configJson.get("map");
            this.lives = (int) (long) configJson.get("lives");
            this.frightenedLength = 60 * ((int) (long) configJson.get("frightenedLength"));
            JSONArray modeLengthsJson = (JSONArray) configJson.get("modeLengths");
            int[] modeLengths = new int[8];
            for (int i = 0; i < 8; i++) {
                if (i == 0) {
                    modeLengths[i] = 60 * ((int) (long) modeLengthsJson.get(i));
                } else {
                    modeLengths[i] = (60 * ((int) (long) modeLengthsJson.get(i))) + modeLengths[i - 1];
                }
            }
            this.modeLengths = modeLengths;
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException f) {
            return;
        } catch (ParseException g) {
            return;
        }
    }

    /**
    Draws the win screen to the window.
    */
    public void winner() {
        this.app.fill(0, 0, 0);
        this.app.rect(-1, -1, 450, 578);
        PImage youWin = this.app.loadImage("src/main/resources/youwin.png");
        this.app.image(youWin, 124, 272);
    }

    /**
    Draws the lose screen to the window.
    */
    public void loser() {
        this.app.fill(0, 0, 0);
        this.app.rect(-1, -1, 450, 578);
        PImage gameOver = this.app.loadImage("src/main/resources/gameover.png");
        this.app.image(gameOver, 168, 280);
    }

    /**
    Draws a debug line from each Ghost to their current target.
    <p>
    Will not draw line if the ghost is dead or frightened.

    @param app The app to draw the debug lines to.
    */
    public void debugMode(App app) {
        if (this.frightened) {
            return;
        }
        app.stroke(256, 256, 256);
        for (int i = 0; i < this.ghosts.size(); i++) {
            Ghost ghost = this.ghosts.get(i);
            if (ghost.canGhostMove()) {
                int ghostX = ghost.getCoord().getX() + 8;
                int ghostY = ghost.getCoord().getY() + 8;
                int targetX = ghost.getTarget().getX() + 8;
                int targetY = ghost.getTarget().getY() + 8;
                app.line(ghostX, ghostY, targetX, targetY);
            }
        }
    }
  
}