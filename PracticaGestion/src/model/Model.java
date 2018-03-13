/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos
 */
public class Model extends AbstractModel {

    private ArrayList<PieceModel> iconArray = null;
    private int blankPiece;
    private Random r = new Random();
    
    public Model(int rowNum, int columnNum, int pieceSize, String[] imageList) {
        super(rowNum, columnNum, pieceSize, imageList);
        iconArray = new ArrayList<>();
        blankPiece = 0;
        for (int i = 0; i < columnNum*rowNum; i++) {
            PieceModel p = new PieceModel(i,i/columnNum,i%columnNum,pieceSize,imageList[i]);
            iconArray.add(p);
        }
    }
    
    public Model(int rowNum, int columnNum, int pieceSize) {
        super(rowNum, columnNum, pieceSize);
        iconArray = new ArrayList<>();
        blankPiece = 0;
        for (int i = 0; i < columnNum*rowNum; i++) {
            PieceModel p = new PieceModel(i,i/columnNum,i%columnNum,pieceSize);
            iconArray.add(p);
        }
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPuzzleSolve() {
        boolean colocado = true;
        for (int i = 0; i < columnNum*rowNum && colocado; i++) {
            colocado = iconArray.get(i).getId() == i;
        }
        return colocado;
    }

    @Override
    public int[] getRandomMovement(int lastPos, int pos) {
        boolean repetido = true;
        int randomMove[] = new int[2];

            while(repetido){
                PieceModel piezaBlanca = iconArray.get(pos);
                
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
                    if(piezaMoverFila+1<columnNum){
                        
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

    @Override
    public void update(int blankPos, int movedPos) {
       if(blankPos !=99)
        {
        System.out.println("Board View: "+blankPos+", "+movedPos);
        PieceModel p = iconArray.get(movedPos);
        PieceModel p2 = iconArray.get(blankPos);

            int x = p.getIndexColumn();
            int y = p.getIndexRow();
            p.setPosition(p2.getIndexColumn(), p2.getIndexRow());
            p2.setPosition(x, y);       
            iconArray.set(blankPos, p);
            iconArray.set(movedPos, p2);
            blankPiece = movedPos;    
            //Integer[] pos = {movedPos,blankPos};
            //PuzzleGUI.getInstance().getCommand().setMovimiento(pos);
            if(isPuzzleSolve()){
                PuzzleGUI.getInstance().mensajeVictoria();
            }
            
        }
        
    }
    
}
