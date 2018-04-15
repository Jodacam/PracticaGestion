
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
public class XMLLoader {
	
	
	public Document document;

	public XMLLoader() {
		
	}
	
	public void buildXML(){
		document = new Document();
		Element e = new Element("students");
		
		Element[] estudiantes = new Element[3];
		
		for(int i = 0; i<3 ; i++) {
			estudiantes[i] = new Element("student").setAttribute("id", ""+i);
			e.addContent(estudiantes[i]);
		}
		
		Element age = new Element("age").addContent("21");
		Element gender = new Element("gender").addContent("Male");
		Element phone = new Element("phone").addContent("12342113");
		Element name = new Element("name").addContent("Bart");
		
		estudiantes[0].addContent(age);
		estudiantes[0].addContent(gender);
		estudiantes[0].addContent(phone);
		estudiantes[0].addContent(name);
		
		Element age1 = new Element("age").addContent("21");
		Element gender1 = new Element("gender").addContent("Female");
		Element phone1 = new Element("phone").addContent("1234345");
		Element name1 = new Element("name").addContent("Lissa");
		
		estudiantes[1].addContent(age1);
		estudiantes[1].addContent(gender1);
		estudiantes[1].addContent(phone1);
		estudiantes[1].addContent(name1);
		
		Element age2 = new Element("age").addContent("23");
		Element gender2 = new Element("gender").addContent("Female");
		Element phone2 = new Element("phone").addContent("1234345");
		Element name2 = new Element("name").addContent("Maggie");
		
		estudiantes[2].addContent(age2);
		estudiantes[2].addContent(gender2);
		estudiantes[2].addContent(phone2);
		estudiantes[2].addContent(name2);
		
		document.setRootElement(e);
	}
	
	public List<String> getStudentsNameWithAge(){
		List<String> eList = new ArrayList();
		
		XPathFactory xPath = XPathFactory.instance();
		XPathExpression expression = xPath.compile("/students/student/name/text() | age/text()");
		List<Text> nodes = expression.evaluate(document);
		for(Text n:nodes) {
			eList.add(n.getText());
		}
		
		
		
		
		return eList;
	}
	
	public String[] getStudentsMale(){
		return null;
	}
	
	
	public void ReadXML() {
		 SAXBuilder saxBuilder = new SAXBuilder();
		 String dir = System.getProperty("user.dir");
		 File text = new File(dir+ File.separator+"resources"+File.separator+"EquipoXML.xml");
		 Document d;
		 System.out.println(dir);
		 try {
		 d =(Document)saxBuilder.build(text);
		 Element e = d.getRootElement();
		 List<Attribute> atributes = e.getAttributes();
		 PrintChildren(e);
		 JAXBContext c;
			c = JAXBContext.newInstance(player.class);
			player a = (player)c.createUnmarshaller().unmarshal(text);
			int i = 1;		 
		} catch (JDOMException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	
	public void PrintChildren(Element Parent) {
		List<Element> c = Parent.getChildren();
		if(c.isEmpty()) {
			System.out.println(Parent.getName()+":"+ Parent.getText());
		}else {
			for(Element i : c) {
				PrintChildren(i);
			}
		}
	}
	
	
	public void writeXML() {
		
		Document d = new Document();
		Element e = new Element("nombres");
		e.setAttribute("id","3");
		e.addContent(new Element("Persona").setText("Murciano"));
		e.getChild("Persona").setAttribute("Name","El Murciano");
		d.setRootElement(e);
		
		XMLOutputter o = new XMLOutputter();
		o.setFormat(Format.getPrettyFormat());
		try {
			o.output(d,new FileOutputStream(new File("prueba.xml")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	
}
