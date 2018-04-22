/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.google.gson.Gson;
import command.Command;
import command.MoveCommand;

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

import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import view.PuzzleGUI;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
public class ConfigLoader {

    private static Config ActualConfig;

    public static final String ProyectDir = System.getProperty("user.dir");
    public static final String FileSeparator = System.getProperty("file.separator");

    private static ConfigLoader configLoader;

    private static final Gson JSONMapper = new Gson();

    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public Config getActualConfig() {

        if (ActualConfig == null) {
            LoadDefaultConfig();
        }
        return ActualConfig;
    }

    private ConfigLoader() {

    }

    public void LoadDefaultConfig() {

        Config c = null;
        try {
            SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
            File miDoc = new File("config.xml");
            org.jdom2.Document doc;
            doc = (org.jdom2.Document) builder.build(miDoc);

            try {
                JAXBContext context = JAXBContext.newInstance(Config.class);
                Unmarshaller XMLoader = context.createUnmarshaller();
                File xml = new File("config.xml");
                c = (Config) XMLoader.unmarshal(xml);
            } catch (JAXBException ex) {
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            ActualConfig = c;
                   
        } catch (Exception e) {
            System.out.println("XML no valido");
        }
    }

    private LoadState LoadGame(File saveFile) {
        LoadState state = null;
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

    public void SaveGame(Deque<Command> list, File image) {
        if (ActualConfig.getGameName() == null) {
            String inputString = JOptionPane.showInputDialog(null, "Please write the save name");
            ActualConfig.setGameName(inputString);
        }

        String imageName = "default";
        if (image != null) {
            imageName = FileSeparator + "saveGame" + FileSeparator + "imageSaves" + FileSeparator + ActualConfig.getGameName() + "_saveImage";
        }

        Deque<MoveCommand> c = new ConcurrentLinkedDeque<MoveCommand>();

        for (Command d : list) {
            c.add((MoveCommand) d);
        }

        LoadState stateGame = new LoadState(ActualConfig, c, imageName, ActualConfig.getGameName());
        String data = JSONMapper.toJson(stateGame);
        try {

            if (image != null) {
                BufferedImage imageBuffed = ImageIO.read(image);
                ImageIO.write(imageBuffed, "jpg", new File(ProyectDir + imageName));
            }
            FileWriter writer = new FileWriter(ProyectDir + FileSeparator + "saveGame" + FileSeparator + ActualConfig.getGameName() + ".sav");
            PrintWriter w = new PrintWriter(writer);
            w.print(data);
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public LoadState Load() {
        JFileChooser selectorArchivo = new JFileChooser();

        File directorioRecursos = new File(ProyectDir + FileSeparator + "saveGame");

        selectorArchivo.setCurrentDirectory(directorioRecursos);

        int i = selectorArchivo.showOpenDialog(PuzzleGUI.getInstance());
        File selectedFile = null;

        if (i == JFileChooser.APPROVE_OPTION) {
            selectedFile = selectorArchivo.getSelectedFile();
            return LoadGame(selectedFile);
        } else {
            return null;
        }
    }

    public void SetNewConfig(Config c) {
        ActualConfig = c;
    }

    public void SetNewConfig(int row, int colum, int size) {
        ActualConfig.setImageSize(size);
        ActualConfig.setNumColumn(colum);
        ActualConfig.setNumRow(row);
    }

   
}
