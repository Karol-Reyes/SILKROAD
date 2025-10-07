import java.util.*;
import javax.swing.JOptionPane;

public class SilkRoadContest{
    private SilkRoad road;
    private int delayValue = 0;
    
    /**
     * resuelve el problema de la maraton
     * @param objetos por dias
     * @return ganancias por dias
     */
    public int[] solve(int[][] days){
        System.setProperty("java.awt.headless", "true");
        int[] profits = new int[days.length];
        //this.road = new SilkRoad(Road.maxPos(days));
        
        for (int i = 0; i < days.length; i++){
            int[][] partial = Arrays.copyOfRange(days, 0, i + 1);
            this.road = new SilkRoad(partial);
            road.moveRobots();
            profits[i] = road.getTotallitary();
            
            road.reboot();
        }
        
        for (int j = 0; j < days.length; j++){                
                int[] pos = days[j];                
                int type = pos[0];                
                int location = pos[1];                                
                if (type == 1){                    
                    road.removeRobot(location);                
                }                
                else if (type == 2){                    
                    road.removeStore(location);                   
                }            
            }
        return profits;
    }
    
    /**
     * realiza la simulación de la maraton
     * @param objetos por dias
     * @param velocidad de la simulacion
     */
    public void simulate(int[][] days, boolean slow){
        for (int i = 0; i < days.length; i++){
            int[][] partial = Arrays.copyOfRange(days, 0, i + 1);
            Canvas.getCanvas().setVisible(true);
            
            this.road = new SilkRoad(partial);
            this.road.makeVisible();
            delayValue = slow ? 100 : 0;
            this.road.setDelay(delayValue);
            
            road.moveRobots();
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            road.reboot();
            for (int j = 0; j < partial.length; j++){                
                int[] pos = partial[j];                
                int type = pos[0];                
                int location = pos[1];                                
                if (type == 1){                    
                    road.removeRobot(location);                
                }                
                else if (type == 2){                    
                    road.removeStore(location);                   
                }            
            }
            road.makeInvisible();
        }
        road.finish();
    }
}