/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import command.Command;
import command.MoverCommand;
import config.ConfigLoader;
import config.LoadState;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import view.BoardView;
import view.PuzzleGUI;

/**
 *
 * @author pablo
 */
public class Controller extends AbstractController {

	

	private Map<String, Function> EventsFunctions = new HashMap<>();

	public Controller() {
		super();
		movimientos = new ConcurrentLinkedDeque<>();
		EventsFunctions.put("clutter", (String[] param) -> {
			for (int i = 0; i < 200; i++) {
				int x = r.nextInt(PuzzleGUI.imageSize*PuzzleGUI.columnNum);
				int y = r.nextInt(PuzzleGUI.imageSize*PuzzleGUI.rowNum);
				movimientos.push(new MoverCommand(x, y));
			}

		});
		EventsFunctions.put("solve", (String[] param) -> {
			while (!movimientos.isEmpty()) {
					movimientos.pop().undoCommand();
			}
		});
                
                EventsFunctions.put("loadImage", (String[] param) -> {
			while (!movimientos.isEmpty()) {
					movimientos.pop();
			}
                        File img = PuzzleGUI.getInstance().showFileSelector();
                        PuzzleGUI.getInstance().updateBoard(img);
                        notifyObservers(99, 99);
		});
                
                EventsFunctions.put("save",(String[] param)->{                  
                    ConfigLoader.SaveGame(movimientos,PuzzleGUI.getInstance().getBoardView().getImage());                   
                });
                
                EventsFunctions.put("load", (String[] param)->{
                   LoadState state = ConfigLoader.Load();
                   if(0 != state.getImagePath().compareTo("default"))
                    PuzzleGUI.getInstance().CreateNewBoard(new File(ConfigLoader.ProyectDir+state.getImagePath()));
                   
                   while(!state.getCommand().isEmpty()){ 
                   Command d = state.getCommand().pollLast();
                   d.redoCommand();
                   movimientos.push(d);
                   }
                   
                   
                   
                });
                
                
                
                
	}

	Random r = new Random();

	@Override
	public void actionPerformed(ActionEvent ae) {
		// To change body of generated methods, choose Tools | Templates.
		System.out.println(ae.getActionCommand());// devuelve un string dependiendo del boton que se pulse
		System.out.println(ae.getID());
                
		try {
			EventsFunctions.get(ae.getActionCommand()).ExecuteAction(null);

		} catch (NullPointerException e) {
                    e.printStackTrace();
			System.out.println("No implementado");
		}
                

	}

	@Override
	public void notifyObservers(int blankPos, int movedPos) {
		for (int i = 0; i < observerList.size(); i++)
			observerList.get(i).update(blankPos, movedPos); // To change body of generated methods, choose Tools |
															// Templates.
	}

	public void mouseClicked(MouseEvent me) {// Metodo que recibe el evento de click del raton
		System.out.println(me.getX() + ", " + me.getY());
		int x = me.getX();
		int y = me.getY();
		if (x < PuzzleGUI.imageSize*PuzzleGUI.columnNum
                    && y < PuzzleGUI.imageSize*PuzzleGUI.rowNum) {			
			movimientos.push(new MoverCommand(x, y));
		}
	}

}
