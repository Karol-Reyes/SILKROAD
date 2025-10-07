import java.util.List;
import java.util.Random;

/**
 * Representa una tienda en la ruta de seda.
 * Cada tienda tiene una ubicación, monedas asignadas y un triángulo como representación gráfica.
 */
public class Store {
    private int location;
    private int maxTenges;
    private int tenges;
    private int initialTenges;
    private Triangle shape;
    private String color;
    private int emptiedCount; //Metodo ciclo 2
    private String sColor = "yellow";
    private String theColor;
    //private static final String[] sColor = {"red", "yellow", "blue", "green", "magenta"};
    //private static final Random rand = new Random();

    public Store(int location, int tenges, Road road) {
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        //this.color = sColor[rand.nextInt(sColor.length)];
        this.color = sColor;
        this.theColor = sColor;
        this.maxTenges = 0;
        this.emptiedCount = 0; 

        Rectangle rect = road.getRectangles().get(location);
        int posX = rect.getX() + 15;
        int posY = rect.getY() + 7;

        this.shape = new Triangle(color, posX, posY);
        this.shape.makeVisible();
    }

    /** 
     * Extrae todas las monedas disponibles. 
     */
    public int stealAll() {
        int stolen = tenges;
        tenges = 0;
        if (stolen > 0) {
            emptiedCount++; 
        }
        return stolen;
    }
    
    /**
     * Devuelve el color asignado originalmente
     */
    public void resetColor(){
        setColor(theColor);
    }
    
    /**
     * @return veces que la tienda ha sido vaciada 
     */
    public int getEmptiedCount() {
        return emptiedCount;
    }

    /** @return máximo de monedas registradas */
    public int getMaxTenges() { return maxTenges; }

    /** @return ubicación de la tienda */
    public int getLocation() { return location; }

    /** @return monedas actuales de la tienda */
    public int getTenges() { return tenges; }

    /** @return cantidad inicial de monedas de la tienda */
    public int getInitialTenges() { return initialTenges; }

    /**
     * Establece la cantidad inicial de monedas de la tienda.
     * @param value nuevo valor de monedas iniciales
     */
    public void setInitialTenges(int value) { this.initialTenges = value; }

    /** @return color de la tienda */
    public String getColor() { return color; }
    
    /** cambia la tienda a otro color */
    public void setColor(String color) {
        this.color = color;
        this.shape.changeColor(color);
    }

    /** Restaura las monedas de la tienda a su valor inicial. */
    public void resupply() { tenges = initialTenges; }

    /** Elimina visualmente la tienda. */
    public void remove() { shape.makeInvisible(); }

    /** Hace visible la tienda. */
    public void makeVisible() { shape.makeVisible(); }

    /** Hace invisible la tienda. */
    public void makeInvisible() { shape.makeInvisible(); }
}