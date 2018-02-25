package control;

import observer.Observable;
import observer.Observer;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

/**
 * Interfaz que tiene que ser implementada por un controlador.
 * @author Miguel Ángel
 * @version 1.0
 */
public abstract class AbstractController extends MouseAdapter implements ActionListener, Observable {
    protected ArrayList<Observer> observerList;
    public AbstractController(){
        observerList = new ArrayList<Observer>();
    }

    public void addObserver(Observer observer){
        if(observer!=null){
            observerList.add(observer);
        }
    }

    public void removeObserver(Observer observer){
        if(observer!=null){
            observerList.remove(observer);
        }
    }

}
