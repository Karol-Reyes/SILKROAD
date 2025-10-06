import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SilkRoadAcceptanceTest {

    private SilkRoad road;

    @Before
    public void setUp() {
        road = new SilkRoad(10);
        road.makeInvisible();
    }

    @Test
    public void testScenario1() {
        road.placeStore(3, 40);
        road.placeRobot(1);

        road.moveRobot(1, 3);

        int profitBefore = road.porfit();
        assertEquals(38, profitBefore);

        road.reboot();

        int profitAfter = road.porfit();
        assertEquals(0, profitAfter);

        int[][] stores = road.stores();
        assertEquals(40, stores[0][1]);
        
    }
    
    @Test
    public void testScenario2() {

        int[][] config = {{1, 1}, {1, 4}, {2, 2, 10}, {2, 3, 20}, {2, 5, 30}};
        SilkRoad road = new SilkRoad(config);
        road.makeInvisible();
    
        road.moveRobots();
    
        int totalProfit = road.porfit();
        assertTrue("Profit +", totalProfit > 0);
    
        int[][] profitHistory = road.profitPerMove();
        assertEquals("robots = 2", 2, profitHistory.length);
        for (int i = 0; i < profitHistory.length; i++) {
            assertTrue("moneda > 1",profitHistory[i].length > 1);
        }
    
        int[][] emptied = road.empitiedStores();
        assertEquals("tiendas = 3", 3, emptied.length);
        for (int i = 0; i < emptied.length; i++) {
            assertTrue("almenos 1 empitiedStore", emptied[i][1] >= 1);
        }
    }
}