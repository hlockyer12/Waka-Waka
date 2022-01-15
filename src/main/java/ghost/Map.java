package ghost;

import java.util.*;
import java.io.*;
import processing.core.PImage;
import processing.core.PApplet;

/**
Represents the Map of the game which the player and ghosts will move through.
*/
public class Map {

    private String filepath;
    private HashMap<String, Coordinate> ghostStart;
    private Coordinate playerStart;
    private int fruitCount;
    private Coordinate topLeft;
    private Coordinate topRight;
    private Coordinate bottomLeft;
    private Coordinate bottomRight;
    
    /**
    Constructs a new Map instance from the provided file path.
    <p>
    Reads through the map list and creates the starting coordinates 
    of the player and ghosts.

    @param filepath The path to the Map's text file.
    */
    public Map(String filepath) {
        this.filepath = filepath;
        ArrayList<Integer> xCoords = new ArrayList<Integer>();
        ArrayList<Integer> yCoords = new ArrayList<Integer>();
        for (int i = 0; i < 28; i++) {
            xCoords.add(i*16);
        }
        for (int i = 0; i < 36; i++) {
            yCoords.add(i*16);
        }
        this.ghostStart = new HashMap<String, Coordinate>();
        this.playerStart = null;
        ArrayList<ArrayList<String>> mapList = this.getMapList();
        ArrayList<String> ghostCodes = new ArrayList<String>(Arrays.asList("a", "c", "i", "w"));
        for (int i = 0; i < mapList.size(); i++) {
            for (int j = 0; j < mapList.get(i).size(); j++) {
                String element = mapList.get(i).get(j);
                int x = xCoords.get(j);
                int y = yCoords.get(i);
                if (element.equals("p")) {
                    this.playerStart = new Coordinate(x-4, y-5);
                } else if (ghostCodes.contains(element)) {
                    Coordinate ghostStartSpot = new Coordinate(x-6, y-6);
                    this.ghostStart.put(element, ghostStartSpot);
                }
            }
        }
        this.topLeft = new Coordinate(0, 0);
        this.topRight = new Coordinate(432, 0);
        this.bottomLeft = new Coordinate(0, 560);
        this.bottomRight = new Coordinate(432, 560);
        this.fruitCount = 0;
    }

    /**
    Returns the starting coordinate for the Waka.
    
    @return The Waka's starting coordinate.
    */
    public Coordinate getPlayerStart() {
        return this.playerStart;
    }

    /**
    Returns the starting coordinates and which Ghost type they relate to.
    
    @return HashMap of strings relating to Ghost types and their starting coordinates.
    */
    public HashMap<String, Coordinate> getGhostCoords() {
        return this.ghostStart;
    }

    /**
    Returns the total amount of fruit on the map.
    
    @return The total amount of fruit on the map.
    */
    public int getFruitCount() {
        return this.fruitCount;
    }

    /**
    Returns the coordinate of the top left corner.
    
    @return The coordinate of the top left corner.
    */
    public Coordinate getTopLeft() {
        return this.topLeft;
    }

    /**
    Returns the coordinate of the top right corner.
    
    @return The coordinate of the top right corner.
    */
    public Coordinate getTopRight() {
        return this.topRight;
    }

    /**
    Returns the coordinate of the bottom left corner.
    
    @return The coordinate of the bottom left corner.
    */
    public Coordinate getBottomLeft() {
        return this.bottomLeft;
    }

    /**
    Returns the coordinate of the bottom right corner.
    
    @return The coordinate of the bottom right corner.
    */
    public Coordinate getBottomRight() {
        return this.bottomRight;
    }

