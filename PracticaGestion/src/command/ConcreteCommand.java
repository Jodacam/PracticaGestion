/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import java.util.Collection;
import java.util.Random;
import java.util.Stack;
import view.PuzzleGUI;

/**
 *
 * @author pablo
 */
public class ConcreteCommand implements Command {

    private Stack<Integer[]> movimientos;
    private Integer[] movimientoDeshecho;
    
    @Override
    public void undoCommand() {
        if(!movimientos.isEmpty()){
            Integer[] mov = movimientos.pop();
            PuzzleGUI.getInstance().getBoardView().update(mov[0],mov[1]);
            movimientoDeshecho = mov;
        }
    }
    
    public void resolverCommand() {
    	
    	while(!movimientos.isEmpty()){
        this.undoCommand();
    }	
    	
    }

    public ConcreteCommand() {
        movimientos = new Stack<>();
        movimientoDeshecho = new Integer[2];
    }

    @Override
    public void redoCommand() {
        if(movimientoDeshecho[0]!=null){
            PuzzleGUI.getInstance().getBoardView().movePiece(movimientoDeshecho[0],movimientoDeshecho[1]);
            movimientoDeshecho[0] = null;
            movimientoDeshecho[1] = null;
        }
    }
    
    public void setMovimiento(Integer[] movimiento){
        movimientos.push(movimiento);
    }
    
    public Stack<Integer[]> getMovimientos(){
        return movimientos;
    }
    
    public void desordenarCommand() {
    	Random r = new Random();
		for(int i=0;i<99;i++){
            int x = r.nextInt(96);
            int y = r.nextInt(96);
            int piezas[] = PuzzleGUI.getInstance().getBoardView().movePiece(x, y);
            PuzzleGUI.getInstance().getControler().notifyObservers(piezas[0],piezas[1]);      	
		}
    }
}
