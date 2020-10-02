package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;

/**
 * Representation of the instancedocument element in an SDAT file
 */
public class InstanceDocument {


    @XmlElement(namespace = "http://www.strom.ch", name = "DocumentID", required = true)
    private String documentID;

    public String getDocumentID() {
        return documentID;
    }
}
