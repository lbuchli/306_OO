package ch.maddl.analyzer.control.util.data.sdat;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class MeteringData {

    @XmlElement(namespace = "http://www.strom.ch", name = "Interval", required = true)
    private Interval interval;

    @XmlElement(namespace = "http://www.strom.ch", name = "Resolution", required = true)
    private Resolution resolution;

    @XmlElement(name = "Observation", namespace = "http://www.strom.ch", required = true)
    private List<Observation> observations;

    public Interval getInterval() {
        return interval;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public List<Observation> getObservations() {
        return observations;
    }
}
