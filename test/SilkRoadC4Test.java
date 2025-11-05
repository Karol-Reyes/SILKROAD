package test;

import silkroad.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias (simples) para tipos de tiendas y robots.
 * JUnit 4 compatible.
 */
public class SilkRoadC4Test {

    private Road road;

    @Before
    public void setUp() {
        road = new Road(10);
    }

    @Test
    public void normalStoreStealAndResupply() {
        NormalStore s = new NormalStore(1, 50, road);
        assertEquals(50, s.getTenges());
        int stolen = s.stealAll();
        assertEquals(50, stolen);
        assertEquals(0, s.getTenges());
        s.resupply();
        assertEquals(50, s.getTenges());
    }

    @Test
    public void autonomousStorePlacedWithinRoadRange() {
        AutonomousStore a = new AutonomousStore(0, 10, road);
        int loc = a.getLocation();
        assertTrue(loc >= 0 && loc < road.getRectangles().size());
        assertEquals(10, a.getTenges());
    }

    @Test
    public void fighterStoreStealAttempt_robotHasMoreTenges_thenSteals() {
        FighterStore f = new FighterStore(2, 5, road);
        RobotNormal r = new RobotNormal(0, road);
        r.addTenges(10);
        int robotTengesBefore = r.getStolenTenges();
        int stolen = f.stealAttempt(5, r.getStolenTenges());
        assertTrue(stolen > 0);
        assertTrue(f.getTenges() < 5 || f.getTenges() == 0);
    }

    @Test
    public void fighterStoreStealAttempt_robotHasLess_thenNoSteal() {
        FighterStore f = new FighterStore(3, 10, road);
        RobotNormal r = new RobotNormal(0, road);
        int stolen = f.stealAttempt(5, r.getStolenTenges());
        assertEquals(0, stolen);
        assertEquals(10, f.getTenges());
    }

    @Test
    public void robotTenderStealsHalf() {
        NormalStore s = new NormalStore(4, 20, road);
        RobotTender t = new RobotTender(0, road);
        t.interactWithStore(s);
        assertEquals(10, t.getStolenTenges());
        assertEquals(10, s.getTenges());
    }

    @Test
    public void robotNormalStealsAllAndStoreBecomesEmpty() {
        NormalStore s = new NormalStore(5, 15, road);
        RobotNormal r = new RobotNormal(0, road);
        r.interactWithStore(s);
        assertEquals(15, r.getStolenTenges());
        assertEquals(0, s.getTenges());
    }

    @Test
    public void robotMovementCountsSteps() {
        RobotNormal r = new RobotNormal(0, road);
        assertEquals(0, r.getSteps());
        r.setPosition(2, road);
        assertEquals(2, r.getSteps());
    }

    @Test
    public void robotNeverBack_and_tender_basicCreation() {
        RobotNeverBack nb = new RobotNeverBack(1, road);
        assertNotNull(nb);
        RobotTender t = new RobotTender(2, road);
        assertNotNull(t);
        t.setPosition(3, road);
        nb.setPosition(4, road);
        assertTrue(t.getPosition() >= 0);
        assertTrue(nb.getPosition() >= 0);
    }
}
