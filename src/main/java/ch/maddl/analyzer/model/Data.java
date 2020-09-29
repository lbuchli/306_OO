package ch.maddl.analyzer.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class Data {

    private Map<LocalDateTime, Double> usageOverTime;
    private Map<LocalDateTime, Double> supplyOverTime;
    private double usageLowTariff;
    private double usageHighTariff;
    private double supplyLowTariff;
    private double supplyHighTariff;

    public Data(
            Map<LocalDateTime, Double> usageOverTime,
            Map<LocalDateTime, Double> supplyOverTime,
            double usageLowTariff,
            double usageHighTariff,
            double supplyLowTariff,
            double supplyHighTariff) {
        this.usageOverTime = usageOverTime;
        this.supplyOverTime = supplyOverTime;
        this.usageLowTariff = usageLowTariff;
        this.usageHighTariff = usageHighTariff;
        this.supplyLowTariff = supplyLowTariff;
        this.supplyHighTariff = supplyHighTariff;
    }

    public Map<LocalDateTime, Double> getUsageOverTime() {
        return usageOverTime;
    }

    public Map<LocalDateTime, Double> getSupplyOverTime() {
        return supplyOverTime;
    }

    public double getUsageLowTariff() {
        return usageLowTariff;
    }

    public double getUsageHighTariff() {
        return usageHighTariff;
    }

    public double getSupplyLowTariff() {
        return supplyLowTariff;
    }

    public double getSupplyHighTariff() {
        return supplyHighTariff;
    }
}
