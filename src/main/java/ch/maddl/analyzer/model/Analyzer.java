package ch.maddl.analyzer.model;

public class Analyzer {

    private static Analyzer instance;

    private Data data;

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
    }
}
