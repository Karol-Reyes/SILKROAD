package silkroad;
import shapes.*;

public class ProgressBar {
    private Rectangle background;
    private Rectangle bar;
    private int maxWidth;
    private int currentWidth;
    private int x;
    private int y;

    /**
     * Crea la barra de progreso para SikRoad
     */
    public ProgressBar(int x, int y, int maxWidth, String color) {
        this.x = x;
        this.y = y;
        this.maxWidth = maxWidth;
        this.currentWidth = 0;

        background = new Rectangle("black",x,y);
        background.changeSize(20, maxWidth);

        bar = new Rectangle(color,x,y);
        bar.changeSize(20, 0);
    }
    
    /**
     * Hace visible la barra de progreso 
     */
    public void makeVisible() {
        if (background != null) {
            background.makeVisible();
        }
        if (bar != null) {
            bar.makeVisible();
        }
    }
    
    /**
     * Hace invisible la barra de progreso 
     */
    public void makeInvisible() {
        if (bar != null) {
            bar.makeInvisible();
        }
        if (background != null) {
            background.makeInvisible();
        }
    }
    
    /**
     * Actualiza la barra según el porcentaje
     */
    public void update(double percentage) {
        if (percentage < 0) percentage = 0;
        if (percentage > 1) percentage = 1;

        int newWidth = (int)(maxWidth * percentage);
        bar.changeSize(20, newWidth);
        currentWidth = newWidth;
    }
    
    /**
     * Vuelve la barra a 0% 
     */
    public void reset() {
        currentWidth = 0;
        bar.changeSize(20, 0);
    }
}