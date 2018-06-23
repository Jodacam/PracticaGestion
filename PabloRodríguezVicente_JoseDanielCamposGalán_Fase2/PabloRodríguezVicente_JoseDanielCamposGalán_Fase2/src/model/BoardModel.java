/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import command.Command;
import command.MoveCommand;
import config.Config;
import config.ConfigLoader;

import config.LoadState;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.JOptionPane;
import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
public class BoardModel extends AbstractModel {

  
    private Random r = new Random();
    private Deque<Command> movimientos;
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
        super(rowNum, columnNum, pieceSize,"Local");
        movimientos = new ArrayDeque<>();
        
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

    



   

    //Este metodo ha sido movido a BoardView
    @Override
    public int[] getRandomMovement(int lastPos, int pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    @Override
    public boolean StoreAll(Deque list, File image) {
          
        
        
        if (gameName == null) {
            String inputString = JOptionPane.showInputDialog(null, "Please write the save name");
           gameName = inputString;
        }

        String imageName = "default";
        if (image != null) {
            imageName = FileSeparator + "saveGame" + FileSeparator + "imageSaves" + FileSeparator + gameName + "_saveImage";
        }

        Deque<MoveCommand> c = new ArrayDeque<MoveCommand>();

        list.forEach((d) -> {
            c.add((MoveCommand) d);
        });

        Config con = new Config();
        con.setGameName(gameName);
        con.setImageSize(pieceSize);
        con.setNumColumn(columnNum);
        con.setUsedDataBase("Local");
        con.setNumRow(rowNum);
        
        LoadState stateGame = new LoadState(con, c, imageName, gameName);
        String data = JSONMapper.toJson(stateGame);
        try {

            if (image != null) {
                BufferedImage imageBuffed = ImageIO.read(image);
                ImageIO.write(imageBuffed, "jpg", new File(ProyectDir + imageName));
            }
            FileWriter writer = new FileWriter(ProyectDir + FileSeparator + "saveGame" + FileSeparator + gameName + ".sav");
            PrintWriter w = new PrintWriter(writer);
            w.print(data);
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return  true;
    }

    @Override
    public LoadState LoadFromDataBase() { 
            return LoadGame(PuzzleGUI.getInstance().showFileSelector());
    }
    

    @Override
    public void AddMovement(MoveCommand command) {
        movimientos.push(command);
    }

    @Override
    public MoveCommand RemoveMovement() {
        return  (MoveCommand)movimientos.pop();
    }

    @Override
    public void CloseDataBase() {
       
    }

    @Override
    public void RemoveAllMovements(String id) {
        movimientos.clear();
    }
    
    
   private LoadState LoadGame(File saveFile) {
        LoadState state = null;
        try {
            FileReader reader = new FileReader(saveFile);
            state = JSONMapper.fromJson(reader, LoadState.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return state;
    }
}
