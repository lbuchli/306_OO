package ch.maddl.analyzer.control.util.data.esl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;
import java.util.List;

public class TimePeriod {

    @XmlAttribute(name = "end", required = true)
    private Date end;

    @XmlElement(name = "ValueRow", required = true)
    private List<ValueRow> valueRows;

    public Date getEnd() {
        return end;
    }

    public List<ValueRow> getValueRows() {
        return valueRows;
    }
}
