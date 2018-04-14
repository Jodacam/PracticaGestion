/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import command.Command;
import command.MoveCommand;
import config.ConfigLoader;
import config.LoadState;
import model.BoardModel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import view.InfoView;
import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
public class Controller extends AbstractController {
    //HahsMap que incluye todas las funciones a realizar mediante eventos.

    private Map<String, Function> EventsFunctions = new HashMap<>();

    //Inicializamos el Controlador
    public Controller() {
        super();
        movimientos = new ConcurrentLinkedDeque<>();
        //Se generan las funciones a ejecutar cuando se produzca un evento.
        EventsFunctions.put("clutter", (String[] param) -> {

            for (int i = 0; i < 40; i++) {
                if (!movimientos.isEmpty()) {
                    MoveCommand movimientoAnterior = (MoveCommand) movimientos.getFirst();                                       
                    int[] movimiento = this.getViewFromObservers().getRandomMovement(movimientoAnterior.Movimiento[0], movimientoAnterior.Movimiento[1]);
                    notifyObservers(movimiento[0],movimiento[1]);
                    movimientos.push(new MoveCommand(movimiento,this));
                } else {
                    
                   int[] movimiento = this.getViewFromObservers().getRandomMovement(1,0);
                    notifyObservers(movimiento[0],movimiento[1]);
                    movimientos.push(new MoveCommand(movimiento,this));
                }
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
            ConfigLoader.getInstance().getActualConfig().setGameName(null);
            File img = PuzzleGUI.getInstance().showFileSelector();
            if(img != null){
                PuzzleGUI.getInstance().updateBoard(img);
                this.ReStartModel();
                notifyObservers(99, 99);
            }
        });

        EventsFunctions.put("save", (String[] param) -> {
            ConfigLoader.getInstance().SaveGame(movimientos, PuzzleGUI.getInstance().getBoardView().getImage());
        });

        EventsFunctions.put("load", (String[] param) -> {
            LoadState state = ConfigLoader.getInstance().Load();
            if (0 != state.getImagePath().compareTo("default")) {
                PuzzleGUI.getInstance().CreateNewBoard(new File(ConfigLoader.ProyectDir + state.getImagePath()));
                this.ReStartModel();
            } else {
                PuzzleGUI.getInstance().setConfig(state.getConfig());
                PuzzleGUI.getInstance().LoadDefaultBoard();
                this.ReStartModel();
            }

            while (!state.getCommand().isEmpty()) {
                Command d = state.getCommand().pollLast();
                d.redoCommand();
                movimientos.push(d);
            }

        });

        EventsFunctions.put("exit", (String[] param) -> {
            System.exit(0);
        });

        EventsFunctions.put("info", (String[] param) -> {
            InfoView info = new InfoView();
        });

    }

    Random r = new Random();

    //Evento de botones
    @Override
    public void actionPerformed(ActionEvent ae) {

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
        for (int i = 0; i < observerList.size(); i++) {
            observerList.get(i).update(blankPos, movedPos); 
        }															
    }

    public void mouseClicked(MouseEvent me) {// Metodo que recibe el evento de click del raton
        System.out.println(me.getX() + ", " + me.getY());
        int x = me.getX();
        int y = me.getY();
        int imageSize = ConfigLoader.getInstance().getActualConfig().getImageSize();
        if (x < imageSize * ConfigLoader.getInstance().getActualConfig().getNumColumn() && y < imageSize * ConfigLoader.getInstance().getActualConfig().getNumRow()) {           
            int piezas[] = getViewFromObservers().movePiece(x, y);
            notifyObservers(piezas[0], piezas[1]);
            movimientos.push(new MoveCommand(piezas,this));
        }
    }

    private BoardView getViewFromObservers(){
         return  (BoardView) observerList.stream().filter(a -> (a.getClass() == BoardView.class)).findFirst().get();
    }
    
    
    private void ReStartModel() {
        BoardModel model = (BoardModel) observerList.stream().filter(a -> (a.getClass() == BoardModel.class)).findFirst().get();
        observerList.remove(model);
        model = new BoardModel(ConfigLoader.getInstance().getActualConfig().getNumRow(), ConfigLoader.getInstance().getActualConfig().getNumColumn(),
                ConfigLoader.getInstance().getActualConfig().getImageSize());
        this.addObserver(model);
    }

}
