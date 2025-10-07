import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para la clase {@link SilkRoad}.
 * 
 * @author Juan Moreno y Karol Rodriguez  
 * @version 1.0
 */
public class SilkRoadC2Test {
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
}