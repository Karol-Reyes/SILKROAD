package silkroad;

import shapes.*;
import java.util.Random;

public class AutonomousStore extends Store {

    /**
     * Tienda autónoma que se coloca en una posición aleatoria del mapa,
     * ignorando la posición indicada al ser creada.
     * @param posicion a ignorar
     * @param tenges de la tienda
     * @param carretera
     */
    public AutonomousStore(int ignoredLocation, int tenges, Road road) {
        super(0, tenges, road);
        this.color = "blue";
        this.originalColor = color;
        createShape(road);
        int randomLoc = getRandomLocation(road);
        this.location = randomLoc;
        moveToLocation(randomLoc, road);
    }

    /**
     * Genera una posición aleatoria válida dentro del rango del tablero.
     */
    private int getRandomLocation(Road road) {
        Random rand = new Random();
        int total = road.getRectangles().size();
        return rand.nextInt(total); 
    }

    /**
     * Mueve gráficamente la tienda a su nueva posición.
     */
    private void moveToLocation(int location, Road road) {
        Rectangle rect = road.getRectangles().get(location);
        int posX = rect.getX() + 15;
        int posY = rect.getY() + 7;
        shape.moveHorizontal(posX - shape.getX());
        shape.moveVertical(posY - shape.getY());
    }
}


