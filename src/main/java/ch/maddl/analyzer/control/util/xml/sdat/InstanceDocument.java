package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;

public class InstanceDocument {


    @XmlElement(namespace = "http://www.strom.ch", name = "DocumentID", required = true)
    private String documentID;

    public String getDocumentID() {
        return documentID;
    }
}
