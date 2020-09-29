package ch.maddl.analyzer.control.util.data.sdat;

import javax.xml.bind.annotation.XmlElement;

public class Position {

    @XmlElement(namespace = "http://www.strom.ch", name = "Sequence", required = true)
    private int sequence;

    public int getSequence() {
        return sequence;
    }
}
