package ch.maddl.analyzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The analyzer class represents the current data state. (Singleton)
 */
public class Analyzer {

    private static Analyzer instance;

    private List<DataChangedListener> dataChangedListeners;
    private Data data;
    private Data unfiltered;

    private Analyzer() {
        dataChangedListeners = new ArrayList<>();
    }

    /**
     * Get the Analyzer instance
     * @return the instance
     */
    public static Analyzer getInstance() {
        if (instance == null) {
            instance = new Analyzer();
        }
        return instance;
    }

    /**
     * Get the current dataset
     * @return the dataset
     */
    public Data getData() {
        return data;
    }

    /**
     * Change the dataset (this causes the DataChangedListeners to be notified)
     * @param data the new data
     */
    public void setData(Data data) {
        this.data = data;
        dataChangedListeners.forEach(listener -> listener.onDataChanged());
    }

    /**
     * Get the unfiltered data
     * @return the data
     */
    public Data getUnfiltered() {
        return unfiltered;
    }

    /**
     * Set the unfiltered data
     * @param unfiltered the new unfiltered data
     */
    public void setUnfiltered(Data unfiltered) {
        this.unfiltered = unfiltered;
    }

    /**
     * ¨Add a {@link DataChangedListener} to be notified when the data changes.
     * @param listener the listener
     */
    public void addDataChangedListener(DataChangedListener listener) {
        dataChangedListeners.add(listener);
    }
}
