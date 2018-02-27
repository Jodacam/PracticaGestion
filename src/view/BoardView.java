package view;

import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ángel
 * @version 1.0
 */
public class BoardView extends JPanel implements Observer {
    public static final int imageWidth= 96;
    public static final int imageHeight= 96;
    private ArrayList<PieceView> iconArray = null;
    private int blankPiece;

    public BoardView(int rowNum, int columnNum,int imageSize, String[] imageList){
        super();
        iconArray = new ArrayList();
        int leng = rowNum*columnNum;
        blankPiece = 0;
        for(int i = 0;i <leng; i++ ){
            PieceView p = new PieceView(i,i/columnNum,i%columnNum,imageSize,imageList[i]);
            iconArray.add(p);
        }
       

    }

    public BoardView(int rowNum, int columnNum, int imageSize, File imageFile){
        super();
    }

    //redimensionamos la imagen para 96*96
    private BufferedImage resizeImage(File fileImage){
        BufferedImage resizedImage = null;

        return(resizedImage);
    }

    //dividimos la imagen en el número
    private BufferedImage[] splitImage(BufferedImage image){
        //Divisor de imágenes
        BufferedImage images[] = null;
        return(images);
    }

    public void update(int blankPos, int movedPos){
        
        System.out.println("Board View: "+blankPos+", "+movedPos);
        /*PieceView p = iconArray.get(movedPos);
        PieceView p2 = iconArray.get(blankPos);
        
        
        int disx = p2.getIndexColumn() - p.getIndexColumn();
        int disy = p2.getIndexRow()-p.getIndexRow();             
        boolean inPlace = disx == 1 ? (disy == 0):(disx==-1 ? (disy==0):(disy==1 ? (disx==0):(disy==-1 ?  disx==0:false)));
        if( inPlace)
        {

            int x = p.getIndexColumn();
            int y = p.getIndexRow();
            p.setPosition(p2.getIndexColumn(), p2.getIndexRow());
            p2.setPosition(x, y);       
            iconArray.set(blankPos, p);
            iconArray.set(movedPos, p2);*/
            //blankPiece = movedPos;      
            this.repaint();
        //}
                   
    }

    public void update(Graphics g){
       
        paint(g);
    }

    public void paint(Graphics g){
        super.paint(g);
        //g.setColor(Color.BLACK);
        //g.fillOval(0, 0, imageWidth, imageHeight);
        for(PieceView iconImage:iconArray){
            g.drawImage(iconImage.getImage(), iconImage.getIndexColumn()*iconImage.getIconWidth(), iconImage.getIndexRow()*iconImage.getIconHeight(), iconImage.getImageSize(), iconImage.getImageSize(), this);
        }
    }

    //Dado una posicion X e Y localizar una pieza
    private int locatePiece(int posX,int posY){
        System.out.println("BoardView locatePiece: "+posX+", "+posY);//texto para debuggear
        int posArray = (posX/iconArray.get(blankPiece).getIconWidth())+(posY/iconArray.get(blankPiece).getIconHeight()*3);
        return posArray;
    }

    /**
     * Mueve la pieza y devuelve las coordenadas en un array de dos posiciones
     * donde: la primera posicion representa la posicion actual de la pieza blanca
     * y la segunda posicion representa la posicion actual de la pieza a mover.
     * @param posX posicion X del puntero
     * @param posY posicion Y del puntero.
     * @return Array de dos posiciones: posicion actual de la pieza blanca y posicion
     * actual de la pieza que tiene que ser movida.
     */
    public int[] movePiece(int posX,int posY){
        
        int piezas[] = new int[2];
        PieceView blankPieces = iconArray.get(blankPiece);
        
        //piezas[0] = blankPieces.getIndexRow()*3 + blankPieces.getIndexColumn();
        piezas[0] = blankPiece;
        piezas[1] = locatePiece(posX,posY);
        
        PieceView p = iconArray.get(piezas[1]);
        PieceView p2 = iconArray.get(piezas[0]);
        
        
        int disx = Math.abs(p2.getIndexColumn() - p.getIndexColumn());
        int disy = Math.abs(p2.getIndexRow()-p.getIndexRow());             
        //boolean inPlace = disx == 1 ? (disy == 0):(disx==-1 ? (disy==0):(disy==1 ? (disx==0):(disy==-1 ?  disx==0:false)));
        if( /*inPlace*/disx + disy == 1)
        {
            System.out.println("WIIII");
            int x = p.getIndexColumn();
            int y = p.getIndexRow();
            p.setPosition(p2.getIndexColumn(), p2.getIndexRow());
            p2.setPosition(x, y);       
            iconArray.set(piezas[0], p);
            iconArray.set(piezas[1], p2);
            blankPiece = piezas[1];      
            //this.repaint();
            return piezas;
        }
        
        return piezas;
    }

}
