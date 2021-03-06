package ch.maddl.analyzer.control.util.json;

import java.util.List;

/**
 * A data of a sensor to be marshalled into a JSON file
 */
public class SensorData {

    private String sensorID;

    private List<DataValue> data;

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public void setData(List<DataValue> data) {
        this.data = data;
    }
}
