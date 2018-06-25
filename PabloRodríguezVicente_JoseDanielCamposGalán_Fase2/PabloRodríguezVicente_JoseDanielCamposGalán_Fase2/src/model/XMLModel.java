/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import command.Command;
import command.MoveCommand;
import config.Config;
import config.LoadState;
import config.MoveInformation;
import config.XMLState;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import static model.AbstractModel.FileSeparator;
import static model.AbstractModel.ProyectDir;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.XQuery;
import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos
 */
public class XMLModel extends AbstractModel {

    Context dataBaseContext;
    final String QUERY_INTO = "storeGame/LoadState[@id='";

    public XMLModel(int rowNum, int columnNum, int pieceSize) {

        super(rowNum, columnNum, pieceSize, "XML");
        dataBaseContext = new Context();
        try {
            new CreateDB("XMLdb").execute(dataBaseContext);
            new Add("LoadStates.xml", ProyectDir + FileSeparator + "xmlDataBase").execute(dataBaseContext);
        } catch (BaseXException ex) {
            Logger.getLogger("").log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
        PieceModel p = new PieceModel(id, indexRow, indexCol, pieceSize, imagePath);
        iconArray.add(p);
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol) {
        PieceModel p = new PieceModel(id, indexRow, indexCol, pieceSize);
        iconArray.add(p);
    }

    @Override
    public int[] getRandomMovement(int lastPos, int pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean StoreAll(Deque<Command> list, File image) {
        boolean couldStore = true;

        if (!isStore) {
            String[] games = GetGames();
            String newName = PuzzleGUI.getInstance().GetFromPanel("Write a name for the save data", games);

            if (newName != null && !newName.isEmpty()) {
                Deque<MoveCommand> c = new ArrayDeque<>();
                list.forEach(d -> {
                    c.add((MoveCommand) d);
                });

                String imageName = "default";
                if (image != null) {
                    imageName = FileSeparator + "saveGame" + FileSeparator + "imageSaves" + FileSeparator + newName
                            + "_saveImage";
                }

                Config f = new Config();
                f.setImageSize(pieceSize);
                f.setNumColumn(columnNum);
                f.setNumRow(rowNum);
                f.setUsedDataBase("Mongo");
                LoadState state = new LoadState(f, c, imageName, newName);
                couldStore = Store(state);
                if (couldStore) {
                    //gameName = newName;
                    isStore = true;
                    try {
                        if (image != null) {
                            BufferedImage imageBuffed = ImageIO.read(image);
                            ImageIO.write(imageBuffed, "jpg", new File(ProyectDir + imageName));
                        }
                    } catch (IOException ex) {
                        Logger.getLogger("").log(Level.SEVERE, null, ex);
                    }
                }
                
            } else {
                couldStore = false;
            }
        }
        return couldStore;

    }

    private boolean Store(LoadState state) {
        state.getConfig().setGameName(state.getId());
        boolean couldStore = false;
        try {

            String queryResult = new XQuery(QUERY_INTO + state.getId() + "']")
                    .execute(dataBaseContext);
            if (queryResult.isEmpty()) {
                gameName = state.getId();
                String insert = "insert node " + state + "into storeGame";

                new XQuery(insert).execute(dataBaseContext);

                while (!state.getCommand().isEmpty()) {
                    MoveCommand d = state.getCommand().pollLast();
                    AddMovement(d);
                }

                couldStore = true;
                UpdateFile();
            }

        } catch (BaseXException ex) {
            Logger.getLogger("").log(Level.SEVERE, null, ex);
        }
        return couldStore;
    }

    @Override
    public LoadState LoadFromDataBase() {
        String[] games = GetGames();
        String id = PuzzleGUI.getInstance().GetFromPanel("Elige una partida para cargar", games);
        XMLState c = null;
        LoadState state = null;
        try {
            String queryResult = new XQuery(QUERY_INTO + id + "']").execute(dataBaseContext);
            if (!queryResult.isEmpty()) {

                JAXBContext context = JAXBContext.newInstance(XMLState.class);
                Unmarshaller XMLoader = context.createUnmarshaller();
                StringReader reader = new StringReader(queryResult);
                c = (XMLState) XMLoader.unmarshal(reader);
                state = new LoadState(c);
                gameName = state.getConfig().getGameName();
                rowNum = state.getConfig().getNumRow();
                columnNum = state.getConfig().getNumColumn();
                pieceSize = state.getConfig().getImageSize();
                isStore = state.getConfig().isStoredInDB();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return state;

    }

    @Override
    public void AddMovement(MoveCommand command) {

        if (gameName != null) {
            try {
                new XQuery("insert node " + command + "into " + QUERY_INTO + gameName + "']/command")
                        .execute(dataBaseContext);
                UpdateFile();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public MoveCommand RemoveMovement() {
        MoveCommand moveCommand = null;
        
        if(gameName != null){
        try {
            //Obtengo el Comando
            String query = new XQuery(QUERY_INTO + gameName + "']/command/comando [last()]").execute(dataBaseContext);
            // Lo elimino
            new XQuery("delete node " + QUERY_INTO + gameName + "']/command/comando [last()]")
                    .execute(dataBaseContext);
            JAXBContext context = JAXBContext.newInstance(MoveInformation.class);
            Unmarshaller XMLoader = context.createUnmarshaller();
            StringReader reader = new StringReader(query);
            MoveInformation c = (MoveInformation) XMLoader.unmarshal(reader);
            moveCommand = new MoveCommand(new int[]{c.getN1(), c.getN2()}, null);
            UpdateFile();

        } catch (Exception e) {

            e.printStackTrace();
        }
        }
        return moveCommand;
    }

    @Override
    public void CloseDataBase() {
        dataBaseContext.closeDB();
        dataBaseContext.close();
    }

    @Override
    public void RemoveAllMovements(String id) {
        try {
            new XQuery("delete node " + QUERY_INTO + gameName + "']/command/comando").execute(dataBaseContext);
        } catch (BaseXException ex) {
            Logger.getLogger("").log(Level.SEVERE, null, ex);
        }
        UpdateFile();
    }

    private String[] GetGames() {
        String[] games = null;
        try {
            String query = new XQuery("for $item in /storeGame \n"
                    + "return $item/LoadState/@id").execute(dataBaseContext);
            games = query.split(" ");
            for (int i = 0; i < games.length; i++) {
                games[i] = games[i].replace("id=", "");
            }
        } catch (BaseXException e) {
            e.printStackTrace();
        }
        return games;
    }

    private void UpdateFile() {
        try {
            new XQuery("for $item in /storeGame \n" + "return file:write('xmlDataBase" + FileSeparator + "LoadStates.xml', $item)")
                    .execute(dataBaseContext);
        } catch (BaseXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

 @Override
    public float ObtainSize() {
        File path = new File(ProyectDir + FileSeparator + "xmlDataBase");
        return DirectorySize(path);
    }
    
    
    private float DirectorySize(File p){
        long size = 0;
        for (File file : p.listFiles()) {
        if (file.isFile()) {          
            size += file.length();
        }
        else
            size += DirectorySize(file);
    }
    return size;      
    }

}
