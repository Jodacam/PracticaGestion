package command;

/**
 * Clase que representa un comando que nos permite implementar
 * las funciones de undo y redo.
 * @author Miguel Ángel
 * @version 1.0
 */
public interface Command {
    public void undoCommand();
    public void redoCommand();
}
