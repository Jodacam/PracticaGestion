/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config.dataBase;

import config.Config;
import config.ConfigLoader;

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
import config.MoveInformation;

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
        state.getConfig().setGameName(state.getId());
        boolean couldStore = false;
        try {

            String queryResult = new XQuery(QUERY_INTO + state.getId() + "']")
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
            new XQuery("insert node " + command + "into " + QUERY_INTO + id + "']/command")
                    .execute(dataBaseContext);
            UpdateFile();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public MoveCommand RemoveMovement(String id) {
        MoveCommand moveCommand = null;
        try {
            //Obtengo el Comando
            String query = new XQuery(QUERY_INTO + id + "']/command/comando [last()]").execute(dataBaseContext);
            // Lo elimino
            new XQuery("delete node " + QUERY_INTO + id + "']/command/comando [last()]")
                    .execute(dataBaseContext);

            JAXBContext context = JAXBContext.newInstance(MoveInformation.class);
            Unmarshaller XMLoader = context.createUnmarshaller();
            StringReader reader = new StringReader(query);
            MoveInformation c = (MoveInformation) XMLoader.unmarshal(reader);
            moveCommand = new MoveCommand(new int[]{c.getN1(), c.getN2()}, null);
            UpdateFile();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return moveCommand;
    }

    private void UpdateFile() {
        try {
            new XQuery("for $item in /storeGame \n" + "return file:write('xmlDataBase" + ConfigLoader.FileSeparator + "LoadStates.xml', $item)")
                    .execute(dataBaseContext);
        } catch (BaseXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void CloseDataBase() {
        dataBaseContext.closeDB();
        dataBaseContext.close();
    }

    @Override
    public String[] GetGames() {
        String[] games = null;
        try {
            String query = new XQuery("for $item in /storeGame \n"
                    + "return $item/LoadState/@id").execute(dataBaseContext);
            games = query.split(" ");
            for(int i=0;i<games.length;i++){
                games[i] = games[i].replace("id=","");
            }
        } catch (BaseXException e) {
            e.printStackTrace();
        }
        return games;
    }

    @Override
    public void RemoveAllMovements(String id) {
        try {
            new XQuery("delete node "+QUERY_INTO+id+"']/command/comando").execute(dataBaseContext);
        } catch (BaseXException ex) {
            Logger.getLogger(XMLDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        UpdateFile();
    }
}
