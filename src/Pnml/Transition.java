package Pnml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "transition")
public class Transition {
    @XmlAttribute(name = "id")
    public String id;
    @XmlElement(name = "label")
    public String label;
}

