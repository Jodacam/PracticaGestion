/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.sun.media.sound.ModelAbstractChannelMixer;
import command.Command;
import command.MoveCommand;

import config.LoadState;
import model.BoardModel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import model.AbstractModel;
import view.BoardView;
import view.InfoView;
import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
public class Controller extends AbstractController {
    //HahsMap que incluye todas las funciones a realizar mediante eventos.

    private Map<String, Function> EventsFunctions = new HashMap<>();
    private BoardView viewInstance;
    private AbstractModel modelInstance;
    private long totalTimeInsert;
    private long totalTimeRemove;

    //Inicializamos el Controlador
    public Controller(AbstractModel m) {
        super();
        modelInstance = m;
        movimientos = new ConcurrentLinkedDeque<>();
        //Se generan las funciones a ejecutar cuando se produzca un evento.
        EventsFunctions.put("clutter", (String[] param) -> {

            String movS = PuzzleGUI.getInstance().GetFromPanel("Cuantos movimientos deseas realizar para desordenar", null);
            int movI = Integer.parseInt(movS);
            for (int i = 0; i < movI; i++) {
                if (!movimientos.isEmpty()) {
                    MoveCommand movimientoAnterior = (MoveCommand) movimientos.getFirst();
                    int[] movimiento = viewInstance.getRandomMovement(movimientoAnterior.Movimiento[0], movimientoAnterior.Movimiento[1]);
                    notifyObservers(movimiento[0], movimiento[1]);
                    MoveCommand dCommand = new MoveCommand(movimiento, this);
                    long startTime = System.currentTimeMillis();
                    modelInstance.AddMovement(dCommand);
                    long endTime = System.currentTimeMillis() - startTime;
                    System.out.println(endTime);
                    totalTimeInsert += endTime;
                    movimientos.push(dCommand);
                } else {

                    int[] movimiento = viewInstance.getRandomMovement(1, 0);
                    notifyObservers(movimiento[0], movimiento[1]);
                    MoveCommand dCommand = new MoveCommand(movimiento, this);
                    modelInstance.AddMovement(dCommand);
                    movimientos.push(dCommand);
                }
            }
            PuzzleGUI.getInstance().ShowMessage("Tiempo Medio de insercion en "  + ": " + totalTimeInsert / movI);
            PuzzleGUI.getInstance().UpdateTiempo(totalTimeInsert / movI);
            totalTimeInsert = 0;

        });

        EventsFunctions.put("solve", (String[] param) -> {
            int size = movimientos.size();
            while (!movimientos.isEmpty()) {
                long startTime = System.currentTimeMillis();
                MoveCommand moveCommand = modelInstance.RemoveMovement();
                long endTime = System.currentTimeMillis() - startTime;
                System.out.println(endTime);
                totalTimeRemove += endTime;

                if (moveCommand != null) {
                    moveCommand.controller = this;
                    movimientos.pop();
                } else {
                    moveCommand = (MoveCommand) movimientos.pop();
                }
                 
                moveCommand.undoCommand();

            }
            PuzzleGUI.getInstance().ShowMessage("Has Ganado!");
            PuzzleGUI.getInstance().ShowMessage("Tiempo Medio de recogida en " + modelInstance.gameName + ": " + totalTimeRemove / size);
            PuzzleGUI.getInstance().UpdateTiempo(totalTimeRemove / size);
            totalTimeRemove = 0;
        });

        EventsFunctions.put("loadImage", (String[] param) -> {
            while (!movimientos.isEmpty()) {
                movimientos.pop();
            }
            modelInstance.gameName = null;
            File img = PuzzleGUI.getInstance().showFileSelector();
            if (img != null) {
                String inputString = PuzzleGUI.getInstance().GetFromPanel("Choose a number of rows, the number must be equal or higgher than 3",null);
                int input = Integer.parseInt(inputString);
                int rowNum;
                if (input >= 3) {
                    rowNum = input;
                } else {
                    rowNum = 3;
                }
                int columnNum;
                inputString = PuzzleGUI.getInstance().GetFromPanel( "Choose a number of columns, the number must be equal or higgher than 3",null);
                input = Integer.parseInt(inputString);
                if (input >= 3) {
                    columnNum = input;
                } else {
                    columnNum = 3;
                }
                int imageSize;
                inputString = PuzzleGUI.getInstance().GetFromPanel("Choose the piece size, the number must be equal or higgher than 32, all pieces are squares",null);
                input = Integer.parseInt(inputString);
                if (input >= 32) {
                    imageSize = input;
                } else {
                    imageSize = 32;
                }                               
                PuzzleGUI.getInstance().updateBoard(img,rowNum,columnNum,imageSize);
                modelInstance.CloseDataBase();               
                observerList.remove(modelInstance);
                modelInstance = AbstractModel.InstanciateModel(rowNum, columnNum, imageSize, modelInstance.getType());             
                observerList.add(modelInstance);
                setViewFromObservers();
                notifyObservers(99, 99);
            }
        });

        EventsFunctions.put("save", (String[] param) -> {
            if (modelInstance.StoreAll(movimientos,
                    viewInstance.getImage())) {
                PuzzleGUI.getInstance().ShowMessage("Partida Guardada");
            } else {
                PuzzleGUI.getInstance().ShowMessage("No se ha podido Guardar la partida. Nombre ya elegido o nombre vacio");
            }
        });

        EventsFunctions.put("load", (String[] param) -> {
            long startTime = System.currentTimeMillis();
            LoadState state = modelInstance.LoadFromDataBase();
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println(endTime);           
            if (state != null) {
               
                this.LoadMovement(state);
            } else {
                PuzzleGUI.getInstance().ShowMessage("Partida no encontrada");
            }

        });

        EventsFunctions.put("exit", (String[] param) -> {
            System.exit(0);
        });

        EventsFunctions.put("info", (String[] param) -> {
            InfoView info = new InfoView();
        });
       

        EventsFunctions.put("Mongo", (String[] param) -> {
           modelInstance = modelInstance.ChangeDataBase("Mongo");
        });
        EventsFunctions.put("XML", (String[] param) -> {
            modelInstance = modelInstance.ChangeDataBase("XML");
        });
        
        EventsFunctions.put("Local", (String[] param) -> {
            modelInstance = modelInstance.ChangeDataBase("Local");
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

        int x = me.getX();
        int y = me.getY();
        int imageSize = modelInstance.getPieceSize();
        if (x < imageSize * modelInstance.getColumnCount() && y < imageSize * modelInstance.getRowCount()) {
            int piezas[] = viewInstance.movePiece(x, y);
            notifyObservers(piezas[0], piezas[1]);
            MoveCommand m = new MoveCommand(piezas, this);
            long startTime = System.currentTimeMillis();
            modelInstance.AddMovement(m);
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println(endTime);
            PuzzleGUI.getInstance().UpdateTiempo(endTime);
            PuzzleGUI.getInstance().UpdateMemoria((int)modelInstance.ObtainSize()/1024);
            movimientos.push(m);
            if (modelInstance.isPuzzleSolve()) {
                PuzzleGUI.getInstance().ShowMessage("Has Ganado!");
                movimientos.clear();
                modelInstance.RemoveAllMovements(modelInstance.gameName);
            }
        }
    }

    public void setViewFromObservers() {
        viewInstance = (BoardView) observerList.stream().filter(a -> (a.getClass() == BoardView.class)).findFirst().get();
    }

//  
    private void LoadMovement(LoadState state) {
        if (state == null) {
            PuzzleGUI.getInstance().ShowMessage("No has cargado ninguna partida");
            return;
        }
        movimientos.clear();
        if (0 != state.getImagePath().compareTo("default")) {
            PuzzleGUI.getInstance().CreateNewBoard(new File(modelInstance.ProyectDir + state.getImagePath()));
            setViewFromObservers();

        } else {
            PuzzleGUI.getInstance().setConfig(state.getConfig());
            PuzzleGUI.getInstance().LoadDefaultBoard();
            setViewFromObservers();

        }

        while (!state.getCommand().isEmpty()) {
            MoveCommand d = state.getCommand().pollLast();
            d = new MoveCommand(d.Movimiento, this);
            d.redoCommand();
            movimientos.push(d);
        }
    }

}
