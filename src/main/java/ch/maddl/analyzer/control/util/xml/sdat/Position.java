package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;

/**
 * Representation of the position element in an SDAT file
 */
public class Position {

    @XmlElement(namespace = "http://www.strom.ch", name = "Sequence", required = true)
    private int sequence;

    public int getSequence() {
        return sequence;
    }
}
