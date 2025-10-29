package silkroad;
import shapes.*;

public class RobotNormal extends Robot {
    
    /**
     * robot que actua e interactua normalmente con su entorno
     * @param posicion de creacion
     * @param carretera
     */
    public RobotNormal(int location, Road road) {
        super(location, road);
        this.color = "green";
        createShape(road);
    }

    /**
     * define la nueva posicion donde se va a ubicar el robot
     * @param nueva posicion
     * @param carretera 
     */
    @Override
    public void setPosition(int pos, Road road){
        if (pos < 0 || pos >= road.getRectangles().size()) return;
        
        Rectangle rect = road.getRectangles().get(pos);
        int posX = rect.getX() + 8;
        int posY = rect.getY() + 7;

        int pasos = Math.abs(pos - this.location);
        steps += pasos;

        int dx = posX - shape.getX();
        int dy = posY - shape.getY();

        shape.moveHorizontal(dx);
        shape.moveVertical(dy);

        this.location = pos;
    }
    
    /**
     * define como el robot va a interactuar con la tiendas
     * @param tienda con la que interactúa
     */
    @Override
    public void interactWithStore(Store store) {
        int desired = store.getTenges();
        int stolen = store.stealAttempt(desired, getStolenTenges());
        addTenges(stolen);
        if (store.getTenges() == 0) {
            store.emptiedStore();
            store.setColor("white");
        }
    }

    /**
     * adiciona los nuevos tenges robados
     * @param tenges robados
     */
    public void addTenges(int amount) {
        if (amount > 0) {
            tenges += amount;
            stolenTenges += amount;
            recordEarnings(amount);
        }
    }
}