/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import command.MoveCommand;
import java.util.Deque;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
@XmlRootElement(name = "LoadState")
public class LoadStateAuxiliar {
    
    private Config config;
    private String command;
    private String imagePath;
    private int id;

    
    
    
    @XmlAttribute(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
 
    @XmlElement(name = "Config")
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    @XmlElement(name = "command")
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    @XmlElement(name = "imagePath")
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    
    
    
    
    
    
    
    
    
}
