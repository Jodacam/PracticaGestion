/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import control.Controller;
import java.util.Collection;
import java.util.Random;
import java.util.Stack;
import view.PuzzleGUI;

/**
 *
 * @author pablo
 */
public class ConcreteCommand implements Command {


    private Controller control;
    Random r = new Random();
    @Override
    public void undoCommand() {
        if(!control.movimientos.isEmpty()){
            Integer[] mov = control.movimientos.pop();
            control.notifyObservers(mov[0], mov[1]);
           control.movimientoDeshecho = mov;
        }
    }      

    public ConcreteCommand(Controller a) {
        control = a;

    }

    @Override
    public void redoCommand() {
        
            
            int x = r.nextInt(96);
            int y = r.nextInt(96);
            int[] movimiento = PuzzleGUI.getInstance().getBoardView().movePiece(x,y);
            setMovimiento(movimiento);
            control.notifyObservers(movimiento[0], movimiento[1]);           
        
    }
    
    private void setMovimiento(int[] movimiento){
        Integer[] l = new Integer[2];
        l[0] = movimiento[1];
        l[1] = movimiento[0];
        control.movimientos.push(l);
    }
    

}
