package ch.maddl.analyzer.control.util.data.esl;

import javax.xml.bind.annotation.XmlElement;

public class Meter {

    @XmlElement(name = "TimePeriod", required = true)
    private TimePeriod timePeriod;

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
}
