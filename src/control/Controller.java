/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import view.BoardView;
import view.PuzzleGUI;

/**
 *
 * @author pablo
 */
public class Controller extends AbstractController {

    @Override
    public void actionPerformed(ActionEvent ae) {
         //To change body of generated methods, choose Tools | Templates.
         System.out.println(ae.getActionCommand());//devuelve un string dependiendo del boton que se pulse
         System.out.println(ae.getID());
         if(ae.getActionCommand() == "clutter"){
            
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
            notifyObservers(piezas[0],piezas[1]);                
        }
    }
    
    
    
}
