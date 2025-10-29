package shapes;

import java.awt.*;
import java.awt.geom.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000) 
 */

public class Circle extends Figure{

    public static final double PI=3.1416;
    private int diameter;

    public Circle(String color, int x, int y){
        super(color, x, y);
        diameter = 15;
    }
    
    /**
     * @return posicion en X del circulo
     */
    public int getX(){ return xPosition; }
    
    /**
     * @return posicion en y del circulo
     */
    public int getY(){ return yPosition; }
    
    /**
     * @return color del circulo
     */
    public String getColor(){return color;}
    
    /**
     * dibujar el circulo
     */
    @Override
    protected void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new Ellipse2D.Double(xPosition, yPosition, 
                diameter, diameter));
            canvas.wait(10);
        }
    }

    /**
     * borrar el circulo
     */
    @Override
    protected void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }
}
