/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import command.MoveCommand;
import java.util.Deque;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez Vicente
 */
public class LoadState {
    
    private Config config;
    private Deque<MoveCommand> command;
    private String imagePath;

    public LoadState(Config config, Deque<MoveCommand> command, String imagePath) {
        this.config = config;
        this.command = command;
        this.imagePath = imagePath;
    }  
    
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Deque<MoveCommand> getCommand() {
        return command;
    }

    public void setCommand(Deque<MoveCommand> command) {
        this.command = command;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    
    
    
    
    
    
    
    
    
}
