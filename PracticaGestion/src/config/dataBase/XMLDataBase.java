/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config.dataBase;

import config.Config;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Execute;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;
import org.basex.query.up.primitives.NewInput;
import org.basex.query.up.primitives.db.DBAdd;

import command.MoveCommand;
import config.LoadState;
import config.LoadStateAuxiliar;
import java.io.File;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Jose Daniel Campos y Pablo Rodriguez
 */
public class XMLDataBase implements DataBaseAbstract {

	Context dataBaseContext;
	final String QUERY_INTO = "storeGame/LoadState[@id='";
	public XMLDataBase(String dataBaseName, String dataBasePath) {
		dataBaseContext = new Context();
		try {
			new CreateDB("XMLdb").execute(dataBaseContext);
			new Add(dataBaseName, dataBasePath).execute(dataBaseContext);
		} catch (BaseXException ex) {
			Logger.getLogger(XMLDataBase.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public boolean StoreAll(LoadState state) {

		boolean couldStore = false;
		try {

			String queryResult = new XQuery( QUERY_INTO + state.getId() + "']")
					.execute(dataBaseContext);
			if (queryResult.isEmpty()) {
				String insert = "insert node " + state + "into storeGame";
				new XQuery(insert).execute(dataBaseContext);
				while (!state.getCommand().isEmpty()) {
			            MoveCommand d = state.getCommand().pollLast();
			            AddMovement(d, state.getId());
				  }
				
				couldStore = true;
				UpdateFile();
			}

		} catch (BaseXException ex) {
			Logger.getLogger(XMLDataBase.class.getName()).log(Level.SEVERE, null, ex);
		}
		return couldStore;
	}

	@Override
	public LoadState LoadFromDataBase(String id) {
		LoadStateAuxiliar c = null;
		LoadState state = null;
		try {
			String queryResult = new XQuery(QUERY_INTO + id + "']").execute(dataBaseContext);
			if (!queryResult.isEmpty()) {

				JAXBContext context = JAXBContext.newInstance(LoadStateAuxiliar.class);
				Unmarshaller XMLoader = context.createUnmarshaller();
				StringReader reader = new StringReader(queryResult);
				c = (LoadStateAuxiliar) XMLoader.unmarshal(reader);
				state = new LoadState(c);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return state;
	}

	@Override
	public void AddMovement(MoveCommand command, String id) {

		try {
			new XQuery("insert node " + command + "into "+ QUERY_INTO + id + "']/command")
					.execute(dataBaseContext);
			UpdateFile();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void RemoveMovement(String id) {
		try {
			new XQuery("delete node "+ QUERY_INTO + id + "']/command/comando [last()]")
					.execute(dataBaseContext);
			UpdateFile();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	private void UpdateFile() {
		try {
			new XQuery("for $item in /storeGame \n" + "return file:write('xmlDataBase/LoadStates.xml', $item)")
			.execute(dataBaseContext);
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
    

