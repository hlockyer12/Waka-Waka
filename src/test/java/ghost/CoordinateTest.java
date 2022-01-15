package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import processing.core.PApplet;

class CoordinateTest {

    @Test 
    public void coordinateTest() {
        Coordinate coordA = new Coordinate(16, 16);
        Coordinate coordB = new Coordinate(16, 32);
        Coordinate coordC = new Coordinate(32, 32);
        Coordinate coordD = new Coordinate(16, 16);
        assertNotNull(coordA);
        assertTrue(coordA.getX() == 16 && coordA.getY() == 16);
        assertTrue(coordA.equals(coordD));
        assertTrue(Coordinate.eucliDist(coordA, coordB) == 16);
        assertTrue(coordC.toIndex() == 58);
    }
}