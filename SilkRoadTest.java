import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SilkRoadTest {

    private SilkRoad road;

    @Before
    public void setUp() {
        road = new SilkRoad(20); // tablero de 20 casillas
    }

    @After
    public void tearDown() {
        road = null;
    }

    // 1) Crear entidades y validar que existan robots/tiendas
    @Test
    public void shouldCreateEntities() {
        road.setupEntities(5);
        int total = road.getRobots().size() + road.getStores().size();
        assertEquals("Debe haber 5 entidades creadas", 5, total);
    }

    // 2) Verificar que se puede mover un robot
    @Test
    public void shouldMoveRobot() {
        road.setupEntities(3);
        if (!road.getRobots().isEmpty()) {
            int id = 1;
            int oldLocation = road.getRobots().get(0).getPosition();
            road.moveRobot(id, 5);
            int newLocation = road.getRobots().get(0).getPosition();
            assertNotEquals("El robot debe haber cambiado de casilla", oldLocation, newLocation);
        }
    }

    // 3) Eliminar una tienda y comprobar que la lista se reduce
    @Test
    public void shouldRemoveStore() {
        road.setupEntities(5);
        int before = road.getStores().size();
        if (before > 0) {
            int location = road.getStores().get(0).getLocation() + 1;
            road.removeStore(location);
            int after = road.getStores().size();
            assertEquals("Debe eliminarse una tienda", before - 1, after);
        }
    }

    // 4) Verificar que los robots regresan al origen con tenges en 0
    @Test
    public void shouldResetRobotsOnReturn() {
        road.setupEntities(3);
        if (!road.getRobots().isEmpty()) {
            road.getRobots().get(0).addTenges(50);
            road.returnRobots();
            assertEquals("Los robots deben volver con 0 tenges", 0, road.getRobots().get(0).getTenges());
        }
    }

    // 5) Verificar que no explota al robar una tienda vacía
    @Test
    public void shouldHandleEmptyStoreSteal() {
        road.setupEntities(3);
        if (!road.getStores().isEmpty()) {
            Store store = road.getStores().get(0);
            int first = store.stealAll();
            int second = store.stealAll();
            assertTrue("El primer robo debe ser >= 0", first >= 0);
            assertEquals("El segundo robo debe ser 0 porque ya está vacía", 0, second);
        }
    }
}

