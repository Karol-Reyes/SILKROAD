package silkroad;
import shapes.*;

import java.util.*;

/**
 * Representa un robot en la ruta de seda.
 * Cada robot tiene una ubicación, pasos recorridos, monedas robadas
 * y una figura circular que lo representa visualmente en el tablero.
 */
public abstract class Robot {
    protected int location;
    protected int steps;
    protected int originalLocation;
    protected Circle shape;
    protected String color;
    
    protected int tenges;
    protected int stolenTenges;
    
    protected List<Integer> earningsHistory;

    /**
     * Crea un nuevo robot en la ubicación indicada.
     * @param location posición inicial en la carretera
     * @param road carretera donde se ubica el robot
     */
    public Robot(int location, Road road) {
        this.location = location;
        this.steps = 0;
        this.originalLocation = location;
        this.tenges = 0;
        this.stolenTenges = 0;        
        this.earningsHistory = new ArrayList<>();
    }
    
    /**
     * crea la figura que representa al robot y la ubica en la posicion designada
     * @param carretera
     */
    protected void createShape(Road road){
        Rectangle rect = road.getRectangles().get(location);
        int posX = rect.getX() + 8;
        int posY = rect.getY() + 7;

        this.shape = new Circle(color, posX, posY);
        this.shape.makeVisible();
    }

    /** @return número total de pasos recorridos */
    public int getSteps() { return steps; }

    /** @return ubicación actual en la carretera */
    public int getPosition() { return location; }

    /** @return ubicación original del robot */
    public int getOriginalPosition() { return originalLocation; }

    /** @return color del robot */
    public String getColor() { return color; }
    
    /** cambia el robot a un nuevo color */
    public void setColor(String color) {
        this.color = color;
        this.shape.changeColor(color);
    }

    /** @return total de monedas que posee */
    public int getTenges() { return this.tenges; }

    /** @return total de monedas robadas (sin descontar pasos) */
    public int getStolenTenges() { return this.stolenTenges; }

    /**
     * Agrega monedas al robot.
     * @param amount cantidad de monedas
     */
    public void addTenges(int amount) {
        if (amount <= 0) return;
        this.tenges += amount;
        this.stolenTenges += amount;
    }

    /**
     * Resta monedas al robot.
     * @param amount cantidad de monedas gastadas
     */
    public void spendTenges(int amount) {
        if (amount <= 0) return;
        
        this.tenges = Math.max(0, tenges - amount);
        this.stolenTenges = Math.max(0, stolenTenges - amount);
    }

    /** Reinicia las monedas del robot. */
    public void resetStolenTenges() {
        this.stolenTenges = 0;
        this.tenges = 0;
    }

    /**
     * Mueve el robot a una nueva posición.
     * @param pos nueva posición
     * @param road carretera del robot
     */
    public abstract void setPosition(int pos, Road road);

    /** Reinicia el contador de pasos. */
    public void resetSteps() { this.steps = 0; }

    /**
     * Devuelve al robot a su posición original.
     * @param road carretera del robot
     */
    public void returnToOrigin(Road road) {
        setPosition(originalLocation, road);
    }

    /** Elimina la representación gráfica del robot. */
    public void remove() { shape.makeInvisible(); }

    /** @return coordenada X del robot */
    public int getX() { return shape.getX(); }

    /** @return coordenada Y del robot */
    public int getY() { return shape.getY(); }

    /** Hace visible al robot. */
    public void makeVisible() { shape.makeVisible(); }

    /** Hace invisible al robot. */
    public void makeInvisible() { shape.makeInvisible(); }
    
    //////////////////////////////////CICLO 2///////////////////////////
    
    /**
     * agrega el historial de ganacias a una lista
     * @param suma de las ganancias obtenidas 
     */
    public void recordEarnings(int amount) {
        earningsHistory.add(amount);
    }
    
    /**
     * @return lista del historico de ganancias
     */
    public List<Integer> getEarningsHistory() {
        return earningsHistory;
    }
    
    //////////////////////////////////C I C L O  3 ///////////////////////////
    /**Mueve el robot a la derecha*/
    public void moveRight(Road road){
        moveBy(1, 0, road);
    }
    
    /**Mueve el robot a la izquierda*/
    public void moveLeft(Road road) {
        moveBy(-1, 0, road);
    }

    /**Mueve el robot a la arriba*/
    public void moveUp(Road road) {
        moveBy(0, -1, road);
    }

    /**Mueve el robot a la abajo*/
    public void moveDown(Road road) {
        moveBy(0, 1, road);
    }
    
    /**Calcula la nueva casilla a la que se dirigirá el robot
     * @param bloque en x
     * @param bloque en y
     * @param road al que pertenece el robot
     */
    private void moveBy(int xBlocks, int yBlocks, Road road) {

        int theWidth = road.getWidth() + 5;
        int theHeigth = road.getHeight() + 5;

        shape.moveHorizontal(xBlocks * theWidth);
        shape.moveVertical(yBlocks * theHeigth);
    }
    
    /**
     * se define como un robot interactua con la tienda seleccionada
     * @param tienda con la que interactua
     */
    public abstract void interactWithStore(Store store);
    
    /**
     * @return si un robot puede realizar el movimiento o no
     * @param posicion a donde se va a desplazar el robot
     */
    public boolean canMove(int newPosition){
        return true;
    }
}