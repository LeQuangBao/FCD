package WSN;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Link")
public class Link {
    @XmlAttribute(name = "id")
    public String id;
    @XmlAttribute(name = "MaxSendingRate")
    public String MaxSendingRate;
    @XmlElement(name = "From")
    public String From;
    @XmlElement(name = "To")
    public String To;
    public boolean IsConverted;
}