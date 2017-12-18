package Editor;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Sensor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sensor {
	
	@XmlElement(name = "Id")
	public String Id;
	@XmlElement(name = "Name")
	public String Name;
	@XmlElement(name = "Token")
	public String Token;
	@XmlElement(name = "Energy")
	public float Energy;
	@XmlElement(name = "Type")
	public int Type;
	@XmlElement(name = "X")
	public double X;
	@XmlElement(name = "Y")
	public double Y;
	@XmlElement
	public double StartX;
	@XmlElement
	public double StartY;
	@XmlElement
	public double EndX;
	@XmlElement
	public double EndY;

}
