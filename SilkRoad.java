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

    ////////////////////////////////////////////////////// C I C L O   U N O ////////////////////////////////////////
    /**
     * Crea una instancia del juego con un camino de cierta cantidad de casillas.
     * @param quantity número de casillas del camino
     */
    public SilkRoad(int quantity) {
        road = new Road(quantity);
        boolean isVisible = false;
        this.robots = new ArrayList<>();
        this.stores = new ArrayList<>(); 
        
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
        if (location < 1 || location > road.getRectangles().size()) {
            JOptionPane.showMessageDialog(
                null, "Position out of range.", "Error", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        for (Robot r : robots) {
            if (r.getPosition() == location - 1) {
                JOptionPane.showMessageDialog(
                    null, "There is already a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        Robot robot = new Robot(location - 1, road);
        robots.add(robot);
        
        updateProgressBar();
    }
    
    /**
     * Mueve un robot de una casilla a otra. Si entra en una tienda, roba sus monedas.
     * @param location posición actual del robot
     * @param newLocation nueva posición a la que se moverá
     */
    public void moveRobot(int location, int meters) {
        if (meters < 1 || meters > road.getRectangles().size()) {
            JOptionPane.showMessageDialog(null, "Movement out of range.", "Error", JOptionPane.WARNING_MESSAGE);
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
        if (robot == null) {
            JOptionPane.showMessageDialog(null, "There is no a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Robot r : robots) {
            if (r != robot && r.getPosition() == meters - 1) {
                JOptionPane.showMessageDialog(null, "There is already a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);
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
    }
    
    /**
     * Elimina un robot de la casilla indicada.
     * @param location posición del robot a eliminar
     */
    public void removeRobot(int location) {
        if (location < 1 || location > road.getRectangles().size()) {
            JOptionPane.showMessageDialog(null, "Box out of range", "Error",JOptionPane.WARNING_MESSAGE);
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
        updateProgressBar();
        if (!removed){
            JOptionPane.showMessageDialog(null, "There is no a robot in the box", "Error", JOptionPane.WARNING_MESSAGE);    
        }
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
    }

    /**
     * Coloca una tienda en la posición indicada.
     * @param location posición de la tienda
     * @param tenges número de monedas iniciales
     */
    public void placeStore(int location, int tenges) {
        if (location < 1 || location > road.getRectangles().size()) {
            JOptionPane.showMessageDialog(null, "Box out of range", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        for (Store s : stores) {
            if (s.getLocation() == location - 1) {
                JOptionPane.showMessageDialog(null, "There is already a store in the box", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        Store store = new Store(location - 1, tenges, road);
        stores.add(store);
        
        totalInitialTenges += tenges;
        store.setInitialTenges(tenges);
        updateProgressBar();
    }
    
    /**
     * Elimina una tienda de la casilla indicada.
     * @param location posición de la tienda a eliminar
     */
    public void removeStore(int location) {
        if (location < 1 || location > road.getRectangles().size()) {
            JOptionPane.showMessageDialog(null, "Box out of range", "Error", JOptionPane.WARNING_MESSAGE);
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
        updateProgressBar();
        if (!removed){
            JOptionPane.showMessageDialog(null, "There is no a store in the box", "Error", JOptionPane.WARNING_MESSAGE);    
        }
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
    }
    
    /**
     * Reinicia el estado del juego: robots y tiendas.
     */
    public void reboot(){
        returnRobots(); 
        resupplyStores();
        JOptionPane.showMessageDialog(null, "All items have been reset", 
            "reboot completed", JOptionPane.INFORMATION_MESSAGE);
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
     * @return sume neta de tenges en los robots
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
            information[index][1] = r.getTenges();
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
        StringBuilder sb = new StringBuilder();
        sb.append("==== Final Report ====\n");
        sb.append("Initial total store coins: ").append(totalInitialTenges).append("\n");
        sb.append("Final coins stolen by robots (net): ").append(getTengesRobots()).append("\n");
        sb.append("Simulation finished.\n");
    
        JOptionPane.showMessageDialog(null, sb.toString(), "Silk Road - Finished", JOptionPane.INFORMATION_MESSAGE);
        makeInvisible(); // oculta todos los objetos
        System.exit(0);  // termina el programa
    }
    ///////////////////////////////////////////////////// C I C L O   D O S /////////////////////////////////////////////
    /**
     * Configura robots y tiendas respecto a la cantidad de dias.
     * @param entityCount cantidad de entidades a configurar
     */
    public void setupEntities(int days) {
        Random rand = new Random();
        List<Integer> usedPositions = new ArrayList<>();
    
        for (int i = 0; i < days; i++) {
            int type = rand.nextInt(2) + 1; 
            int position = rand.nextInt(road.getRectangles().size()) + 1;
            while (usedPositions.contains(position)) {
                position = rand.nextInt(road.getRectangles().size()) + 1;
            }
            usedPositions.add(position);
            if (type == 1) {
                placeRobot(position);
            } else {
                int tenges = rand.nextInt(99) + 1; 
                placeStore(position, tenges);
            }
        }
    }
    
    /**
     * dice cuantas veces se han vaciados las tienda
     */
    
    public void empitiedStores() {
        String report = ""; // texto vacío al inicio

        for (int i = 0; i < stores.size(); i++) {
            Store s = stores.get(i);
            int storeId = i + 1;
    
            report = report + "Store " + storeId + " (pos " + (s.getLocation() + 1) + ") " + "emptied " + s.getEmptiedCount() + " times\n";
        }
    
        JOptionPane.showMessageDialog(null, report, "Store Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
}