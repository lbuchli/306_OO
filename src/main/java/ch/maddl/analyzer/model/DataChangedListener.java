package ch.maddl.analyzer.model;

/**
 * A listener to be notified if the data is changed
 */
@FunctionalInterface
public interface DataChangedListener {

    /**
     * This method gets called on data changes
     */
    public void onDataChanged();
}
