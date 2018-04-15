/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config.dataBase;

import config.LoadState;

/**
 *
 * @author Jose Daniel Campos
 */
public interface DataBaseAbstract {
    
    public void Store(LoadState state);
    public LoadState LoadFromDataBase(int id);
    
    
}
