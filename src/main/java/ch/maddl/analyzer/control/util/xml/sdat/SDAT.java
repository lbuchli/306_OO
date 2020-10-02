package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;

/**
 * Representation of an SDAT file (annotated bean class)
 */
public class SDAT {

    @XmlElement(namespace = "http://www.strom.ch", name = "MeteringData", required = true)
    private MeteringData meteringData;

    @XmlElement(namespace = "http://www.strom.ch", name = "ValidatedMeteredData_HeaderInformation", required = true)
    private HeaderInformation headerInformation;

    public HeaderInformation getHeaderInformation() {
        return headerInformation;
    }

    public MeteringData getMeteringData() {
        return meteringData;
    }
}


