package ch.maddl.analyzer.control.util.xml.sdat;

import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

public class Interval {

    @XmlElement(namespace = "http://www.strom.ch", name = "StartDateTime", required = true)
    Date startDateTime;

    @XmlElement(namespace = "http://www.strom.ch", name = "EndDateType", required = true)
    Date endDateTime;

    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }
}
