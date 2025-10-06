import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SilkRoadCC2Test {

    private SilkRoad road;

    @Before
    public void setUp() {
        road = new SilkRoad(5);
        road.makeInvisible();
    }

    @Test
    public void testCombinedScenario() {
        road.placeRobot(1);
        road.placeRobot(3);
        road.placeStore(2, 10);
        road.placeStore(4, 20);

        road.moveRobot(1, 2);
        road.moveRobot(3, 4);

        int profit = road.porfit();
        assertEquals(28, profit);

        int[][] stores = road.stores();
        assertEquals(0, stores[0][1]);
        assertEquals(0, stores[1][1]);

        int[][] robots = road.robots();
        assertEquals(2, robots.length);

        road.removeRobot(2);
        assertEquals(1, road.robots().length);

        road.reboot();
        assertEquals(1, road.robots().length);
        assertEquals(10, road.stores()[0][1]);
    }
}
