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
public class MongoDataBase implements DataBaseAbstract{

    @Override
    public void StoreAll(LoadState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LoadState LoadFromDataBase(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	@Override
	public void AddMovement(MoveCommand command,String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveMovement(String id) {
		// TODO Auto-generated method stub
		
	}
    
}
