import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Juego de tiendas y robots en un camino en espiral.
 * Los robots pueden moverse, robar monedas de las tiendas y el sistema mantiene
 * un estado con barras de progreso y reportes de ganancias.
 * 
 * @author Juan Moreno y Karol Rodriguez
 * @version 1.0
 */
public class SilkRoad
{
    private Rectangle backGround;
    private Rectangle bar;
    private Road road;
    private List<Robot> robots;
    private List<Store> stores;
    private int maxTenge;
    private int totalInitialTenges = 0;
    private boolean isVisible;
    private boolean itsOk;

    ////////////////////////////////////////////////////// C I C L O   U N O ////////////////////////////////////////
    /**
     * Crea una instancia del juego con un camino de cierta cantidad de casillas.
     * @param quantity número de casillas del camino
     */
    public SilkRoad(int quantity) {
        road = new Road(quantity);
        boolean isVisible = true;
        this.robots = new ArrayList<>();
        this.stores = new ArrayList<>(); 
        this.itsOk = true;
        makeVisible();
        
        backGround = new Rectangle();
        backGround.changeColor("black");
        backGround.changeSize(20, 200);
        backGround.moveHorizontal(260);
        backGround.moveVertical(565);
        backGround.makeVisible();
        
        bar = new Rectangle();
        bar.changeColor("green");
        bar.changeSize(20, 0);
        bar.moveHorizontal(260);
        bar.moveVertical(565);
        bar.makeVisible();
    }

    
    /*
     * Actualiza la barra de progreso en función de las monedas robadas y pasos gastados.
     */
    private void updateProgressBar() {
        int total = 0;
    
        for (int i = 0; i < robots.size(); i++) {
            int stolenTenges = robots.get(i).getStolenTenges();
            int stepsEds = robots.get(i).getSteps();
            total += (stolenTenges - stepsEds);
        }
    
        if (total < 0) {
            total = 0;
        }
        int maximo = totalInitialTenges;
        
        if (maximo <= 0) {
            maximo = 1; // evitar división por cero
        }
        int altura = (int) ((total * 200.0) / maximo);
        
        if (altura > 200) {
            altura = 200;
        }
        bar.changeSize(20, altura);  
    }
    
    /* Suma las monedas robadas (no descuenta pasos) de todos los robots
     * *@return sume de los stolenTenges de todos los robots
     */
    private int getTotalStolenByRobots() {
        int sum = 0;
        for (Robot r : robots) {
            sum += r.getStolenTenges();
        }
        return sum;
    }
    
    /**
     * Coloca un robot en la posición indicada si está libre.
     * @param location posición en el camino
     */
    public void placeRobot(int location) {
        if (isVisible == true && (location < 1 || location > road.getRectangles().size())) {
            JOptionPane.showMessageDialog(
                null, "Position out of range.", "Error", JOptionPane.WARNING_MESSAGE
            );
            itsOk = false;
            return;
        }
        for (Robot r : robots) {
            if (isVisible == true && r.getPosition() == location - 1) {
                JOptionPane.showMessageDialog(
                    null, "There is already a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);
                itsOk = false;
                return;
            }
        }
        Robot robot = new Robot(location - 1, road);
        robots.add(robot);
        
        updateProgressBar();
        itsOk = true;
    }
    
    /**
     * Mueve un robot de una casilla a otra. Si entra en una tienda, roba sus monedas.
     * @param location posición actual del robot
     * @param newLocation nueva posición a la que se moverá
     */
    public void moveRobot(int location, int meters) {
        if (isVisible == true && (meters < 1 || meters > road.getRectangles().size())) {
            JOptionPane.showMessageDialog(null, "Movement out of range.", "Error", JOptionPane.WARNING_MESSAGE);
            itsOk = false;
            return;
        }
        Robot robot = null;
        int robotIndex = -1;
        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getPosition() == location - 1) {
                robot = robots.get(i);
                robotIndex = i;
                break;
            }
        }
        if (isVisible == true && robot == null) {
            JOptionPane.showMessageDialog(null, "There is no a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);
            itsOk = false;
            return;
        }
        for (Robot r : robots) {
            if (isVisible == true && r != robot && r.getPosition() == meters - 1) {
                JOptionPane.showMessageDialog(null, "There is already a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);
                itsOk = false;
                return;
            }
        }
        robot.setPosition(meters - 1, road);
        int steps = Math.abs(meters - location);
        
        for (Store s : stores) {
            if (s.getLocation() == meters - 1 && s.getTenges() > 0) {
                int robbed = s.stealAll();
                robot.addTenges(robbed);
                break;
            }
        }
        updateProgressBar();
        itsOk = true;
    }
    
    /**
     * Elimina un robot de la casilla indicada.
     * @param location posición del robot a eliminar
     */
    public void removeRobot(int location) {
        if (isVisible == true && (location < 1 || location > road.getRectangles().size())) {
            JOptionPane.showMessageDialog(null, "Box out of range", "Error",JOptionPane.WARNING_MESSAGE);
            itsOk = false;
            return;
        }
        boolean removed = false;
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            if (r.getPosition() == location - 1) { 
                r.remove();   
                robots.remove(i); 
                removed = true;
                break;
            }
        }
        if (isVisible == true && !removed){
            JOptionPane.showMessageDialog(null, "There is no a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);   
            itsOk = false;
        }
        updateProgressBar();
        itsOk = true;
    }
    
    /**
     * Devuelve todos los robots a su origen, reiniciando monedas y pasos.
     */
    public void returnRobots() {
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            r.returnToOrigin(road);
            r.resetSteps();
            r.resetStolenTenges();
        }
        updateProgressBar();
        itsOk = true;
    }

