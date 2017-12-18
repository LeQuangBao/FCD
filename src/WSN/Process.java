package WSN;

import javax.xml.bind.annotation.XmlElement;

public class Process {
    @XmlElement(name = "Sensors")
    public Sensors sensors;
    @XmlElement(name = "Links")
    public Links links;
}
