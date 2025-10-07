import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas de aceptación para la clase {@link SilkRoad}.
 * 
 * @author  Juan Moreno y Karol Rodriguez
 * @version 1.0
 */
public class SilkRoadAcceptanceTest {

    private SilkRoad road;

    /**
     * Configuración inicial antes de cada escenario.
     */
    @Before
    public void setUp() {
        road = new SilkRoad(10);
        road.makeInvisible();
    }

    /**
     * Escenario de aceptación básico.
     * 
     * Evalúa un control completo con un solo robot y una tienda:
     * Coloca una tienda con valor inicial.
     * Coloca un robot y lo mueve hacia la tienda.
     * Verifica la ganancia total obtenida tras el robo.
     * Reinicia el sistema y comprueba que las ganancias se restablecen a cero.
     * Confirma que las tiendas recuperan su valor original después del reinicio.
     */
    @Test
    public void testScenario1() {
        road.placeStore(3, 40);
        road.placeRobot(1);

        road.moveRobot(1, 2);

        int profitBefore = road.porfit();
        assertEquals(38, profitBefore);

        road.reboot();

        int profitAfter = road.porfit();
        assertEquals(0, profitAfter);

        int[][] stores = road.stores();
        assertEquals(40, stores[0][1]);
    }
    
    /**
     * Escenario de aceptación avanzado.
     * 
     * Evalúa un entorno con múltiples robots y tiendas definidos mediante una
     * configuración inicial de matriz bidimensional.
     * 
     * El correcto movimiento simultáneo de todos los robots.
     * El cálculo acumulado de las ganancias totales.
     * La generación del historial de ganancias por movimiento.
     * El registro de tiendas vaciadas tras los robos.
     * La coherencia de los datos devueltos por los métodos de reporte.
     */
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
            assertTrue("moneda > 1", profitHistory[i].length > 1);
        }
    
        int[][] emptied = road.empitiedStores();
        assertEquals("tiendas = 3", 3, emptied.length);
        for (int i = 0; i < emptied.length; i++) {
            assertTrue("almenos 1 empitiedStore", emptied[i][1] >= 1);
        }
    }
}