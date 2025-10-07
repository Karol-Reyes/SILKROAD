import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase {@link SilkRoad},
 * que evalúa un escenario combinado con múltiples acciones encadenadas.

 * @author  Juan Moreno y Karol Rodriguez
 * @version 1.0
 */
public class SilkRoadCC2Test {

    private SilkRoad road;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @Before
    public void setUp() {
        road = new SilkRoad(8);
        road.makeInvisible();
    }

    /**
     * Prueba completa que combina múltiples operaciones de {@code SilkRoad}.
     * 
     * Este escenario evalúa:
     * Creación de varios robots y tiendas.
     * Movimiento de robots y cálculo de ganancias por robo.
     * Validación del total de ganancias acumuladas.
     * Verificación del vaciado de tiendas tras ser robadas.
     * Eliminación y conteo correcto de robots.
     * Comportamiento esperado tras el reinicio.
     */
    @Test
    public void accordingMorenoRodriguezShould1() {
        road.placeRobot(1);
        road.placeRobot(3);
        road.placeStore(2, 10);
        road.placeStore(4, 20);

        road.moveRobot(1, 1);
        road.moveRobot(3, 1);

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
    
    /**
     * Escenario combinado con movimientos y reinicios .
     * 
     * Este test evalúa:
     * Creación de robots y tiendas.
     * Movimientos con robo de monedas.
     * Reinicio de sistema y recuperación de valores originales.
     * Consistencia del conteo de robots y ganancias totales.
     */
    @Test
    public void accordingMorenoRodriguezShould2() {
        road.placeRobot(1);
        road.placeRobot(2);
        road.placeStore(3, 30);
        road.placeStore(5, 50);

        road.moveRobot(1, 2);
        road.moveRobot(2, 3);

        assertTrue(road.porfit() > 0);

        road.reboot();
        assertEquals(2, road.robots().length);
        assertEquals(30, road.stores()[0][1]);
        assertEquals(50, road.stores()[1][1]);

        road.moveRobot(1, 2);
        road.moveRobot(2, 3);
        assertTrue(road.porfit() >= 40);
    }
    
    /**
     * Escenario con múltiples robots y tiendas.
     * 
     * Evalúa la sincronización de movimientos
     * Cálculo acumulado de ganancias
     * Restauración del estado tras reiniciar el sistema.
     */
    @Test
    public void accordingMorenoRodriguezShould3() {
        road.placeRobot(1);
        road.placeRobot(4);
        road.placeRobot(6);

        road.placeStore(2, 15);
        road.placeStore(5, 25);
        road.placeStore(7, 40);

        road.moveRobot(1, 1);
        road.moveRobot(4, 1);
        road.moveRobot(6, 1);

        assertTrue(road.porfit() > 0);

        int[][] stores = road.stores();
        assertEquals(0, stores[0][1]);
        assertEquals(0, stores[1][1]);
        assertEquals(0, stores[2][1]);

        road.reboot();
        assertEquals(3, road.robots().length);
        assertEquals(15, road.stores()[0][1]);
        assertEquals(25, road.stores()[1][1]);
        assertEquals(40, road.stores()[2][1]);
    }
}