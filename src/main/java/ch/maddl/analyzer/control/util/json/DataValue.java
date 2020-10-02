package ch.maddl.analyzer.control.util.json;

/**
 * A data value to be marshalled into a JSON file
 */
public class DataValue {

    private String ts;

    private double value;

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
