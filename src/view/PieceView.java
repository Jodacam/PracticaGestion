package view;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ángel
 * @version 1.0
 */
public class PieceView extends ImageIcon implements Cloneable{

    //id de la imagen
    private int id;
    //índice de fila
    private int indexRow;
    //índice de columna
    private int indexColumn;
    //Tamaño de la imagen
    private int imageSize;


    /**
     * Constructor de una clase
     * @param indexRow indice de fila
     * @param indexColumn indice de columna
     * @param imagePath ubicación de la imagen.
     */
    public PieceView(int id,int indexRow, int indexColumn,int imageSize,String imagePath){
        super();
    }

    public PieceView(int id, int indexRow, int indexColumn,int imageSize,Image image){
        super();
    }


    public int getIndexRow() {
        return indexRow;
    }

    public int getIndexColumn() {
        return indexColumn;
    }


    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getId(){
        return this.id;
    }

    public String toString(){
        return("id:"+id);
    }

}
