import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

public class SilkRoadContest {

    private static final int STEP_DELAY = 400; // ms por paso
    private static final int DAY_DELAY  = 1000; // ms entre días

    public void simulate(int[][] days, boolean slow) {
        for (int d = 1; d <= days.length; d++) {

            int[][] subDays = new int[d][];
            for (int i = 0; i < d; i++) subDays[i] = days[i];

            SilkRoad road = new SilkRoad(subDays);
            Road path = getRoad(road);

            // pintar fondo negro (posiciones)
            if (path != null) {
                for (Rectangle cell : path.getRectangles()) {
                    cell.changeColor("black");
                }
            }

            // IMPORTANT: traer las figuras por delante para evitar que queden tapadas
            bringAllShapesToFront(road);

            if (!slow) {
                road.moveRobots();
                paintEmptyStoresWhite(road);
                invokeUpdateProgressBar(road);
            } else {
                animateRobots(road);
            }

            try {
                Thread.sleep(DAY_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            road.makeInvisible();
            
        }
    }

    /** Animación paso a paso de los robots con fixes para no tapar shapes */
    private void animateRobots(SilkRoad road) {
        Road rd = getRoad(road);

        try {
            boolean moved;
            do {
                moved = false;

                List<Robot> robots = getRobots(road);
                List<Store> stores = getStores(road);

                Robot bestRobot = null;
                Store bestStore = null;
                int bestProfit = Integer.MIN_VALUE;

                // buscar el mejor movimiento
                for (Robot r : robots) {
                    for (Store s : stores) {
                        if (s.getTenges() <= 0) continue;
                        int steps = Math.abs(s.getLocation() - r.getPosition());
                        int profit = s.getTenges() - steps;
                        if (profit >= 0 && profit > bestProfit) {
                            bestProfit = profit;
                            bestRobot = r;
                            bestStore = s;
                        }
                    }
                }

                if (bestRobot != null && bestStore != null) {
                    moved = true;
                    int current = bestRobot.getPosition();
                    int target = bestStore.getLocation();
                    int dir = (target > current) ? 1 : -1;

                    while (current != target) {
                        // pintar negro SOLO si no hay otra tienda y no hay OTRO robot en esa casilla
                        boolean hasStore = isStoreAt(current, stores);
                        boolean otherRobotThere = isOtherRobotAt(current, robots, bestRobot);

                        if (rd != null && current >= 0 && current < rd.getRectangles().size()) {
                            if (!hasStore && !otherRobotThere) {
                                rd.getRectangles().get(current).changeColor("black");
                            }
                        }

                        // avanzar 1 casilla
                        current += dir;
                        bestRobot.setPosition(current, rd);

                        // traer figuras al frente por si el rectangulo quedó encima
                        bringAllShapesToFront(road);

                        // actualizar barra de progreso (tu método privado, invocado por reflexión)
                        invokeUpdateProgressBar(road);

                        Thread.sleep(STEP_DELAY);
                    }

                    // al llegar: robar y dejar la tienda blanca (si tenía tenges)
                    if (bestStore.getTenges() > 0) {
                        int robbed = bestStore.stealAll();
                        bestRobot.addTenges(robbed);
                        bestStore.setColor("white");
                    }

                    bestRobot.recordEarnings(bestProfit);

                    // actualizar barra y volver a asegurar visibilidad
                    invokeUpdateProgressBar(road);
                    bringAllShapesToFront(road);
                }

            } while (moved);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Pinta las tiendas vacías en blanco */
    private void paintEmptyStoresWhite(SilkRoad road) {
        List<Store> stores = getStores(road);
        for (Store s : stores) {
            if (s.getTenges() <= 0) {
                s.setColor("white");
                s.makeVisible(); // asegurar que la tienda quede visible
            }
        }
        bringAllShapesToFront(road);
    }

    /** Trae todas las tiendas y robots al frente (makeVisible) para evitar que queden tapados */
    private void bringAllShapesToFront(SilkRoad road) {
        List<Store> stores = getStores(road);
        for (Store s : stores) {
            try { s.makeVisible(); } catch (Exception ex) {}
        }
        List<Robot> robots = getRobots(road);
        for (Robot r : robots) {
            try { r.makeVisible(); } catch (Exception ex) {}
        }
    }

    /** Verifica si hay tienda en una posición */
    private boolean isStoreAt(int pos, List<Store> stores) {
        for (Store s : stores) {
            if (s.getLocation() == pos) return true;
        }
        return false;
    }

    /** Verifica si hay otro robot (distinto del que se mueve) en la posición pos */
    private boolean isOtherRobotAt(int pos, List<Robot> robots, Robot exclude) {
        for (Robot r : robots) {
            if (r == exclude) continue;
            if (r.getPosition() == pos) return true;
        }
        return false;
    }

    // ---------- métodos por reflexión (igual que tenías) ----------
    @SuppressWarnings("unchecked")
    private List<Robot> getRobots(SilkRoad s) {
        try {
            Method m = SilkRoad.class.getDeclaredMethod("getRobots");
            m.setAccessible(true);
            return (List<Robot>) m.invoke(s);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Store> getStores(SilkRoad s) {
        try {
            Method m = SilkRoad.class.getDeclaredMethod("getStores");
            m.setAccessible(true);
            return (List<Store>) m.invoke(s);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private Road getRoad(SilkRoad s) {
        try {
            Method m = SilkRoad.class.getDeclaredMethod("getRoad");
            m.setAccessible(true);
            return (Road) m.invoke(s);
        } catch (Exception e) {
            return null;
        }
    }

    private void invokeUpdateProgressBar(SilkRoad s) {
        try {
            Method m = SilkRoad.class.getDeclaredMethod("updateProgressBar");
            m.setAccessible(true);
            m.invoke(s);
        } catch (Exception e) {
            // silencioso si falla
        }
    }
}
