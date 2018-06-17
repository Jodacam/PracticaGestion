/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;
import command.Command;
import command.MoveCommand;
import config.LoadState;
import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author Jose Daniel Campos
 */





public class MongoModel extends AbstractModel {

    final Gson JSONMapper = new Gson();
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    MongoClient mongoClient;
    
    
    public MongoModel(int rowNum, int columnNum, int pieceSize, String[] imageList) {
        super(rowNum, columnNum, pieceSize, imageList);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getRandomMovement(int lastPos, int pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean StoreAll(Deque<Command> list, File image) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LoadState LoadFromDataBase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void AddMovement(MoveCommand command) {
        if(this.ActualConfig.getGameName()!= null){
            String json = JSONMapper.toJson(command);
            PushOptions p = new PushOptions();
            p.position(0);
            List<Document> moves = new ArrayList<>();
            moves.add(Document.parse(json));
            collection.updateOne(eq("id", this.ActualConfig.getGameName()), Updates.pushEach("command", moves, p));
        }
    }

    @Override
    public MoveCommand RemoveMovement() {
        
        if(gameName != null){
        LoadState state = LoadFromDataBase();
        MoveCommand c = state.getCommand().pollFirst();
        String json = JSONMapper.toJson(state);
        collection.replaceOne(eq("id", gameName), Document.parse(json));
        return c;
        }
        return null;
    }

    @Override
    public void CloseDataBase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    public void RemoveAllMovements(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(int blankPos, int movedPos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
