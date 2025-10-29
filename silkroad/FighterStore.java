package silkroad;

import shapes.*;
import javax.swing.JOptionPane;

public class FighterStore extends Store {

    /**
     * Tienda que solamente se deja robar los tenges si el robot que intenta
     * robarla tiene mas tenges de los que tiene ella
     * @param localizacion de la tienda
     * @param tenges de la tienda
     * @param carretera
     */
    public FighterStore(int location, int tenges, Road road) {
        super(location, tenges, road);
        this.color = "red";
        this.originalColor = color;
        createShape(road);
    }

    /**
     * Permite intentar robar una cantidad deseada teniendo en cuenta si es normal o es fighter
     */
    @Override
    public int stealAttempt(int desiredAmount, int robotTenges) {
        if (robotTenges <= 0) {
            JOptionPane.showMessageDialog(null,"El robot no puede robar: no tiene tenges.","Robo fallido",JOptionPane.WARNING_MESSAGE);
            return 0;
        }

        if (this.getTenges() <= 0) {
            JOptionPane.showMessageDialog(null,"La tienda ya no tiene tenges para robar.","Robo fallido",JOptionPane.WARNING_MESSAGE);
            return 0;
        }

        if (robotTenges > this.getTenges()) {
            int amount = Math.min(desiredAmount, this.getTenges());
            return steal(amount);
        } else {
            JOptionPane.showMessageDialog(null,"El robot no puede robar esta tienda: tiene menos tenges que la tienda.","Robo fallido",JOptionPane.WARNING_MESSAGE);
            return 0;
        }
    }
}