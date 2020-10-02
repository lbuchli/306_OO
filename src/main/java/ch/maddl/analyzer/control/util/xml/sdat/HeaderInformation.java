package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;

/**
 * Representation of the headerinformation element in an SDAT file
 */
public class HeaderInformation {

    @XmlElement(namespace = "http://www.strom.ch",name = "InstanceDocument", required = true)
    private InstanceDocument instanceDocument;

    public InstanceDocument getInstanceDocument() {
        return instanceDocument;
    }
}
