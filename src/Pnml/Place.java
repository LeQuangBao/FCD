package Pnml;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "place")
public class Place {
    @XmlAttribute(name = "id")
    public String id;
    @XmlElement(name = "label")
    public String label;
    @XmlElement(name = "token")
    public int token;
}
