package silkroad;
import shapes.*;

import java.util.*;
import javax.swing.JOptionPane;

public class SilkRoadContest{
    /**
     * Resuelve el problema de la maratón.
     * @param days objetos por días
     * @return ganancias por días
     */
    public static int[] solve(int[][] days) {
        System.setProperty("java.awt.headless", "true");
        int[] profits = new int[days.length];
    
        for (int i = 0; i < days.length; i++) {
            int[][] partial = Arrays.copyOfRange(days, 0, i + 1);
            SilkRoad road = new SilkRoad(partial);
    
            road.moveRobots();
            profits[i] = road.porfit();
            road.reboot();
    
            for (int j = 0; j < partial.length; j++) {                
                int[] pos = partial[j];                
                int type = pos[0];                
                int location = pos[1];                                
                if (type == 1) {                    
                    road.removeRobot(location);                
                } else if (type == 2) {                    
                    road.removeStore(location);                   
                }            
            }
            road.makeInvisible();
        }
        return profits;
    }

    /**
     * Realiza la simulación de la maratón.
     * @param days objetos por días
     * @param slow velocidad de la simulación (true = lenta)
     */
    public static void simulate(int[][] days, boolean slow) {
        int delayValue = slow ? 100 : 0;

        for (int i = 0; i < days.length; i++) {
            int[][] partial = Arrays.copyOfRange(days, 0, i + 1);
            Canvas.getCanvas().setVisible(true);
            
            SilkRoad road = new SilkRoad(partial);
            road.makeVisible();
            road.setDelay(delayValue);
            
            road.moveRobots();
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            road.reboot();

            for (int j = 0; j < partial.length; j++) {                
                int[] pos = partial[j];                
                int type = pos[0];                
                int location = pos[1];                                
                if (type == 1) {                    
                    road.removeRobot(location);                
                } else if (type == 2) {                    
                    road.removeStore(location);                   
                }            
            }
            road.makeInvisible();
        }

        SilkRoad finalz = new SilkRoad(0);
        finalz.finish();
    }
}