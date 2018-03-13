/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDLoader;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import command.Command;
import command.MoverCommand;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import view.PuzzleGUI;

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
            //XMLInputFactory XMLfactory = XMLInputFactory.newFactory();
            //XMLfactory.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, true);
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
        ActualConfig = state.getConfig();
        PuzzleGUI.getInstance().setConfig(ActualConfig);
        
        return state;
    }

    
    
    
    public static void SaveGame(Deque<Command> list,File image) {
       if(ActualConfig.getGameName() == null){
           String inputString = JOptionPane.showInputDialog(null, "Please write the save name");
           ActualConfig.setGameName(inputString);      
       }
                        
       String imageName = "default";
       if(image != null)
            imageName = FileSeparator+"saveGame"+FileSeparator+"imageSaves"+FileSeparator+ActualConfig.getGameName()+"_"+image.getName();
       
       
       Deque<MoverCommand> c = new ConcurrentLinkedDeque<MoverCommand>();
       
       for(Command d:list){
           c.add((MoverCommand)d);
       }
       
                           
       LoadState stateGame = new LoadState(ActualConfig, c,imageName);
       String data = JSONMapper.toJson(stateGame);
        try {
          
        if(image!=null){
        BufferedImage imageBuffed = ImageIO.read(image);
        ImageIO.write(imageBuffed,"jpg", new File(ProyectDir+imageName));
        }
            FileWriter writer = new FileWriter(ProyectDir+FileSeparator+"saveGame"+FileSeparator+ActualConfig.getGameName()+".sav");
            PrintWriter w = new PrintWriter(writer);
            w.print(data);
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    public static LoadState Load() {
        JFileChooser selectorArchivo = new JFileChooser();
        
                    File directorioRecursos = new File(ProyectDir+FileSeparator+"saveGame");
        
                    selectorArchivo.setCurrentDirectory(directorioRecursos);
                     
                    int i = selectorArchivo.showOpenDialog(PuzzleGUI.getInstance());
                    File selectedFile = null;
                    
                    if(i == JFileChooser.APPROVE_OPTION){
                    selectedFile = selectorArchivo.getSelectedFile();
                    return ConfigLoader.LoadGame(selectedFile);
                    }else{
                    return null;       
                  }
    }
    
    
    
    public static void SetNewConfig(Config c){
        ActualConfig = c;
    }
    
    public static void SetNewConfig(int row,int colum,int size){
        ActualConfig.setImageSize(size);
        ActualConfig.setNumColumn(colum);
        ActualConfig.setNumRow(row);
    }
    
}
