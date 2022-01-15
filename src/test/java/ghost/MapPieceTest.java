package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import processing.core.PApplet;

class MapPieceTest {

    @Test 
    public void mapPiecesTest() {
        App testApp = new App();
        PApplet.runSketch(new String[] {"App"}, testApp);
        testApp.setup();
        MapPiece emptyPiece = new EmptyPiece(0, 16);
        MapPiece fruitPiece = new FruitPiece(testApp.loadImage("src/main/resources/fruit.png"), 16, 16);
        MapPiece sodaCan = new SodaCan(testApp.loadImage("src/main/resources/sodacan.png"), 32, 16);
        MapPiece superFruit = new SuperFruit(testApp.loadImage("src/main/resources/superfruit.png"), 48, 16);
        MapPiece wallPiece = new WallPiece(testApp.loadImage("src/main/resources/horizontal.png"), 56, 16);
        assertTrue(emptyPiece.getCoord().equals(new Coordinate(0, 16)));
        assertTrue(fruitPiece.getCoord().equals(new Coordinate(16, 16)));
        assertTrue(sodaCan.getCoord().equals(new Coordinate(32, 16)));
        assertTrue(superFruit.getCoord().equals(new Coordinate(48, 16)));
        assertTrue(wallPiece.getCoord().equals(new Coordinate(56, 16)));   
    }
}