    /**
     * Coloca una tienda en la posición indicada.
     * @param location posición de la tienda
     * @param tenges número de monedas iniciales
     */
    public void placeStore(int location, int tenges) {
        if (isVisible == true && (location < 1 || location > road.getRectangles().size())) {
            JOptionPane.showMessageDialog(null, "Box out of range", "Error", JOptionPane.WARNING_MESSAGE);
            itsOk = false;
            return;
        }
    
        for (Store s : stores) {
            if (isVisible == true && s.getLocation() == location - 1) {
                JOptionPane.showMessageDialog(null, "There is already a store in the box", "Error", JOptionPane.WARNING_MESSAGE);
                itsOk = false;
                return;
            }
        }
        Store store = new Store(location - 1, tenges, road);
        stores.add(store);
        
        totalInitialTenges += tenges;
        store.setInitialTenges(tenges);
        updateProgressBar();
        itsOk = true;
    }
    
    /**
     * Elimina una tienda de la casilla indicada.
     * @param location posición de la tienda a eliminar
     */
    public void removeStore(int location) {
        if (isVisible == true && (location < 1 || location > road.getRectangles().size())) {
            JOptionPane.showMessageDialog(null, "Box out of range", "Error", JOptionPane.WARNING_MESSAGE);
            itsOk = false;
            return;
        }
        boolean removed  = false;
        for (int i = 0; i < stores.size(); i++) {
            Store s = stores.get(i);
            if (s.getLocation() == location - 1) {
                totalInitialTenges -= s.getInitialTenges(); 
                s.remove();                
                stores.remove(i);
                removed = true;
                break;
            }
        }
        if (isVisible == true && !removed){
            JOptionPane.showMessageDialog(null, "There is no a store in the box", "Error", JOptionPane.WARNING_MESSAGE);    
            itsOk = false;
        }
        updateProgressBar();
        itsOk = true;
    }
    
    /**
     * Reabastece todas las tiendas a su valor inicial y reinicia los robos de robots.
     */
    public void resupplyStores() {
        for (int i = 0; i < stores.size(); i++) {
            Store s = stores.get(i);
            s.resupply();
        }
    
        for (Robot r : robots) {
           r.resetStolenTenges(); // tenges robados se reinician a 0
        }
        updateProgressBar();
        itsOk = true;
    }
    
