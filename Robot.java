import java.util.*;

/**
 * Representa un robot en la ruta de seda.
 * Cada robot tiene una ubicación, pasos recorridos, monedas robadas
 * y una figura circular que lo representa visualmente en el tablero.
 */
public class Robot {
    private int location;
    private int steps;
    private int originalLocation;
    private Circle shape;
    private String color;
    private static final String[] colors = {"red", "yellow", "blue", "green", "magenta"};
    private static final Random rand = new Random();
    private int tenges;
    private int stolenTenges;
    
    private List<Integer> earningsHistory;

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
        this.color = colors[rand.nextInt(colors.length)];

        Rectangle rect = road.getRectangles().get(location);
        int posX = rect.getX() + 8;
        int posY = rect.getY() + 7;

        this.shape = new Circle(color, posX, posY);
        this.shape.makeVisible();
        
        this.earningsHistory = new ArrayList<>();
    }

    /** @return número total de pasos recorridos */
    public int getSteps() { return steps; }

    /** @return ubicación actual en la carretera */
    public int getPosition() { return location; }

    /** @return ubicación original del robot */
    public int getOriginalPosition() { return originalLocation; }

    /** @return color del robot */
    public String getColor() { return color; }

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
        if (amount > tenges) {
            tenges = 0;
            stolenTenges = 0;
        } else {
            stolenTenges -= amount;
        }
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
    public void setPosition(int pos, Road road) {
        if (pos < 0 || pos >= road.getRectangles().size()) return;
        Rectangle rect = road.getRectangles().get(pos);

        int posX = rect.getX() + 8;
        int posY = rect.getY() + 7;

        int pasos = Math.abs(pos - this.location);
        steps += pasos;

        int dx = posX - getX();
        int dy = posY - getY();

        shape.moveHorizontal(dx);
        shape.moveVertical(dy);

        this.location = pos;
    }

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
    
    public void recordEarnings(int amount) {
        earningsHistory.add(amount);
    }

    public List<Integer> getEarningsHistory() {
        return earningsHistory;
    }
}
