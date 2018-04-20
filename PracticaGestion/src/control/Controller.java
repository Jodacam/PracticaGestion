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
    private BoardModel modelInstance;
    //Inicializamos el Controlador
    public Controller(BoardModel m) {
        super();
        modelInstance = m;       
        movimientos = new ConcurrentLinkedDeque<>();
        //Se generan las funciones a ejecutar cuando se produzca un evento.
        EventsFunctions.put("clutter", (String[] param) -> {

            for (int i = 0; i < 40; i++) {
                if (!movimientos.isEmpty()) {
                    MoveCommand movimientoAnterior = (MoveCommand) movimientos.getFirst();                                       
                    int[] movimiento = viewInstance.getRandomMovement(movimientoAnterior.Movimiento[0], movimientoAnterior.Movimiento[1]);
                    notifyObservers(movimiento[0],movimiento[1]);
                    MoveCommand dCommand = new MoveCommand(movimiento,this);
                    long startTime = System.currentTimeMillis();
                    ConfigLoader.getInstance().SaveMovement(dCommand);
                    long endTime = System.currentTimeMillis() - startTime;
                    System.out.println(endTime);
                    movimientos.push(dCommand);
                } else {
                    
                   int[] movimiento = viewInstance.getRandomMovement(1,0);
                    notifyObservers(movimiento[0],movimiento[1]);
                    MoveCommand dCommand = new MoveCommand(movimiento,this);
                    ConfigLoader.getInstance().SaveMovement(dCommand);
                    movimientos.push(dCommand);
                }
            }

        });
        EventsFunctions.put("solve", (String[] param) -> {
            while (!movimientos.isEmpty()) {
            	long startTime = System.currentTimeMillis();
                MoveCommand moveCommand = ConfigLoader.getInstance().DeleteMovement();
                long endTime = System.currentTimeMillis() - startTime;
                System.out.println(endTime);
                if(moveCommand == null)
                	movimientos.pop().undoCommand();
                else {
                	movimientos.pop();
                	moveCommand.controller = this;
                	moveCommand.undoCommand();               	
                }               	
            }
            PuzzleGUI.getInstance().mensajeVictoria();
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
                setViewFromObservers();
                notifyObservers(99, 99);
            }
        });

        EventsFunctions.put("save", (String[] param) -> {
            ConfigLoader.getInstance().SaveGame(movimientos,viewInstance.getImage());
        });
        
        
		EventsFunctions.put("saveInDataBase", (String[] param) -> {
			if (ConfigLoader.getInstance().SaveInDataBase( movimientos,
					viewInstance.getImage())) {
				System.out.println("Partida Guardada");
			} else {
				 System.out.println("Partida Ya existente, por favor seleccione otro nombre");
			}
		});
        
        

        EventsFunctions.put("load", (String[] param) -> {
            LoadState state = ConfigLoader.getInstance().Load();
            this.LoadMovement(state);

        });

        EventsFunctions.put("exit", (String[] param) -> {
            System.exit(0);
        });

        EventsFunctions.put("info", (String[] param) -> {
            InfoView info = new InfoView();
        });
        
        EventsFunctions.put("loadDataBase", (String[] param) -> {
            String loadName = PuzzleGUI.getInstance().GetNameFromPanel();
            long startTime = System.currentTimeMillis();
            LoadState state = ConfigLoader.getInstance().LoadFromDataBase(loadName);
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println(endTime);
            if(state != null){
                
            	ConfigLoader.getInstance().SetNewConfig(state.getConfig());
                this.LoadMovement(state);
            }else{
                System.out.println("Partida No encontrada");
            }
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
        int imageSize = ConfigLoader.getInstance().getActualConfig().getImageSize();
        if (x < imageSize * ConfigLoader.getInstance().getActualConfig().getNumColumn() && y < imageSize * ConfigLoader.getInstance().getActualConfig().getNumRow()) {           
            int piezas[] = viewInstance.movePiece(x, y);
            notifyObservers(piezas[0], piezas[1]);
            MoveCommand m  = new MoveCommand(piezas,this);
            long startTime = System.currentTimeMillis();
            ConfigLoader.getInstance().SaveMovement(m);           
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println(endTime);
            movimientos.push(m);
            if (modelInstance.isPuzzleSolve()) {
            	PuzzleGUI.getInstance().mensajeVictoria();
            }
        }
    }

    public void setViewFromObservers(){
        viewInstance =  (BoardView) observerList.stream().filter(a -> (a.getClass() == BoardView.class)).findFirst().get();
   }
    
    
    private void ReStartModel() {      
        observerList.remove(modelInstance);
        modelInstance = new BoardModel(ConfigLoader.getInstance().getActualConfig().getNumRow(), ConfigLoader.getInstance().getActualConfig().getNumColumn(),
                ConfigLoader.getInstance().getActualConfig().getImageSize());
        this.addObserver(modelInstance);
    }
    
    private void LoadMovement(LoadState state) {
        if (0 != state.getImagePath().compareTo("default")) {
            PuzzleGUI.getInstance().CreateNewBoard(new File(ConfigLoader.ProyectDir + state.getImagePath()));
            setViewFromObservers();
            this.ReStartModel();
        } else {
            PuzzleGUI.getInstance().setConfig(state.getConfig());
            PuzzleGUI.getInstance().LoadDefaultBoard();
            setViewFromObservers();
            this.ReStartModel();
        }

        while (!state.getCommand().isEmpty()) {
            MoveCommand d = state.getCommand().pollLast();
            d = new MoveCommand(d.Movimiento, this);
            d.redoCommand();
            movimientos.push(d);
        }
    }

}
