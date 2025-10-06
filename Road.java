import java.util.ArrayList;
import java.util.List;

/**
 * Representa un camino en forma de espiral compuesto por rectángulos.
 */
public class Road {
    private List<Rectangle> rectangles;
    private static final int separation = 5;
    private int quantity;
    private int width;
    private int height;
    private int x;
    private int y;

    /**
     * Crea un camino con una cantidad específica de bloques.
     * @param quantity número total de bloques a generar
     */
    public Road(int quantity) {
        rectangles = new ArrayList<>();
        this.quantity = quantity;
        this.width = 30;
        this.height = 30;
        this.x = 350;
        this.y = 350;
        figure();
    }

    /**
     * @return altura de cada bloque
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return ancho de cada bloque
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return lista de rectángulos que conforman el camino
     */
    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    /**
     * Genera los bloques en espiral y los añade al camino.
     */
    public void figure() {
        int posX = x;
        int posY = y;
        int adress = 1;
        int length = 2;
        int generated = 0;

        while (generated < quantity) {
            for (int i = 0; i < length && generated < quantity; i++) {
                Rectangle r = new Rectangle();
                r.changeSize(width, height);
                r.makeVisible();
                r.moveHorizontal(posX);
                r.moveVertical(posY);
                rectangles.add(r);
                generated++;

                switch (adress) {
                    case 1: posX += width + separation; break;
                    case 2: posY += height + separation; break;
                    case 3: posX -= width + separation; break;
                    case 4: posY -= height + separation; break;
                }
            }
            adress++;
            if (adress > 4) adress = 1;
            length++;
        }
    }
}