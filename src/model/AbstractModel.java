package model;

import observer.Observer;

/**
 * Modelo abstracto de datos
 * @author Miguel Ángel
 * @version 1.0
 */
public abstract class AbstractModel<PieceModel> implements Observer {
    //número de filas
    protected int rowNum=0;
    //número de columnas
    protected int columnNum=0;
    //tamaño de la pieza
    protected int pieceSize=0;
    //lista de images
    protected String[] imageList=null;

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
}