    /**
    Creates a 2 dimensional list out of the Map's text file.
    <p>
    Traverses through the text file provided and moves each element into a 2 dimensional list.
    
    @return A 2 dimensional list of each element in the map's text file.
    */
    public ArrayList<ArrayList<String>> getMapList() {
        ArrayList<ArrayList<String>> mapList = new ArrayList<ArrayList<String>>();
        try {
            File mapFile = new File(this.filepath);
            Scanner mapScan = new Scanner(mapFile);
            while (mapScan.hasNextLine()) {
                String[] mapLineArr = mapScan.nextLine().split("");
                ArrayList<String> mapLineList = new ArrayList<String>();
                for (int i = 0; i < mapLineArr.length; i++) {
                    mapLineList.add(mapLineArr[i]);
                } 
                mapList.add(mapLineList);
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return mapList;
    }

    /**
    Creates a list of Ghosts from the HashMap of ghost starting coordinates.
    
    @param app The app which the Ghosts will belong to.
    @return A list of the Ghosts in the map.
    */
    public ArrayList<Ghost> getGhosts(PApplet app) {
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        Coordinate ghostStartSpot = null;
        for (String ghostType : this.ghostStart.keySet()) {
            if (ghostType.equals("a")) {
                ghostStartSpot = this.ghostStart.get("a");
                Ambusher ghost = new Ambusher(app, ghostStartSpot.getX(), ghostStartSpot.getY());
                PImage ambushSprite = app.loadImage("src/main/resources/ambusher.png");
                ghost.setSprite(ambushSprite);
                ghosts.add(ghost);
            } else if (ghostType.equals("c")) {
                ghostStartSpot = this.ghostStart.get("c");
                Chaser ghost = new Chaser(app, ghostStartSpot.getX(), ghostStartSpot.getY());
                PImage chaseSprite = app.loadImage("src/main/resources/chaser.png");
                ghost.setSprite(chaseSprite);
                ghosts.add(ghost);
            } else if (ghostType.equals("i")) {
                ghostStartSpot = this.ghostStart.get("i");
                Ignorant ghost = new Ignorant(app, ghostStartSpot.getX(), ghostStartSpot.getY());
                PImage ignorSprite = app.loadImage("src/main/resources/ignorant.png");
                ghost.setSprite(ignorSprite);
                ghosts.add(ghost);
            } else if (ghostType.equals("w")) {
                ghostStartSpot = this.ghostStart.get("w");
                Whim ghost = new Whim(app, ghostStartSpot.getX(), ghostStartSpot.getY());
                PImage whimSprite = app.loadImage("src/main/resources/whim.png");
                ghost.setSprite(whimSprite);
                ghosts.add(ghost);
            }
        }
        return ghosts;
    }

    /**
    Creates a list of MapPieces which relates to the 2 dimensional provided provided.
    <p>
    Each element in the provided list relates to a certain MapPiece or staring position for the Waka or Ghosts.
    0 - EmptyPiece
    1 - Horizontal WallPiece
    2 - Vertical WallPiece
    3 - Up + Left WallPiece
    4 - Up + Right WallPiece
    5 - Down + Left WallPiece
    6 - Down + Right WallPiece
    7 - FruitPiece
    8 - SuperFruit
    9 - SodaCan
    p - Waka starting position
    a - Ambusher
    c - Chaser
    i - Ignorant
    w - Whim

    @param mapList 2 dimensional list of map piece codes.
    @param app The app which the map will be drawn on.
    @return A list of MapPieces to be used in the app.
    */
    public ArrayList<MapPiece> makeMapPieces(ArrayList<ArrayList<String>> mapList, PApplet app) {
        this.fruitCount = 0;
        ArrayList<MapPiece> mapPieceList = new ArrayList<MapPiece>();
        ArrayList<Integer> xCoords = new ArrayList<Integer>();
        ArrayList<Integer> yCoords = new ArrayList<Integer>();
        for (int i = 0; i < 28; i++) {
            xCoords.add(i*16);
        }
        for (int i = 0; i < 36; i++) {
            yCoords.add(i*16);
        }
        int counter = 0;
        for (int i = 0; i < mapList.size(); i++) {
            for (int j = 0; j < mapList.get(i).size(); j++) {
                String element = mapList.get(i).get(j);
                int x = xCoords.get(j);
                int y = yCoords.get(i);
                MapPiece piece = null;
                ArrayList<String> ghostCodes = new ArrayList<String>(Arrays.asList("a", "c", "i", "w"));
                if (element.equals("0")) {
                    piece = new EmptyPiece(x, y);
                } else if (element.equals("1")) {
                    piece = new WallPiece(app.loadImage("src/main/resources/horizontal.png"), x, y);
                } else if (element.equals("2")) {
                    piece = new WallPiece(app.loadImage("src/main/resources/vertical.png"), x, y);
                } else if (element.equals("3")) {
                    piece = new WallPiece(app.loadImage("src/main/resources/upLeft.png"), x, y);
                } else if (element.equals("4")) {
                    piece = new WallPiece(app.loadImage("src/main/resources/upRight.png"), x, y);
                } else if (element.equals("5")) {
                    piece = new WallPiece(app.loadImage("src/main/resources/downLeft.png"), x, y);
                } else if (element.equals("6")) {
                    piece = new WallPiece(app.loadImage("src/main/resources/downRight.png"), x, y);
                } else if (element.equals("7")) {
                    piece = new FruitPiece(app.loadImage("src/main/resources/fruit.png"), x, y);
                    this.fruitCount++;
                } else if (element.equals("8")) {
                    piece = new SuperFruit(app.loadImage("src/main/resources/superfruit.png"), x, y);
                    this.fruitCount++;
                } else if (element.equals("9")) {
                    piece = new SodaCan(app.loadImage("src/main/resources/sodacan.png"), x, y);
                    this.fruitCount++;
                } else if (element.equals("p")) {
                    piece = new EmptyPiece(x, y);
                } else if (ghostCodes.contains(element)) {
                    piece = new FruitPiece(app.loadImage("src/main/resources/fruit.png"), x, y);
                    this.fruitCount++;
                }
                mapPieceList.add(piece);
                counter++;
            }
        }
        return mapPieceList;
    }
}