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
import config.LoadState;
import config.LoadStateAuxiliar;
import java.io.File;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void Store(LoadState state) {
         
    }

    @Override
    public LoadState LoadFromDataBase(int id) {   
        LoadStateAuxiliar c = null;
        try {
            String queryResult = new XQuery("storeGame/LoadState[@id="+id+"]").execute(dataBaseContext);
            System.out.println(queryResult);                        
            JAXBContext context = JAXBContext.newInstance(LoadStateAuxiliar.class);            
            Unmarshaller XMLoader = context.createUnmarshaller();
            StringReader reader = new StringReader(queryResult);
            c = (LoadStateAuxiliar) XMLoader.unmarshal(reader);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        LoadState state = new LoadState(c);
        return state;
	}
    }
    

