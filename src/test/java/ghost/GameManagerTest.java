package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;

class GameManagerTest {

    @Test 
    public void gameManagerTest() {
        App testApp = new App();
        PApplet.runSketch(new String[] {"App"}, testApp);
        testApp.setup();
        GameManager testManager = testApp.getGameManager();
        testManager.getApptributes();
        testManager.readJson("configTest.json");
        assertEquals(testManager.getMapFilePath(), "mapTest.txt");
        assertEquals(testManager.getCounter(), 0);
        testManager.moveWaka();
        assertEquals(testManager.getWaka().canMove(37, testApp), "YES");
        testManager.fruitCheck();
        assertEquals(testManager.getFruitCount(), 0);
    }
}