package silkroad;
import shapes.*;

/**
 * Clase base para las tiendas del juego SilkRoad.
 * Contiene el comportamiento común a todos los tipos de tiendas.
 */
public class Store {
    protected int location;
    protected int tenges;
    protected int initialTenges;
    protected int maxTenges;
    protected int emptiedCount;
    protected String color;
    protected String originalColor;
    protected Triangle shape;

    public Store(int location, int tenges, Road road) {
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        this.maxTenges = tenges;
        this.emptiedCount = 0;
    }
    
    /**
     * crea la figura que representa a la tienda y la ubica en la posicion designada
     * @param carretera
     */
    protected void createShape(Road road){
        Rectangle rect = road.getRectangles().get(location);
        int posX = rect.getX() + 15;
        int posY = rect.getY() + 7;
        
        this.shape = new Triangle(color, posX, posY);
        this.shape.makeVisible();
    }

    /**
     * Devuelve todos los tenges disponibles (saqueo completo).
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
     * Permite robar una cantidad específica (por ejemplo, la mitad).
     */
    public int steal(int amount) {
        int stolen = Math.min(amount, tenges);
        tenges -= stolen;
        if (tenges == 0) {
            emptiedCount++;
        }
        return stolen;
    }

    /** 
     * Restaura el color original 
       */
    public void resetColor() {
        setColor(originalColor);
    }

    /**
     * Reabastece la tienda con su cantidad inicial 
       */
    public void resupply() {
        tenges = initialTenges;
    }

    /**
     * Oculta la tienda gráficamente 
       */
    public void makeInvisible() {
        shape.makeInvisible();
    }

    /** 
     * Muestra la tienda gráficamente
    */
    public void makeVisible() {
        shape.makeVisible();
    }

    /**
     * Elimina gráficamente la tienda 
    */
    public void remove() {
        shape.makeInvisible();
    }

    /**
     * Cambia el color actual de la tienda 
       */
    public void setColor(String color) {
        this.color = color;
        this.shape.changeColor(color);
    }

    /**
     * @return la localizacion de la tienda
     */
    public int getLocation() { 
        return location; 
    }
    
    /**
     * @return los tenges de la tienda
     */
    public int getTenges() { 
        return tenges; 
    }
    
    /**
     * @return los tenges iniciales de la tienda
     */
    public int getInitialTenges() {
        return initialTenges; 
    }
    
    /**
     * @return la cantidad maxima de tenges
     */
    public int getMaxTenges() { 
        return maxTenges; 
    }
    
    /**
     * @return la cuenta de cuantas veces se robo la tienda
     */
    public int getEmptiedCount() {
        return emptiedCount; 
    }
    
    /**
     * @return el color de la tienda
     */
    public String getColor() {
        return color; 
    }

    /**
     * reinicia el valor inicial de los tenges
     */
    public void setInitialTenges(int value) {
        this.initialTenges = value; 
    }
    
    /**
     * reinicia los tenges
     */
    public void setTenges(int value) { 
        this.tenges = value; 
    }
    
    /**
     * Permite intentar robar una cantidad deseada teniendo en cuenta si es normal o es fighter
     */
    public int stealAttempt(int desiredAmount, int robotTenges) {
        return steal(desiredAmount);
    }
    
    public void emptiedStore() {
        this.emptiedCount++;
    }
}