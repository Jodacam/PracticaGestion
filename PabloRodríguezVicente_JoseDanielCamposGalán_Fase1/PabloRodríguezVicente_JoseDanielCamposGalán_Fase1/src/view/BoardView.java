package view;

import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Iterator;



/**
 * Clase que representa la vista del tablero
 * @author Miguel Ã�ngel
 * @version 1.0
 */
public class BoardView extends JPanel implements Observer {
    
    public static final int imageWidth= 96;
    public static final int imageHeight= 96;
    private ArrayList<PieceView> iconArray = null;
    private int blankPiece;
    private File image;
    private Random r = new Random();
    private int columnNum;
    private int rowNum;
    private int imageSize;

    public BoardView(int rowNum, int columnNum,int imageSize, String[] imageList){
        super();
        this.columnNum = columnNum;
        this.rowNum = rowNum;
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
        image = imageFile;
        this.columnNum = columnNum;
        this.rowNum = rowNum;
        this.imageSize = imageSize;
        iconArray = new ArrayList<>();
        BufferedImage b = resizeImage(imageFile);
        Image[] imageList = splitImage(b);
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
        BufferedImage resizedImage = null;
                         
        try {            
            image = ImageIO.read(fileImage);
        } catch (IOException ex) {
            Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
        }                  
        
        resizedImage = new BufferedImage(imageSize*columnNum,imageSize*rowNum,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, imageSize*columnNum, imageSize*rowNum, null);
        g2d.dispose();        
        
        return(resizedImage);
    }



    //dividimos la imagen en el nÃºmero
    private BufferedImage[] splitImage(BufferedImage image){
        //Divisor de imÃ¡genes

        BufferedImage images[] = new BufferedImage[columnNum*rowNum];
        
        for(int i =0; i < rowNum; i++){
            for(int j =0; j < columnNum; j++){
                BufferedImage pieza = image.getSubimage(j*imageSize, i*imageSize, imageSize, imageSize);
                /*try {
                    //ImageIO.write(pieza, "jpg", new File("foto"+i*columnNum+j+".jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                
                
                images[i*columnNum+j] = pieza;
            }
        }
        
        return(images);
    }

    @Override
    public void update(int blankPos, int movedPos){
        
        if(blankPos !=99)
        {
        PieceView p = iconArray.get(movedPos);
        PieceView p2 = iconArray.get(blankPos);

            int x = p.getIndexColumn();
            int y = p.getIndexRow();
            p.setPosition(p2.getIndexColumn(), p2.getIndexRow());
            p2.setPosition(x, y);       
            iconArray.set(blankPos, p);
            iconArray.set(movedPos, p2);
            blankPiece = movedPos;    
            
        }
        this.repaint();
                   
    }

    public void update(Graphics g){
        paint(g);
    }

    public void paint(Graphics g){
        super.paint(g);
        
        for(PieceView iconImage:iconArray){
            g.drawImage(iconImage.getImage(), iconImage.getIndexColumn()*imageSize, iconImage.getIndexRow()*imageSize, imageSize-1, imageSize-1, this);            
        }
    }

    //Dado una posicion X e Y localizar una pieza
    private int locatePiece(int posX,int posY){
       
        int posArray = (posX/imageSize)+(posY/imageSize*columnNum);
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
        if(disx + disy == 1)
        {
            return piezas;
        }else{
            piezas[0] = 99;
            piezas[1] = 99;
            return piezas;
        }
        
        
    }
        public File getImage() {
        return image;
    }
        

    public int[] getRandomMovement(int lastPos, int pos) {
        boolean repetido = true;
        int randomMove[] = new int[2];

            while(repetido){
                PieceView piezaBlanca = iconArray.get(pos);
                
                int piezaMoverColumna=piezaBlanca.getIndexColumn();
                int piezaMoverFila=piezaBlanca.getIndexRow();
                
                int move = r.nextInt(4);
                
                //Array
                if(move == 0){
                    if(piezaMoverFila-1>-1){
                        
                        int posMover = (piezaMoverFila-1)*columnNum+piezaMoverColumna;
                    
                        if(posMover != lastPos){
                            randomMove[0] = pos;
                            randomMove[1] = posMover;

                            repetido = false;
                        }
                    }
                }
                if(move == 1){
                    if(piezaMoverFila+1<rowNum){
                        
                        int posMover = (piezaMoverFila+1)*columnNum+piezaMoverColumna;
                    
                        if(posMover != lastPos){
                            randomMove[0] = pos;
                            randomMove[1] = posMover;

                            repetido = false;
                        }
                    }
                }
                if(move == 2){
                    if(piezaMoverColumna-1>-1){
                        
                        int posMover = piezaMoverFila*columnNum+piezaMoverColumna-1;
                    
                        if(posMover != lastPos){
                            randomMove[0] = pos;
                            randomMove[1] = posMover;

                            repetido = false;
                        }
                    }
                }
                if(move == 3){
                    if(piezaMoverColumna+1<columnNum){
                        
                        int posMover = piezaMoverFila*columnNum+piezaMoverColumna+1;
                    
                        if(posMover != lastPos){
                            randomMove[0] = pos;
                            randomMove[1] = posMover;

                            repetido = false;
                        }
                    }
                }
                
            }

        
        return randomMove;
    }

}
