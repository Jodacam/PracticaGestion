package view;

import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ángel
 * @version 1.0
 */
public class BoardView extends JPanel implements Observer {
    public static final int imageWidth= 64*3;
    public static final int imageHeight= 64*3;
    private ArrayList<PieceView> iconArray = null;
    private int blankPiece;
    private int imageSize;
    public BoardView(int rowNum, int columnNum,int imageSize, String[] imageList){
        super();
        this.imageSize = imageSize;
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
        this.imageSize = imageSize;
        iconArray = new ArrayList<>();
        BufferedImage b = resizeImage(imageFile);
        Image[]imageList = splitImage(b);
        blankPiece = 0;
        int leng = rowNum*columnNum;
        String fileSeparator = System.getProperty("file.separator");
        String imagePath=System.getProperty("user.dir")+fileSeparator+"resources"+fileSeparator+"blank.gif";  
                       
        PieceView e = new PieceView(0,0,0,imageSize,imagePath);
        iconArray.add(e);
        for(int i = 1;i < leng; i++ ){
            PieceView p = new PieceView(i,i/columnNum,i%columnNum,imageSize,imageList[i]);
            iconArray.add(p);
        }
    }

    //redimensionamos la imagen para 96*96
    private BufferedImage resizeImage(File fileImage){
        BufferedImage image = null;               
        try {
            image = ImageIO.read(fileImage);
             ImageIO.write(image, "jpg", new File("fotoSinresice.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage resizedImage = new BufferedImage(imageWidth,imageHeight,image.getType());
        
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, imageWidth, imageHeight, null);
        g2d.dispose();        
        try {
            ImageIO.write(resizedImage, "jpg", new File("fotoResize.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return(resizedImage);
    }

    //dividimos la imagen en el número
    private BufferedImage[] splitImage(BufferedImage image){
        //Divisor de imágenes

        BufferedImage images[] = new BufferedImage[PuzzleGUI.getInstance().columnNum*PuzzleGUI.getInstance().rowNum];
        
        for(int i =0; i < PuzzleGUI.rowNum; i++){
            for(int j =0; j < PuzzleGUI.columnNum; j++){
                BufferedImage pieza = image.getSubimage(j*imageSize, i*imageSize, imageWidth/3, imageHeight/3);
                try {
                    ImageIO.write(pieza, "jpg", new File("foto"+i*PuzzleGUI.columnNum+j+".jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*try {
                    ImageIO.write(pieza, "jpg", archivoPieza);
                } catch (IOException ex) {
                    Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);                }*/
                images[i*PuzzleGUI.columnNum+j] = pieza;
            }
        }
        
        return(images);
    }

    public void update(int blankPos, int movedPos){
        
        if(blankPos !=99)
        {
        System.out.println("Board View: "+blankPos+", "+movedPos);
        PieceView p = iconArray.get(movedPos);
        PieceView p2 = iconArray.get(blankPos);

            int x = p.getIndexColumn();
            int y = p.getIndexRow();
            p.setPosition(p2.getIndexColumn(), p2.getIndexRow());
            p2.setPosition(x, y);       
            iconArray.set(blankPos, p);
            iconArray.set(movedPos, p2);
            blankPiece = movedPos;    
            //Integer[] pos = {movedPos,blankPos};
            //PuzzleGUI.getInstance().getCommand().setMovimiento(pos);
            
        }
        this.repaint();
                   
    }

    public void update(Graphics g){
        paint(g);
    }

    public void paint(Graphics g){
        super.paint(g);
        //g.setColor(Color.BLACK);
        //g.fillOval(0, 0, imageWidth, imageHeight);
        for(PieceView iconImage:iconArray){
            g.drawImage(iconImage.getImage(), iconImage.getIndexColumn()*64, iconImage.getIndexRow()*64, iconImage.getImageSize(), iconImage.getImageSize(), this);
            System.out.println("pintamos");
        }
    }

    //Dado una posicion X e Y localizar una pieza
    private int locatePiece(int posX,int posY){
        System.out.println("BoardView locatePiece: "+posX+", "+posY);//texto para debuggear
        int posArray = (posX/64)+(posY/64*3);
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
        piezas[0] = blankPiece;
        piezas[1] = locatePiece(posX,posY);
        
        PieceView p = iconArray.get(piezas[1]);
        PieceView p2 = iconArray.get(piezas[0]);
        
        
        int disx = Math.abs(p2.getIndexColumn() - p.getIndexColumn());
        int disy = Math.abs(p2.getIndexRow()-p.getIndexRow());             
        if( /*inPlace*/disx + disy == 1)
        {
            Integer[] pos = {piezas[1],piezas[0]};
        
            return piezas;
        }else{
            piezas[0] = 99;
            piezas[1] = 99;
            return piezas;
        }
        
        
    }

}
