/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import command.Command;
import command.ConcreteCommand;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import view.BoardView;
import view.PuzzleGUI;

/**
 *
 * @author pablo
 */
public class Controller extends AbstractController {
    
        public Stack<Integer[]> movimientos;
        public Integer[] movimientoDeshecho;
	private Map<String, Function> EventsFunctions = new HashMap<>();
	private Map<String, Command> Commands = new HashMap<>();
	public Controller() {
            super();
            movimientos = new Stack();
            movimientoDeshecho = new Integer[2];           
            Commands.put("MovimientoAleatorio",new ConcreteCommand(this));               
	    EventsFunctions.put("clutter", (String[] param)->{			 
                         for(int i=0;i<99;i++){     
                            Commands.get("MovimientoAleatorio").redoCommand();
		}
            			
		} );                                                		
		EventsFunctions.put("solve",(String[] param)->{			
	                while(!movimientos.isEmpty()){
                               Commands.get("MovimientoAleatorio").undoCommand();        
			}
                    }
		);
        }
	
		
		
	
	
	
	
    Random r = new Random();
    @Override
    public void actionPerformed(ActionEvent ae) {
        //To change body of generated methods, choose Tools | Templates.
        System.out.println(ae.getActionCommand());//devuelve un string dependiendo del boton que se pulse
        System.out.println(ae.getID());
        try {
        	EventsFunctions.get(ae.getActionCommand()).ExecuteAction(null);
		
		} catch (NullPointerException e) {
			System.out.println("No implementado");
		}
        
        
        
    }

    @Override
    public void notifyObservers(int blankPos, int movedPos) {
        for(int i=0; i<observerList.size(); i++)
            observerList.get(i).update(blankPos, movedPos); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void mouseClicked(MouseEvent me){//Metodo que recibe el evento de click del raton
        System.out.println(me.getX()+", "+me.getY());
        int x = me.getX();
        int y = me.getY();
        if(x < PuzzleGUI.getInstance().getBoardView().imageWidth && y < PuzzleGUI.getInstance().getBoardView().imageWidth){
            int piezas[] = PuzzleGUI.getInstance().getBoardView().movePiece(x, y);
            movimientos.push(new Integer[]{piezas[1],piezas[0]});
            notifyObservers(piezas[0],piezas[1]);                
        }
    }
    
    
    
}
