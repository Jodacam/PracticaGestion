import javax.xml.bind.DataBindingException;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Execute;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;
import org.basex.query.up.primitives.NewInput;
import org.basex.query.up.primitives.db.DBAdd;

public class QueryXML {
	
	
	Context context;
	public void createcCollection() throws BaseXException {
		 context = new Context();		
		new CreateDB("myDB","C:/Users/Jodacam/eclipse-workspace/XML/db").execute(context);
		
		new CreateDB("OtraDB").execute(context);
		new Add("books.xml","C:/Users/Jodacam/eclipse-workspace/XML/db").execute(context);
		System.out.println(new InfoDB().execute(context));
		
		
	}
	
	public String queryCatalog(String query) {
		
	   try {
		String queryResult = new XQuery(query).execute(context);
		System.out.println(queryResult);
		return queryResult;
	} catch (BaseXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new String("");
	}
		
	}
	
	public void addBook(Books b) {
		String aString = "insert node "+b+ " into /catalog"; 
		try {
			new XQuery(aString).execute(context);
			this.updateCatalog(context);
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private void updateCatalog(Context context) {
		// TODO Auto-generated method stub
		
	}

	public void removeBook(String id) {
		String query = "delete node catalog/book[@id= '"+id+"']/.";
		this.queryCatalog(query);
	}
	
	public void replaceBook() {
		
	}

}
