package silkroad;

import java.util.Random;

/**
 * NomadStore: green store that moves to a new random position each time it is fully robbed.
 * After each full robbery its refill amount becomes (initialTenges - timesRobbed).
 * When refill reaches 0 the store becomes inactive.
 * Each time it relocates, the SilkRoad progress bar is updated.
 */
public class NomadStore extends Store {

    private int timesRobbed;
    private boolean active;
    private final Road road;
    private final Random random;

    /**
     * Creates a new NomadStore.
     * @param location initial position on the road
     * @param tenges initial amount of money
     * @param road road where the store is located
     * @param silkroad reference to the main SilkRoad controller
     */
    public NomadStore(int location, int tenges, Road road) {
        super(location, tenges, road);
        this.road = road;
        this.timesRobbed = 0;
        this.active = true;
        this.random = new Random();
        this.color = "green";
        this.originalColor = color;
        createShape(road);
    }

    /**
     * Called when a robot attempts to steal from the store.
     * If the store is emptied, it moves to a new location and decreases its refill amount.
     * @param desiredAmount amount attempted to steal
     * @param robotTenges tenges of the robot
     * @return amount actually stolen
     */
    @Override
    public int stealAttempt(int desiredAmount, int robotTenges) {
        int stolen = super.steal(desiredAmount);
        if (stolen > 0 && this.tenges == 0) {
            onEmptiedByRobot();
        }
        return stolen;
    }

    /**
     * Called when a robot steals all money from the store.
     * If the store is emptied, it moves to a new location and decreases its refill amount.
     * @return amount stolen
     */
    @Override
    public int stealAll() {
        int stolen = super.stealAll();
        if (stolen > 0) {
            onEmptiedByRobot();
        }
        return stolen;
    }

    /**
     * Handles the behavior after being fully robbed.
     * Decreases refill amount, moves the store, and updates the SilkRoad progress bar.
     */
    private void onEmptiedByRobot() {
        timesRobbed++;
        int refill = Math.max(0, initialTenges - timesRobbed);

        if (refill <= 0) {
            active = false;
            if (shape != null) shape.makeInvisible();
            return;
        }

        relocateToRandomPositionDifferentFromCurrent();
        this.tenges = refill;
    }

    /**
     * Relocates the store to a new random position on the road and rebuilds its shape.
     */
    private void relocateToRandomPositionDifferentFromCurrent() {
        int total = road.getRectangles().size();
        if (total <= 1) return;

        int newLoc = location;
        int attempts = 0;
        while (newLoc == location && attempts < 50) {
            newLoc = random.nextInt(total);
            attempts++;
        }

        if (shape != null) {
            try { shape.makeInvisible(); } catch (Exception ignored) {}
        }

        this.location = newLoc;
        createShape(road);

        try {
            if (shape != null) {
                shape.changeColor(this.color);
                shape.makeVisible();
            }
        } catch (Exception ignored) {}
    }

    /**
     * Indicates if the store is still active.
     * @return true if active, false if exhausted
     */
    public boolean isActive() {
        return active;
    }
}
