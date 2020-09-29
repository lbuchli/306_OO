package ch.maddl.analyzer.model;

import java.util.Date;
import java.util.Map;

public class Data {

    private String id;
    private Map<Date, Double> usageOverTime;
    private double usageLowTariff;
    private double usageHighTariff;

    public Data(String id, Map<Date, Double> usageOverTime, double usageLowTariff, double usageHighTariff) {
        this.id = id;
        this.usageOverTime = usageOverTime;
        this.usageLowTariff = usageLowTariff;
        this.usageHighTariff = usageHighTariff;
    }

    public String getId() {
        return id;
    }

    public Map<Date, Double> getUsageOverTime() {
        return usageOverTime;
    }

    public double getUsageLowTariff() {
        return usageLowTariff;
    }

    public double getUsageHighTariff() {
        return usageHighTariff;
    }
}
