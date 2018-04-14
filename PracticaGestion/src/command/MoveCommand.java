/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import control.AbstractController;
import control.Controller;
import java.util.Collection;
import java.util.Random;
import java.util.Stack;
import view.PuzzleGUI;

/**
 *
 * @author pablo
 */
public class MoveCommand implements Command {

    //private Controller control;
    public int[] Movimiento;
    private transient Random r = new Random();
    private transient AbstractController controller;
    
    @Override
    public void undoCommand() {        
    	controller.notifyObservers(Movimiento[1],Movimiento[0]);        
    }      
    public MoveCommand(int [] piezas,AbstractController c) {
    	Movimiento = piezas;
        controller = c;
    }    
    @Override
    public void redoCommand() {            
        controller.notifyObservers(Movimiento[0], Movimiento[1]);           
    }
    
    

}
