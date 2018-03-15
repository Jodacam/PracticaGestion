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

            for (int i = 0; i < PuzzleGUI.rowNum * PuzzleGUI.columnNum; i++) {
                if (!movimientos.isEmpty()) {
                    MoveCommand movimientoAnterior = (MoveCommand) movimientos.getFirst();
                    movimientos.push(new MoveCommand(movimientoAnterior.Movimiento[1], movimientoAnterior.Movimiento[0], true));
                } else {
                    movimientos.push(new MoveCommand(1, 0, true));
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
        if (x < PuzzleGUI.imageSize * PuzzleGUI.columnNum && y < PuzzleGUI.imageSize * PuzzleGUI.rowNum) {
            movimientos.push(new MoveCommand(x, y));
        }
    }

    private void ReStartModel() {
        BoardModel model = (BoardModel) observerList.stream().filter(a -> (a.getClass() == BoardModel.class)).findFirst().get();
        observerList.remove(model);
        model = new BoardModel(ConfigLoader.getInstance().getActualConfig().getNumRow(), ConfigLoader.getInstance().getActualConfig().getNumColumn(),
                ConfigLoader.getInstance().getActualConfig().getImageSize());
        this.addObserver(model);
    }

}
