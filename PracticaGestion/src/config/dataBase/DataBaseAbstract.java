/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config.dataBase;

import command.MoveCommand;
import config.LoadState;

/**
 *
 * @author Jose Daniel Campos
 */
public interface DataBaseAbstract {
    
    public boolean StoreAll(LoadState state);
    public LoadState LoadFromDataBase(String id);
    public void AddMovement(MoveCommand command,String id);
    public void RemoveMovement(String id);
    
    
}
