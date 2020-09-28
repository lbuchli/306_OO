package ch.maddl.analyzer.model;

import java.util.Date;
import java.util.Map;

public class Data {

    private String id;
    private Map<Date, Float> usageOverTime;
    private Float usageLowTariff;
    private Float usageHighTariff;

    public Data(String id, Map<Date, Float> usageOverTime, Float usageLowTariff, Float usageHighTariff) {
        this.id = id;
        this.usageOverTime = usageOverTime;
        this.usageLowTariff = usageLowTariff;
        this.usageHighTariff = usageHighTariff;
    }

    public String getId() {
        return id;
    }

    public Map<Date, Float> getUsageOverTime() {
        return usageOverTime;
    }

    public Float getUsageLowTariff() {
        return usageLowTariff;
    }

    public Float getUsageHighTariff() {
        return usageHighTariff;
    }
}
