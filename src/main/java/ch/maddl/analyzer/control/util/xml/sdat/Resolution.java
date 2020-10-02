package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;

/**
 * Representation of the resolution element in an SDAT file
 */
public class Resolution {

    @XmlElement(namespace = "http://www.strom.ch", name = "Resolution", required = true)
    private int resolution;

    @XmlElement(namespace = "http://www.strom.ch", name = "Unit", required = true)
    private String unit;

    public int getResolution() {
        return resolution;
    }

    public String getUnit() {
        return unit;
    }
}
