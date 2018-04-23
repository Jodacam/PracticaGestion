package observer;


/**
 * Interfaz que gestiona los elementos observados
 * @author Miguel Ángel
 * @version 1.0
 */
public interface Observable {

    public void removeObserver(Observer observer);
    public void addObserver(Observer observer);
    public void notifyObservers(int blankPos,int movedPos);
}
