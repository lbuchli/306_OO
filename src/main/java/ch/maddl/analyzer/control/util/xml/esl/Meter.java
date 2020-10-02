package ch.maddl.analyzer.control.util.xml.esl;

import javax.xml.bind.annotation.XmlElement;

/**
 * Representation of the meter element in an ESL file
 */
public class Meter {

    @XmlElement(name = "TimePeriod", required = true)
    private TimePeriod timePeriod;

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
}
