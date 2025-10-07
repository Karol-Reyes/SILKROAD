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
    private ProgressBar progressBar;
    private int delayed = 0;
    
    ////////////////////////////////////////////////////// C I C L O   U N O ////////////////////////////////////////
    
    /**
     * Crea una instancia del juego con un camino de cierta cantidad de casillas.
     * @param quantity número de casillas del camino
     */
    public SilkRoad(int quantity) {
        road = new Road(quantity);
        this.isVisible = false;
        this.robots = new ArrayList<>();
        this.stores = new ArrayList<>(); 
        this.itsOk = false;
        
        progressBar = new ProgressBar(260, 565, 200, "green");
        
        Canvas canvas = Canvas.getCanvas();
        if (canvas.isVisible()) {
            progressBar.makeVisible();
        }
    }

    /**
     * Entrega las ganancias totales por dia
     * @return ganancias por dia
     */
    public int getTotallitary(){
        int currentProfit = porfit();
        return currentProfit;
    }
    
    /**
     * Actualiza la barra de progreso según las ganancias netas de los robots.
     * Usa progressBar.update(percentage) de la clase ProgressBar (porcentaje entre 0.0 y 1.0).
     */
    private void updateProgressBar() {
        if (progressBar == null) return;
    
        int total = 0;
        for (int i = 0; i < robots.size(); i++) {
            int stolenTenges = robots.get(i).getStolenTenges();
            int stepsEds     = robots.get(i).getSteps();
            total += (stolenTenges - stepsEds);
        }
    
        if (total < 0) total = 0;
    
        int maximo = totalInitialTenges;
        if (maximo <= 0) maximo = 1; // evitar división por cero
    
        double porcentaje = (double) total / (double) maximo;
        if (porcentaje < 0.0) porcentaje = 0.0;
        if (porcentaje > 1.0) porcentaje = 1.0;
    
        progressBar.update(porcentaje);
        itsOk = true;
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
    
    /*
     * tiempo de demora de la ejecucion
     */
    private void delay(int delayed) {
        try { 
            Thread.sleep(delayed); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }
    }
    
    /**
     * Mueve un robot de una casilla a otra. Si entra en una tienda, roba sus monedas.
     * @param location posición actual del robot
     * @param newLocation nueva posición a la que se moverá
     */
    public void moveRobot(int location, int meters) {
        int position = (location - 1) + meters;
        if (isVisible == true && (position < 0 || position > road.getRectangles().size())) {
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
            if (isVisible == true && r != robot && r.getPosition() == position) {
                JOptionPane.showMessageDialog(null, "There is already a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);
                itsOk = false;
                return;
            }
        }
        int steps = Math.abs(meters);
        
        int direction = (meters >= 0) ? 1 : -1;
        int nowPos = location - 1;
        
        for (int i = 0; i < steps; i++) {
            int nextPos = nowPos + direction;
            if (isVisible == true && nextPos < 0 || nextPos >= road.getRectangles().size()) {
                JOptionPane.showMessageDialog(null, "Movement out of range.", "Error", JOptionPane.WARNING_MESSAGE);
                itsOk = false;
                return;
            }
        
            Rectangle now = road.getRectangles().get(nowPos);
            Rectangle next = road.getRectangles().get(nextPos);
        
            if (next.getX() > now.getX()) {
                robot.moveRight(road);
            } else if (next.getX() < now.getX()) {
                robot.moveLeft(road);
            } else if (next.getY() > now.getY()) {
                robot.moveDown(road);
            } else if (next.getY() < now.getY()) {
                robot.moveUp(road);
            }
            
            delay(delayed);
            nowPos = nextPos;
        }
        
        robot.setPosition(position, road);
        
        for (Store s : stores) {
            if (s.getLocation() == position && s.getTenges() > 0) {
                int robbed = s.stealAll();
                robot.addTenges(robbed);
                s.setColor("white");
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
            s.resetColor();
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
        itsOk = true;
    }
    
    /**
     * Muestra todos los elementos en pantalla.
     */
    public void makeVisible() {
        isVisible = true;
        Canvas.getCanvas().setVisible(true);    
        for (Rectangle r : road.getRectangles()) {
            r.makeVisible();
        }
    
        for (Store s : stores) {
            s.makeVisible();
        }
    
        for (Robot r : robots) {
            r.makeVisible();
        }
        
        progressBar.makeVisible();
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
        
        progressBar.makeInvisible();
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
    
    /**
     * @return si una operacion se realizó correctamente o no
     */
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
                null, "invalid row, please assign at least one object and one location","Configuration Error", JOptionPane.ERROR_MESSAGE);
                itsOk = false;
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
                        itsOk = false;
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
                        itsOk = false;
                    }
            }
        }
        itsOk = true;
    }
    
    /**
     * Cada robot escoge su movimiento mas rentable a la hora de 
     * robar tiendas
     */
    public void moveRobots(){
        boolean moved;
        do {
            moved = false;
            Robot rob = null;
            Store sto = null;
            int bestProfit = -10000;
            for (Robot r : robots) {
                Store bestRobot = null;
                int bestProfitRobot = -10000;
                for (Store s : stores) {
                    if (s.getTenges() <= 0) continue;
                    int steps = Math.abs(s.getLocation() - r.getPosition());
                    int profit = s.getTenges() - steps;
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
                int teps = sto.getLocation() - rob.getPosition();
                moveRobot(rob.getPosition() + 1, teps);
                //int realEarnings = rob.getStolenTenges();
                //rob.recordEarnings(realEarnings);
                rob.recordEarnings(bestProfit);
                moved = true;
            }
        } while (moved);
        itsOk = true;
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
    
    ////////////////////////////////////////////  C I C L O   3  /////////////////////////////////////
    
    /**
     * Configura el tiempo de delay (en milisegundos) para los movimientos.
     * @param ms tiempo de delay en milisegundos
     */
    public void setDelay(int ms) {
        this.delayed = ms;
    }
}