package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import processing.core.PApplet;

class GhostsTest {

    @Test 
    public void ghostsTest() {
        App testApp = new App();
        PApplet.runSketch(new String[] {"App"}, testApp);
        testApp.setup();
        testApp.getGameManager().readJson("configTest.json");
        ArrayList<Ghost> ghosts = testApp.getGhosts();
        String[] ghostClasses = new String[4];
        ghostClasses[0] = "class ghost.Ambusher";
        ghostClasses[1] = "class ghost.Chaser";
        ghostClasses[2] = "class ghost.Whim";
        ghostClasses[3] = "class ghost.Ignorant";
        for (int i = 0; i < ghosts.size(); i++) {
            assertTrue(ghosts.get(i).getClass().toString().equals(ghostClasses[i]));
        }
        Ghost ambusher = ghosts.get(0);
        GhostsTest.ambushTest(ambusher, testApp);
        Ghost chaser = ghosts.get(1);
        GhostsTest.chaseTest(chaser, testApp);
        Ghost ignorant = ghosts.get(2);
        GhostsTest.ignorTest(ignorant, testApp);
        Ghost whim = ghosts.get(3);
        GhostsTest.whimTest(ignorant, testApp);
    }

    public static void ambushTest(Ghost ambusher, App testApp) {
        assertEquals(ambusher.getX(), 410);
        assertEquals(ambusher.getY(), 74);
        assertEquals(ambusher.getCoord(), new Coordinate(410, 80));
        HashMap<Coordinate, Integer> possibleMoves = ambusher.intersectionDirection(testApp.getGameManager());
        assertTrue(ambusher.wayToScatter(possibleMoves, testApp.getGameManager()) == 37);
        assertTrue(ambusher.wayToChase(possibleMoves, testApp.getGameManager()) == 40);
        ambusher.destroy(testApp, 0);
        assertTrue(ambusher.getCoord().equals(new Coordinate(418, 548)));
        ambusher.resetPos();
        assertTrue(ambusher.getCoord().equals(new Coordinate(416, 80)));
        ambusher.move(37, 16);
        assertTrue(ambusher.getCoord().equals(new Coordinate(400, 80)));
        assertTrue(ambusher.aligned());
        ambusher.move(37, 1);
        assertFalse(ambusher.aligned());
    }

    public static void chaseTest(Ghost chaser, App testApp) {
        assertEquals(chaser.getX(), 10);
        assertEquals(chaser.getY(), 74);
        assertEquals(chaser.getCoord(), new Coordinate(16, 80));
        HashMap<Coordinate, Integer> possibleMoves = chaser.intersectionDirection(testApp.getGameManager());
        assertTrue(chaser.wayToScatter(possibleMoves, testApp.getGameManager()) == 38);
        assertTrue(chaser.wayToChase(possibleMoves, testApp.getGameManager()) == 37);
        chaser.destroy(testApp, 1);
        assertTrue(chaser.getCoord().equals(new Coordinate(386, 548)));
        chaser.resetPos();
        assertTrue(chaser.getCoord().equals(new Coordinate(16, 80)));
        chaser.move(39, 16);
        assertTrue(chaser.getCoord().equals(new Coordinate(32, 80)));
        assertTrue(chaser.aligned());
        chaser.move(39, 1);
        assertFalse(chaser.aligned());
    }

    public static void ignorTest(Ghost ignorant, App testApp) {
        assertEquals(ignorant.getX(), 410);
        assertEquals(ignorant.getY(), 490);
        assertEquals(ignorant.getCoord(), new Coordinate(416, 496));
        HashMap<Coordinate, Integer> possibleMoves = ignorant.intersectionDirection(testApp.getGameManager());
        assertTrue(ignorant.wayToScatter(possibleMoves, testApp.getGameManager()) == 40);
        assertTrue(ignorant.wayToChase(possibleMoves, testApp.getGameManager()) == 40);
        ignorant.destroy(testApp, 2);
        assertTrue(ignorant.getCoord().equals(new Coordinate(354, 548)));
        ignorant.resetPos();
        assertTrue(ignorant.getCoord().equals(new Coordinate(416, 496)));
        ignorant.move(39, 16);
        assertTrue(ignorant.getCoord().equals(new Coordinate(432, 496)));
        assertTrue(ignorant.aligned());
        ignorant.move(39, 1);
        assertFalse(ignorant.aligned());
    }

    public static void whimTest(Ghost whim, App testApp) {
        assertEquals(whim.getX(), 10);
        assertEquals(whim.getY(), 490);
        assertEquals(whim.getCoord(), new Coordinate(16, 496));
        HashMap<Coordinate, Integer> possibleMoves = whim.intersectionDirection(testApp.getGameManager());
        assertTrue(whim.wayToScatter(possibleMoves, testApp.getGameManager()) == 37);
        assertTrue(whim.wayToChase(possibleMoves, testApp.getGameManager()) == 38);
        whim.destroy(testApp, 3);
        assertTrue(whim.getCoord().equals(new Coordinate(322, 548)));
        whim.resetPos();
        assertTrue(whim.getCoord().equals(new Coordinate(16, 496)));
        whim.move(39, 16);
        assertTrue(whim.getCoord().equals(new Coordinate(32, 496)));
        assertTrue(whim.aligned());
        whim.move(39, 1);
        assertFalse(whim.aligned());
    }
}