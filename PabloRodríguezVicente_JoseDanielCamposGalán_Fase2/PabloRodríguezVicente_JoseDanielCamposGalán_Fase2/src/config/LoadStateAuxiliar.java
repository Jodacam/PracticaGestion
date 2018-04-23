/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import command.MoveCommand;
import java.util.Deque;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
@XmlRootElement(name = "LoadState")
public class LoadStateAuxiliar {
    
    private Config config;
    private List<MoveInformation> command;
    private String imagePath;
    private String id;

    
    
    
    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
 
    @XmlElement(name = "Config")
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    @XmlElementWrapper(name="command")
    @XmlElement(name="comando")
    public List<MoveInformation> getCommand() {
        return command;
    }

    public void setCommand(List<MoveInformation> command) {
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
