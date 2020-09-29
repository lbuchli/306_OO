package ch.maddl.analyzer.control.util.data.sdat;

import javax.xml.bind.annotation.XmlElement;

public class Observation {

     @XmlElement(namespace = "http://www.strom.ch", name = "Position", required = true)
     private Position position;

     @XmlElement(namespace = "http://www.strom.ch", name = "Volume", required = true)
     private double volume;

     public Position getPosition() {
          return position;
     }

     public double getVolume() {
          return volume;
     }
}
