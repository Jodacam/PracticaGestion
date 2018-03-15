/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Random;
import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
public class BoardModel extends AbstractModel {

    private ArrayList<PieceModel> iconArray = null;
    private int blankPiece;
    private Random r = new Random();
    
    public BoardModel(int rowNum, int columnNum, int pieceSize, String[] imageList) {
        super(rowNum, columnNum, pieceSize, imageList);
        iconArray = new ArrayList<>();
        blankPiece = 0;
        for (int i = 0; i < columnNum*rowNum; i++) {
            PieceModel p = new PieceModel(i,i/columnNum,i%columnNum,pieceSize,imageList[i]);
            iconArray.add(p);
        }
    }
    
    public BoardModel(int rowNum, int columnNum, int pieceSize) {
        super(rowNum, columnNum, pieceSize);
        iconArray = new ArrayList<>();
        blankPiece = 0;
        for (int i = 0; i < columnNum*rowNum; i++) {
            addNewPiece(i,i/columnNum,i%columnNum);
        }
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
        PieceModel p = new PieceModel(id,indexRow,indexCol,pieceSize,imagePath);
        iconArray.add(p);
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol) {
        PieceModel p = new PieceModel(id,indexRow,indexCol,pieceSize);
        iconArray.add(p);
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
            if(isPuzzleSolve()){
                PuzzleGUI.getInstance().mensajeVictoria();
            }
            
        }
        
    }

    //Este metodo ha sido movido a BoardView
    @Override
    public int[] getRandomMovement(int lastPos, int pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
