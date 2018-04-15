
import org.basex.core.BaseXException;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.io.IOException;
import java.util.List;
public class Main {

	public static void main(String[] args) {
	QueryXML A = new QueryXML();
	try {
		A.createcCollection();
		A.queryCatalog("catalog/book/author");
		A.addBook(new Books(0, "Prueba",  "Prueba",  "Prueba", 30,  "Prueba",  "Prueba"));
		A.removeBook("0");
		
	} catch (BaseXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	


}
