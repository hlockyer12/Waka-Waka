package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;

class WakaTest {
    @Test
    public void wakaTest() {
        App testApp = new App();
        PApplet.runSketch(new String[] {"App"}, testApp);
        testApp.setup();
        testApp.getGameManager().readJson("configTest.json");
        Waka waka = testApp.getWaka();
        assertEquals(waka.getX(), 202);
        assertEquals(waka.getY(), 315);
        assertEquals(waka.getDirection(), 37);
        waka.move(37, 16);
        assertTrue(waka.aligned());
        assertEquals(waka.canMove(37, testApp), "YES");
        assertEquals(waka.getIndex(), 544);
    }
}