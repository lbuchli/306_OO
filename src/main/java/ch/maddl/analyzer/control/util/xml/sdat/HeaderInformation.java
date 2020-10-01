package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;

public class HeaderInformation {

    @XmlElement(namespace = "http://www.strom.ch",name = "InstanceDocument", required = true)
    private InstanceDocument instanceDocument;

    public InstanceDocument getInstanceDocument() {
        return instanceDocument;
    }
}
