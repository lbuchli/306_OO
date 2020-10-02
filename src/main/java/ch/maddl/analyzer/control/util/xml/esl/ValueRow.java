package ch.maddl.analyzer.control.util.xml.esl;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Representation of the valuerow element in an ESL file
 */
public class ValueRow {

    @XmlAttribute(name = "obis", required = true)
    private String obis;

    @XmlAttribute(name = "value", required = true)
    private double value;

    public String getObis() {
        return obis;
    }

    public double getValue() {
        return value;
    }
}