    /**
     * Reinicia el estado del juego: robots y tiendas.
     */
    public void reboot(){
        returnRobots(); 
        resupplyStores();
        if (isVisible == true){
            JOptionPane.showMessageDialog(null, "All items have been reset", 
            "reboot completed", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Muestra todos los elementos en pantalla.
     */
    public void makeVisible() {
        isVisible = true;
    
        for (Rectangle r : road.getRectangles()) {
            r.makeVisible();
        }
    
        for (Store s : stores) {
            s.makeVisible();
        }
    
        for (Robot r : robots) {
            r.makeVisible();
        }
        
        if (backGround != null) {
            backGround.makeVisible();
        }

        if (bar != null) {
            bar.makeVisible();
        }
    }
    
    /**
     * Oculta todos los elementos en pantalla.
     */
    public void makeInvisible() {
        isVisible = false;
    
        for (Rectangle r : road.getRectangles()) {
            r.makeInvisible();
        }
    
        for (Robot r : robots) {
            r.makeInvisible();
        }
    
        for (Store s : stores) {
            s.makeInvisible();
        }
    
        if (bar != null) {
            bar.makeInvisible();
        }
    
        if (backGround != null) {
            backGround.makeInvisible();
        }
    }
    
    /*Calcula las monedas con las que quedan los robots despues de
     * descontarlos pasos
     * @return suma neta de tenges en los robots
     */   
    private int getTengesRobots() {
        int sum = 0;
        for (Robot r : robots) {
            sum += (r.getStolenTenges() - r.getSteps());
        }
        return sum;
    }
    
    /**
     * Muestra la ganancia neta de los robots.
     */
    public int porfit() {
        return getTengesRobots();
    }

    /**
     * Consulta el estado completo de las tiendas
     */
    public int[][] stores(){
        int result = stores.size();
        int[][] information = new int[result][2];
        
        int index = 0;
        
        for (Store s : stores){
            information [index][0] = s.getLocation() + 1;
            information [index][1] = s.getTenges();
            index++;
        }
        java.util.Arrays.sort(information, (a,b) -> Integer.compare(a[0], b[0]));
        return information;
    }

    /**
     * Consulta el estado completo de los robots
     */
    public int[][] robots() {
        int result = robots.size();
        int[][] information = new int[result][2]; 
    
        int index = 0;
        for (Robot r : robots) {
            information[index][0] = r.getPosition() + 1;
            information[index][1] = r.getStolenTenges() - r.getSteps();
            index++;
        }
    
        java.util.Arrays.sort(information, (a, b) -> Integer.compare(a[0], b[0]));
        return information;
    }
   
    /*
     * @return carretera utilizada en el juego
     */
    private Road getRoad() { 
        return road; 
    }
    
    /*
     * @return lista de robots
     */
    private List<Robot> getRobots() { 
        return robots; 
    }
    
    /*
     * @return lista de tiendas
     */
    private List<Store> getStores() { 
        return stores; 
    }
    
    /**
     * finaliza el juego
     */
    public void finish() {
        if (isVisible == true){
            String report =
            "==== Final Report ====\n" +
            "Initial total store coins: " + totalInitialTenges + "\n" +
            "Final coins stolen by robots (profit): " + getTengesRobots() + "\n" +
            "Simulation finished.\n";

            JOptionPane.showMessageDialog(null, report, "Silk Road - Finished", JOptionPane.INFORMATION_MESSAGE);
        }
        makeInvisible(); // oculta todos los objetos
        System.exit(0);  // termina el programa
    }
    
    public boolean ok(){
        return itsOk;
    }
    ///////////////////////////////////////////////////// C I C L O   D O S /////////////////////////////////////////////
    /*
     * Metodo auxiliar para calcular el maximo de la ruta
     * @param days cantidad de objetos en la ruta 
     * @return tamaño de la carretera
     */
    private static int maxPos(int[][] days) {
        int maxPos = 0;
        for (int[] row : days) {
            if (row.length >= 2 && row[1] > maxPos) {
                maxPos = row[1];
            }
        }
        return maxPos;
    }
    
    /**
     * Configura robots y tiendas respecto a la entrada estandas.
     * @param days conkjunto de entidades a ingresar
     */
    public SilkRoad(int[][] days) {
        this(maxPos(days));
        for (int[] row : days) {
            if (isVisible == true && row.length < 2) {
                JOptionPane.showMessageDialog(
                    null, "invalid row, please assign at least one object and one location",
                    "Configuration Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            int type = row[0];
            int position = row[1];
            switch (type) {
                case 1:
                    placeRobot(position);
                    robots.get(robots.size() - 1).setColor("red");
                    break;
                case 2:
                    if (isVisible == true && row.length < 3) {
                        JOptionPane.showMessageDialog(
                        null, "the 'tenges' data is missing", 
                        "Configuration Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int tenges = row[2];
                        placeStore(position, tenges);
                        stores.get(stores.size() - 1).setColor("magenta");
                    }
                    break;
                default:
                    if (isVisible == true){
                        JOptionPane.showMessageDialog(
                        null, "invalid type: " + type + " (only 1=Robot or 2=Store)",
                        "Configuration Error", JOptionPane.ERROR_MESSAGE);
                    }
            }
        }
    }
    
    /**
     * Cada robot escoge su movimiento mas rentable a la hora de 
     * robar tiendas
     */
    public void moveRobot(){
        boolean moved;
        do {
            moved = false;
            Robot rob = null;
            Store sto = null;
            int bestProfit = Integer.MIN_VALUE;
            for (Robot r : robots) {
                Store bestRobot = null;
                int bestProfitRobot = Integer.MIN_VALUE;
                for (Store s : stores) {
                    if (s.getTenges() <= 0) continue;
                    int profit = s.getTenges() - Math.abs(s.getLocation() - r.getPosition());
                    if (profit >= 0 && profit > bestProfitRobot) {
                        bestProfitRobot = profit;
                        bestRobot = s;
                    }
                }
                if (bestRobot != null && bestProfitRobot > bestProfit) {
                    bestProfit = bestProfitRobot;
                    rob = r;
                    sto = bestRobot;
                }
            }
    
            if (rob != null && sto != null) {
                moveRobot(rob.getPosition() + 1, sto.getLocation() + 1);
                rob.recordEarnings(bestProfit);
                moved = true;
            }
        } while (moved);
    }
    
    /**
     * dice cuantas veces se han vaciados las tienda
     * @return el conjunto de tienda con la cuenta de cuantas veces
     * se vaciaron cada una
     */
    public int[][] empitiedStores() {
        int[][] data = new int[stores.size()][2];

        for (int i = 0; i < stores.size(); i++) {
            Store s = stores.get(i);
            data[i][0] = s.getLocation() + 1; 
            data[i][1] = s.getEmptiedCount();
        }
        return data;
    }
    
    /**
     * Permite conocer las ganancias obtenidas por cada robot en cada movimiento
     * @return matriz de int[][] con la informacion
     */
    public int[][] profitPerMove(){
        int[][] result = new int[robots.size()][];
    
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            java.util.List<Integer> history = r.getEarningsHistory();
    
            result[i] = new int[history.size() + 1];
            result[i][0] = r.getOriginalPosition() + 1;
    
            for (int j = 0; j < history.size(); j++) {
                result[i][j + 1] = history.get(j);
            }
        }
        return result;
    }
}