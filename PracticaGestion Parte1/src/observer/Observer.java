package observer;

/**
 * Interfaz que representa un observador. Esta interfaz
 * será implementada por todos los modelos.
 * @author Miguel Ángel
 * @version 1.0
 */
public interface Observer {

    /**
     * Actualiza el estado de los modelos observados
     * @param blankPos Posicion de la pieza vacía
     * @param movedPos Posicion de la pieza a mover
     */
    public void update(int blankPos,int movedPos);
}
