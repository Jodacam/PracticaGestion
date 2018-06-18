package model;

import com.google.gson.Gson;
import command.Command;
import command.MoveCommand;
import config.Config;
import config.LoadState;
import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import observer.Observer;

/**
 * Modelo abstracto de datos
 * @author Miguel Ángel
 * @version 1.0
 */
public abstract class AbstractModel implements Observer {
    //número de filas
    protected int rowNum=0;
    //número de columnas
    protected int columnNum=0;
    //tamaño de la pieza
    protected int pieceSize=0;
    //lista de images
    protected String[] imageList=null;
    
     ArrayList<PieceModel> iconArray = null;
     int blankPiece;
    
    public Config ActualConfig;
    public String gameName;

    public static final String ProyectDir = System.getProperty("user.dir");
    public static final String FileSeparator = System.getProperty("file.separator");
    
    static final Gson JSONMapper = new Gson();

    public static AbstractModel InstanciateModel(int rowNum, int columnNum,int pieceSize, String type){
    AbstractModel model = null;
    switch (type){
        case "Mongo":
            break;
        case "XML" :
             break;
        case "Local":
            model = new BoardModel(rowNum, columnNum, pieceSize);
            break;
        default:
            model = new BoardModel(rowNum, columnNum, pieceSize);
    }            
    return  model;
    }
    
    
    
    //constructor de la clase.
    public AbstractModel(int rowNum, int columnNum,int pieceSize, String[] imageList) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.pieceSize = pieceSize;
        this.imageList = imageList;
    }

    //constructor de la clase.
    public AbstractModel(int rowNum, int columnNum,int pieceSize) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.pieceSize = pieceSize;
        this.imageList = null;
    }

    
    
    
    
    /**
     * Añade una nueva pieza en el modelo
     * @param id identificador de la pieza
     * @param indexRow índice de fila donde se encuentra ubicada la pieza
     * @param indexCol índice de columna donde se encuentra ubidada la pieza
     * @param imagePath ubicación de la imagen.
     */
    public abstract void addNewPiece(int id, int indexRow, int indexCol, String imagePath);

    /**
     * Añade una nueva pieza en el modelo
     * @param id identificador de la pieza
     * @param indexRow índice de fila donde se encuentra ubicada la pieza
     * @param indexCol índice de columna donde se encuentra ubidada la pieza
     */
    public abstract void addNewPiece(int id, int indexRow, int indexCol);

    //comprueba si el puzzle ha sido solucionado
    public abstract boolean isPuzzleSolve();
    //genera movimientos aleatorios
    public abstract int[] getRandomMovement(int lastPos, int pos);

    public int getRowCount() {
        return rowNum;
    }

    public int getColumnCount() {
        return columnNum;
    }

    public int getPieceSize(){
        return pieceSize;
    }
     
   
    public void update(int blankPos, int movedPos) {
       if(blankPos !=99)
        {        
        PieceModel p = iconArray.get(movedPos);
        PieceModel p2 = iconArray.get(blankPos);
            int x = p.getIndexColumn();
            int y = p.getIndexRow();
            p.setPosition(p2.getIndexColumn(), p2.getIndexRow());
            p2.setPosition(x, y);       
            iconArray.set(blankPos, p);
            iconArray.set(movedPos, p2);
            blankPiece = movedPos;    
            
        }
        
    }
    
    
    
    public abstract boolean StoreAll(Deque<Command> list, File image);
    public abstract LoadState LoadFromDataBase();
    public abstract void AddMovement(MoveCommand command);
    public abstract MoveCommand RemoveMovement();
    public abstract void CloseDataBase();   
    public abstract void RemoveAllMovements(String id);
    
}
