import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="player")
public class player {

	private String dorsal;
	private String name;
	private String teamName;
	
	
	@XmlElement(name = "dorsal")
	public String getDorsal() {
		return dorsal;
	}
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}
}
