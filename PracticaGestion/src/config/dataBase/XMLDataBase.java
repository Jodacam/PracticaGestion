/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config.dataBase;
import config.Config;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Execute;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;
import org.basex.query.up.primitives.NewInput;
import org.basex.query.up.primitives.db.DBAdd;

import command.MoveCommand;
import config.LoadState;
import config.LoadStateAuxiliar;
import java.io.File;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Jose Daniel Campos
 */
public class XMLDataBase implements DataBaseAbstract{

    Context dataBaseContext;
    
    public XMLDataBase(String dataBaseName,String dataBasePath){
        dataBaseContext = new Context();
        try {
            new CreateDB("XMLdb").execute(dataBaseContext);        
            new Add(dataBaseName,dataBasePath).execute(dataBaseContext);                  
        } catch (BaseXException ex) {
            Logger.getLogger(XMLDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void StoreAll(LoadState state) {

        try {

            String queryResult = new XQuery("storeGame/LoadState[@id=" +state.getConfig().getGameName() + "]").execute(dataBaseContext);
            if (queryResult.isEmpty()) {
                String insert = "insert node " + state + "into storeGame";
                new XQuery("insert node " + state + "into storeGame").execute(dataBaseContext);
                
            state.getCommand().forEach((d) -> {
                AddMovement(d, state.getConfig().getGameName());
                }
            );
                
            }
            String queryResult2 = new XQuery("storeGame/LoadState[@id=" +state.getConfig().getGameName() + "]").execute(dataBaseContext);
            String queryResult3 = new XQuery("storeGame").execute(dataBaseContext);
            int i = 2;
            
        } catch (BaseXException ex) {
            Logger.getLogger(XMLDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public LoadState LoadFromDataBase(String id) {   
        LoadStateAuxiliar c = null;
        LoadState state = null;
        try {
            String queryResult = new XQuery("storeGame/LoadState[@id="+id+"]").execute(dataBaseContext);                                  
           if(!queryResult.isEmpty()){
            
            JAXBContext context = JAXBContext.newInstance(LoadStateAuxiliar.class);            
            Unmarshaller XMLoader = context.createUnmarshaller();
            StringReader reader = new StringReader(queryResult);
            c = (LoadStateAuxiliar) XMLoader.unmarshal(reader);
            state = new LoadState(c);
           }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        
        
        
        return state;
	}

	@Override
	public void AddMovement(MoveCommand command,String id) {
            
		try {
                    new XQuery("insert node " + command + "into storeGame/LoadState[@id=" + id + "]/command").execute(dataBaseContext);
                    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void RemoveMovement(String id) {            
            try {
                new XQuery("delete node storeGame/LoadState[@id=" + id + "]/command/comando [last()]").execute(dataBaseContext);
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		
	}
    }
    

