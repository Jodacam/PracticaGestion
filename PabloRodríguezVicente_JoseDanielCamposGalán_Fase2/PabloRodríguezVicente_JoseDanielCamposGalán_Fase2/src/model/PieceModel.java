/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author pablo
 */
public class PieceModel {
    
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
    public PieceModel(int id,int indexRow, int indexColumn,int imageSize,String imagePath){
        this.id = id;
        this.indexRow = indexRow;
        this.indexColumn = indexColumn;
        this.imageSize = imageSize;
    }

    public PieceModel(int id, int indexRow, int indexColumn,int imageSize){
        this.id = id;
        this.indexRow = indexRow;
        this.indexColumn = indexColumn;
        this.imageSize = imageSize;
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
    
    
    public void setPosition(int colum,int row){
        indexColumn = colum;
        indexRow = row;
    }
}
