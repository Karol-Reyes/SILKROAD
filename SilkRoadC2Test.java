import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SilkRoadC2Test {

    private SilkRoad road;

    @Before
    public void setUp() {
        road = new SilkRoad(10);
        road.makeInvisible();
    }

    @Test
    public void testPlaceRobotOk() {
        road.placeRobot(1);
        assertTrue(road.ok());
    }

    @Test
    public void testPlaceRobotFail() {
        road.placeRobot(11);
        assertFalse(road.ok());
    }

    @Test
    public void testMoveRobotOk() {
        road.placeRobot(1);
        road.moveRobot(1, 2);
        assertTrue(road.ok());
    }

    @Test
    public void testMoveRobotFail() {
        road.moveRobot(1, 2);
        assertFalse(road.ok());
    }

    @Test
    public void testRemoveRobotOk() {
        road.placeRobot(2);
        road.removeRobot(2);
        assertTrue(road.ok());
    }

    @Test
    public void testRemoveRobotFail() {
        road.removeRobot(3);
        assertFalse(road.ok());
    }

    @Test
    public void testPlaceStoreOk() {
        road.placeStore(2, 50);
        assertTrue(road.ok());
    }

    @Test
    public void testPlaceStoreFail() {
        road.placeStore(0, 50);
        assertFalse(road.ok());
    }

    @Test
    public void testRemoveStoreOk() {
        road.removeStore(2);
        assertTrue(road.ok());
    }

    @Test
    public void testRemoveStoreFail() {
        road.removeStore(5);
        assertFalse(road.ok());
    }

    @Test
    public void testRebootOk() {
        road.placeRobot(1);
        road.placeStore(2, 20);
        road.reboot();
        assertTrue(road.ok());
    }

    @Test
    public void testPorfitOk() {
        road.placeStore(2, 50);
        road.placeRobot(1);
        road.moveRobot(1, 2);
        assertTrue(road.porfit() >= 0);
    }

    @Test
    public void testStoresOk() {
        road.placeStore(4, 100);
        int[][] info = road.stores();
        assertEquals(4, info[0][0]);
    }

    @Test
    public void testRobotsOk() {
        road.placeRobot(1);
        int[][] info = road.robots();
        assertEquals(1, info[0][0]);
    }
}
