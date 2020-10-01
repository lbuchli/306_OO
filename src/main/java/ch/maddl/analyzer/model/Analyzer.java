package ch.maddl.analyzer.model;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {

    private static Analyzer instance;

    private List<DataChangedListener> dataChangedListeners;
    private Data data;
    private Data unfiltered;

    private Analyzer() {
        dataChangedListeners = new ArrayList<>();
    }

    public static Analyzer getInstance() {
        if (instance == null) {
            instance = new Analyzer();
        }
        return instance;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
        dataChangedListeners.forEach(listener -> listener.onDataChanged());
    }

    public Data getUnfiltered() {
        return unfiltered;
    }

    public void setUnfiltered(Data unfiltered) {
        this.unfiltered = unfiltered;
    }

    public void addDataChangedListener(DataChangedListener listener) {
        dataChangedListeners.add(listener);
    }
}
