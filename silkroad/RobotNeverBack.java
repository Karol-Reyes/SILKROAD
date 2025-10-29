package silkroad;
import shapes.*;

public class RobotNeverBack extends Robot {
    private int maxPositionReached;

    /**
     * robot que nunca se devuelve
     * @param ubicacion
     * @param carretera
     */
    public RobotNeverBack(int location, Road road) {
        super(location, road);
        this.color = "red";
        createShape(road);
        this.maxPositionReached = location;
    }
    
    /**
     * @return si un robot puede o no moverse a la nueva posicion
     * @param nueva ubicacion
     */
    @Override
    public boolean canMove(int newPosition) {
        if (newPosition < this.location) {return false;}
        return true;
    }

    /**
     * actualiza la posicion del robot
     * @param posicion a ir
     * @param carretera
     */
    @Override
    public void setPosition(int pos, Road road) {
    
        if (pos == this.originalLocation) {
            Rectangle rect = road.getRectangles().get(pos);
            int dx = rect.getX() + 8 - shape.getX();
            int dy = rect.getY() + 7 - shape.getY();
            shape.moveHorizontal(dx);
            shape.moveVertical(dy);
            this.location = pos;
            return;
        }
    
        if (!canMove(pos)) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "This robot can´t never back","Invalid Movement", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        if (pos < 0 || pos >= road.getRectangles().size()) return;
    
        Rectangle rect = road.getRectangles().get(pos);
        int dx = rect.getX() + 8 - shape.getX();
        int dy = rect.getY() + 7 - shape.getY();
        shape.moveHorizontal(dx);
        shape.moveVertical(dy);
        this.steps += Math.abs(pos - location);
        this.location = pos;
    }

    /**
     * define la forma en la que el robot interactua con la tienda
     * @param tienda con la que interactua
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
     * adiciona los nuevs tenges robados
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