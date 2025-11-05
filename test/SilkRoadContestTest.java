package test;

import static org.junit.Assert.*;
import org.junit.Test;
import silkroad.SilkRoadContest;

public class SilkRoadContestTest {

    /**
     * Prueba que verifica que el método puede manejar un arreglo vacío de días.
     */
    @Test
    public void shouldHandleEmptyDays() {
        int[][] days = {};
        SilkRoadContest contest = new SilkRoadContest();
        int[] profits = contest.solve(days);
        assertEquals(0, profits.length);
    }

    /**
     * Prueba que verifica que las ganancias sean no negativas
     * al tener robots y tiendas normales.
     */
    @Test
    public void shouldReturnNonNegativeProfits() {
        int[][] days = {
            {1, 1},        
            {2, 2, 50},    
            {3, 1, 1}      
        };
        SilkRoadContest contest = new SilkRoadContest();
        int[] profits = contest.solve(days);
        for (int p : profits) {
            assertTrue(p >= 0);
        }
    }

    /**
     * Prueba que evalúa el cálculo consistente de ganancias a lo largo de varios días.
     */
    @Test
    public void shouldComputeProfitsConsistently() {
        int[][] days = {
            {1, 1},       
            {2, 2, 30},  
            {3, 1, 1},    
            {3, 1, 1}     
        };
        SilkRoadContest contest = new SilkRoadContest();
        int[] profits = contest.solve(days);

        assertEquals(days.length, profits.length);
        for (int p : profits) {
            assertTrue(p >= 0);
        }
    }

    /**
     * Prueba que asegura que el número de días sea igual al largo del arreglo de ganancias.
     */
    @Test
    public void shouldReturnSameLengthAsDays() {
        int[][] days = {
            {1, 1},
            {2, 2, 10},
            {3, 1, 1}
        };
        SilkRoadContest contest = new SilkRoadContest();
        int[] profits = contest.solve(days);
        assertEquals(days.length, profits.length);
    }
}
