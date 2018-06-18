import config.Config;
import config.ConfigLoader;
import static config.ConfigLoader.FileSeparator;
import static config.ConfigLoader.ProyectDir;
import config.dataBase.MongoDataBase;
import config.dataBase.XMLDataBase;
import control.Controller;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import model.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import view.BoardView;
import view.PuzzleGUI;

/*
 * Copyright 2016 Miguel Ã�ngel RodrÃ­guez-GarcÃ­a (miguel.rodriguez@urjc.es).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Clase principal que ejecuta el juego
 * @Author Miguel Ã�ngel
 * @version 1.0
 */
public class PuzzleApp {

    public static void main(String args[]){

        
       
        
        Config c = LoadDefaultConfig();
        int imageSize =c.getImageSize();
        int rowNum =  c.getNumRow();
        int columnNum= c.getNumColumn();
        String dataBaseType =c.getUsedDataBase();
        
        
        
        String fileSeparator = System.getProperty("file.separator");
        String imagePath=System.getProperty("user.dir")+fileSeparator+"resources"+fileSeparator;

        String[] imageList={imagePath+"blank.gif",imagePath+"one.gif",imagePath+"two.gif",imagePath+"three.gif",imagePath+ "four.gif",
                imagePath+"five.gif",imagePath+"six.gif",imagePath+"seven.gif",imagePath+"eight.gif"};
        // Creamos el modelo
       AbstractModel m = AbstractModel.InstanciateModel(rowNum, columnNum, imageSize,dataBaseType);
        // Creamos el controlador
        Controller controller  = new Controller(m);
        // Inicializamos la GUI
        PuzzleGUI.initialize(controller, rowNum, columnNum, imageSize, imageList);
        // Obtenemos la vista del tablero
        BoardView b = PuzzleGUI.getInstance().getBoardView();
        // AÃ±adimos un nuevo observador al controlador
        controller.addObserver(m);
        controller.setViewFromObservers();
        // Visualizamos la aplicaciÃ³n.
        PuzzleGUI.getInstance().setVisible(true);
    }
    
    
    
     public static Config LoadDefaultConfig() {

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
             c.setStoredInDB(false);                     
        } catch (Exception e) {
            System.out.println("XML no valido");
        }
        return c;
    }
}
