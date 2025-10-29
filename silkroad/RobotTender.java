package silkroad;
import shapes.*;

public class RobotTender extends Robot {
    
    /**
     * robot que se encarga de robar solo la mitad de las monedas de una tienda
     * @param posicion de creacion
     * @param carretera
     */
    public RobotTender(int location, Road road) {
        super(location, road);
        this.color = "magenta";
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
        int half = store.getTenges() / 2;
        if (half > 0) {
            int stolen = store.stealAttempt(half, getStolenTenges());
            addTenges(stolen);
            store.emptiedStore();
        }
        
        if (store.getTenges() == 0) {
            store.setColor("white");
        }
    }
}