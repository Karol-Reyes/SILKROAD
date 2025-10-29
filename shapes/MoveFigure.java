package shapes;


/**
 * Write a description of interface MoveFigure here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public interface MoveFigure
{
    /**
     * mueve una figura a la derecha
     */
    void moveRight();
    
    /**
     * mover una figura a la izquierda
     */
    void moveLeft();
    
    /**
     * mover una figura arriba
     */
    void moveUp();

    /**
     * mover una figura abajo
     */
    void moveDown();
    
    /**
     * realizar el movimiendo en vertical
     */
    void moveVertical(int distance);
    
    /**
     * realizar el movimiento en horizontal
     */
    void moveHorizontal(int distance);
}