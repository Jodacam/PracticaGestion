/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;
import command.Command;
import command.MoveCommand;
import config.Config;
import config.LoadState;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.bson.Document;
import org.bson.conversions.Bson;
import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos
 */





public class MongoModel extends AbstractModel {

    final Gson JSONMapper = new Gson();
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    MongoClient mongoClient;
    
    
    public MongoModel(int rowNum, int columnNum, int pieceSize) {
        super(rowNum, columnNum, pieceSize,"Mongo");
         MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://admin:.99Kwo@gestiondatos-pzwlg.mongodb.net/gestion");
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("gestion");
        System.out.println(database.getName());
        collection = database.getCollection("partida", Document.class);
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
    public int[] getRandomMovement(int lastPos, int pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean StoreAll(Deque<Command> list, File image) {
        boolean couldStore = true;

        if (!isStore) {
            String[] games = GetGames();
            String newName = PuzzleGUI.getInstance().GetFromPanel("Write a name for the save data",games);

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
                   gameName = newName;
                    try {
                        if (image != null) {
                            BufferedImage imageBuffed = ImageIO.read(image);
                            ImageIO.write(imageBuffed, "jpg", new File(ProyectDir + imageName));
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(gameName).log(Level.SEVERE, null, ex);
                    }
                }
               isStore = true;
            } else {
                couldStore = false;
            }
        }
        return couldStore;
    }
    
    
    
    private boolean Store(LoadState state){
        boolean couldStore = false;
        state.getConfig().setStoredInDB(true);
        state.getConfig().setGameName(state.getId());
        String json = JSONMapper.toJson(state);
        try {

            collection.insertOne(Document.parse(json));

            couldStore = true;
        } catch (Exception e) {

        }
        return couldStore; 
    }
    

    @Override
    public LoadState LoadFromDataBase() {        
        String[] games = GetGames();
        String id = PuzzleGUI.getInstance().GetFromPanel("Elige una partida para cargar", games);                   
        return Load(id);
    }
    
    
    
    
    private LoadState Load(String id){
        LoadState state = null;
        Bson bson = eq("id", id);
        Document document = collection.find(bson).first();
        if (document != null) {
            String json = document.toJson();
            state = JSONMapper.fromJson(json, LoadState.class);
            gameName = state.getConfig().getGameName();
            rowNum = state.getConfig().getNumRow();
            columnNum = state.getConfig().getNumColumn();
            pieceSize = state.getConfig().getImageSize();
            isStore = state.getConfig().isStoredInDB();
        }
        return state;
    }
    
    

    @Override
    public void AddMovement(MoveCommand command) {
        if(gameName!= null){
            String json = JSONMapper.toJson(command);
            PushOptions p = new PushOptions();
            p.position(0);
            List<Document> moves = new ArrayList<>();
            moves.add(Document.parse(json));
            collection.updateOne(eq("id", gameName), Updates.pushEach("command", moves, p));
        }
    }

    @Override
    public MoveCommand RemoveMovement() {
        
        if(gameName != null){
        LoadState state = Load(gameName);
        MoveCommand c = state.getCommand().pollFirst();
        String json = JSONMapper.toJson(state);
        collection.replaceOne(eq("id", gameName), Document.parse(json));
        return c;
        }
        return null;
    }

    @Override
    public void CloseDataBase() {
        mongoClient.close();
    }



    @Override
    public void RemoveAllMovements(String id) {
       List<Document> moves = new ArrayList<>();
       collection.updateOne(eq("id", gameName), Updates.set("command", moves));
    }


    
    
    private String[] GetGames(){
        Bson projection = Projections.fields(Projections.include("id"),Projections.exclude("_id"));
        ArrayList<Document> gamesD = collection.find().projection(projection).into(new ArrayList<Document>());
        
        String[] games = new String[gamesD.size()];
        for (int i=0; i<gamesD.size();i++) {
            games[i] = gamesD.get(i).toJson().split(":")[1].split(" ")[1];
        }
        
        return games;
    }

    @Override
    public float ObtainSize() {
        return 0;
    }
}
