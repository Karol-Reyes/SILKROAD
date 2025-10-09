public class ProgressBar {
    private Rectangle background;
    private Rectangle bar;
    private int maxWidth;
    private int currentWidth;
    private int x;
    private int y;
    private String color;

    /**
     * Crea la barra de progreso para SikRoad
     */
    public ProgressBar(int x, int y, int maxWidth, String color) {
        this.x = x;
        this.y = y;
        this.maxWidth = maxWidth;
        this.color = color;
        this.currentWidth = 0;

        background = new Rectangle();
        background.changeColor("black");
        background.changeSize(20, maxWidth);
        background.moveHorizontal(x);
        background.moveVertical(y);

        bar = new Rectangle();
        bar.changeColor(color);
        bar.changeSize(20, 0);
        bar.moveHorizontal(x);
        bar.moveVertical(y);
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
     * genera el nuevo color de la barra
     */
    public void changeColor(String newColor) {
        bar.changeColor(newColor);
        this.color = newColor;
    }
    
    /**
     * Vuelve la barra a 0% 
     */
    public void reset() {
        currentWidth = 0;
        bar.changeSize(20, 0);
    }
}
