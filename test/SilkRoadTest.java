package test;
import silkroad.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase {@link SilkRoad}.
 * 
 * @author Juan Moreno y Karol Rodriguez  
 * @version 1.0
 */
public class SilkRoadTest {
    private SilkRoad road;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @Before
    public void setUp() {
        road = new SilkRoad(10);
        road.makeInvisible();
    }

    /**
     * Prueba que verifica la correcta colocación de un robot en el camino.
     */
    @Test
    public void shoulPlaceRobot() {
        road.placeRobot(1);
        assertTrue(road.ok());
    }

    /**
     * Prueba que verifica el movimiento exitoso de un robot ya colocado.
     */
    @Test
    public void shouldMoveRobot() {
        road.placeRobot(1);
        road.moveRobot(1, 1);
        assertTrue(road.ok());
    }

    /**
     * Prueba que valida la eliminación correcta de un robot existente.
     */
    @Test
    public void shouldRemoveRobot() {
        road.placeRobot(2);
        road.removeRobot(2);
        assertTrue(road.ok());
    }

    /**
     * Prueba que valida la correcta creación de una tienda con cierta cantidad de monedas.
     */
    @Test
    public void shouldPlaceStore() {
        road.placeStore(2, 50);
        assertTrue(road.ok());
    }

    /**
     * Prueba que verifica la eliminación correcta de una tienda existente.
     */
    @Test
    public void shouldRemoveStore() {
        road.removeStore(2);
        assertTrue(road.ok());
    }

    /**
     * Prueba que valida el reinicio completo del sistema (robots y tiendas).
     */
    @Test
    public void shouldReboot() {
        road.placeRobot(1);
        road.placeStore(2, 20);
        road.reboot();
        assertTrue(road.ok());
    }

    /**
     * Prueba que verifica que las ganancias totales sean calculadas correctamente.
     */
    @Test
    public void shouldProfit() {
        road.placeStore(2, 50);
        road.placeRobot(1);
        road.moveRobot(1, 1);
        assertTrue(road.porfit() >= 0);
    }

    /**
     * Prueba que obtiene la información de las tiendas registradas.
     * Se espera que la tienda colocada esté registrada en la posición correcta.
     */
    @Test
    public void shouldStores() {
        road.placeStore(4, 100);
        int[][] info = road.stores();
        assertEquals(4, info[0][0]);
    }

    /**
     * Prueba que obtiene la información de los robots colocados.
     * Se espera que el robot esté registrado en la posición correcta.
     */
    @Test
    public void shouldRobots() {
        road.placeRobot(1);
        int[][] info = road.robots();
        assertEquals(1, info[0][0]);
    }
    
    // ------------------------- PRUEBAS DE NO SHOULD ------------------------------

    /**
     * Prueba que intenta colocar un robot fuera del rango válido.
     * Debe fallar y dejar el estado del sistema como no válido.
     */
    @Test
    public void noShouldPlaceRobot() {
        road.placeRobot(11);
        assertFalse(road.ok());
    }
    
    /**
     * Prueba que intenta mover un robot inexistente.
     * Debe fallar y dejar el estado del sistema como no válido.
     */
    @Test
    public void noShouldMoveRobot() {
        road.moveRobot(1, 2);
        assertFalse(road.ok());
    }
    
    /**
     * Prueba que intenta eliminar un robot que no existe.
     * Debe fallar y dejar el estado del sistema como no válido.
     */
    @Test
    public void noShouldRemoveRobot() {
        road.removeRobot(3);
        assertFalse(road.ok());
    }
    
    /**
     * Prueba que intenta colocar una tienda en una posición inválida (por debajo del rango permitido).
     * Debe fallar y dejar el estado del sistema como no válido.
     */
    @Test
    public void noShouldPlaceStore() {
        road.placeStore(0, 50);
        assertFalse(road.ok());
    }
    
    /**
     * Prueba que intenta eliminar una tienda inexistente.
     * Debe fallar y dejar el estado del sistema como no válido.
     */
    @Test
    public void noShouldRemoveStore() {
        road.removeStore(5);
        assertFalse(road.ok());
    }
    
    //////////////////////////////////// C C 2 ////////////////////////////////////////

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
    
    ///////////////////////////////////////// A C C E P T A N C E /////////////////////////

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