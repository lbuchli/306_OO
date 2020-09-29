package ch.maddl.analyzer.control.util.data.esl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ESLBillingData")
public class ESL {

    @XmlElement(name = "TimePeriod", required = true)
    private TimePeriod timePeriod;

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
}