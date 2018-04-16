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
    public LoadState LoadFromDataBase(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	@Override
	public void AddMovement(MoveCommand command,int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveMovement(int id) {
		// TODO Auto-generated method stub
		
	}
    
}
