/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config.dataBase;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;

import command.MoveCommand;
import config.LoadState;

/**
 *
 * @author Jose Daniel Campos
 */
public class MongoDataBase implements DataBaseAbstract{

	final Gson JSONMapper = new Gson();
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	public  MongoDataBase() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://admin:.99Kwo@gestiondatos-pzwlg.mongodb.net/gestion");

			MongoClient mongoClient = new MongoClient(uri);
			database = mongoClient.getDatabase("gestion");
			System.out.println(database.getName());
			collection = database.getCollection("partida",Document.class);
			
			
	}
	
	
    @Override
    public boolean StoreAll(LoadState state) {
        boolean couldStore = false;
        state.getConfig().setStoredInDB(true);
        state.getConfig().setGameName(state.getId());
    	String json = JSONMapper.toJson(state);
    	try {
    	
    	collection.insertOne(Document.parse(json));
    	
    	couldStore = true;
    	}
    	catch (Exception e) {
		
        }
    	return couldStore;
    }

    @Override
	public LoadState LoadFromDataBase(String id) {
		LoadState state = null;
		Bson bson = eq("id", id);
		Document document = collection.find(bson).first();
		if (document != null) {
			String json = document.toJson();
			state = JSONMapper.fromJson(json, LoadState.class);
		}
		return state;
	}

	@Override
	public void AddMovement(MoveCommand command,String id) {

                String json = JSONMapper.toJson(command);
                PushOptions p = new PushOptions();
                p.position(0);
                List<Document> moves = new ArrayList<>();
                moves.add(Document.parse(json));
		collection.updateOne(eq("id", id), Updates.pushEach("command", moves, p)); 
                
	}

	@Override
	public MoveCommand RemoveMovement(String id) {
            LoadState state = LoadFromDataBase(id);          
            MoveCommand c = state.getCommand().pollFirst();
            String json = JSONMapper.toJson(state);
            collection.replaceOne(eq("id", id), Document.parse(json));
            return c;		
	}
    
}
