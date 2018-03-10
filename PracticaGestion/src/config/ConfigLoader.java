/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Jose Daniel Campos
 */
public class ConfigLoader {
    
    
    private static Config ActualConfig;

    public static final String ProyectDir = System.getProperty("user.dir");
    public static final String FileSeparator = System.getProperty("file.separator");
    
    private static final Gson JSONMapper = new Gson();
    public static Config getActualConfig() {
       
        if (ActualConfig == null)
            LoadDefaultConfig();
        
        
        return ActualConfig;
    }
    
    
    public static void  LoadDefaultConfig(){
         
         
        Config c = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Config.class);
            Unmarshaller XMLoader = context.createUnmarshaller();
            File xml = new File("config.xml");
            c = (Config) XMLoader.unmarshal(xml);                     
        } catch (JAXBException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        }               
        ActualConfig = c;         
    }
    
    
    public static LoadState LoadGame(File saveFile){
        LoadState state= null;
        try {
            FileReader reader = new FileReader(saveFile);
            state = JSONMapper.fromJson(reader, LoadState.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        
        
        
        return state;
    }
    
    
}
