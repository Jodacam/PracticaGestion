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
public class MoverCommand implements Command {

    //private Controller control;
    private int[] Movimiento;
    private transient Random r = new Random();
    
    
    @Override
    public void undoCommand() {        
    	PuzzleGUI.controller.notifyObservers(Movimiento[0],Movimiento[1]);        
    }      

    public MoverCommand(int x, int y) {
    	Movimiento = new int[2];
    	int piezas[] = PuzzleGUI.getInstance().getBoardView().movePiece(x, y);
    	Movimiento[1] = piezas[0];
    	Movimiento[0] = piezas[1];
    	PuzzleGUI.controller.notifyObservers(piezas[0], piezas[1]);
    }

    @Override
    public void redoCommand() {            
        PuzzleGUI.controller.notifyObservers(Movimiento[1], Movimiento[0]);           
    }
    
    

}
