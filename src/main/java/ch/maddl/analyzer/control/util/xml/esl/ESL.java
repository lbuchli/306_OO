package ch.maddl.analyzer.control.util.xml.esl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation of an ESL file
 */
@XmlRootElement(name = "ESLBillingData")
public class ESL {

    @XmlElement(name = "Meter")
    private Meter meter;

    public Meter getMeter() {
        return meter;
    }
}
