package ch.maddl.analyzer.model;

import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The data gained from the xml files (this is a bean class)
 */
public class Data {

    private List<Pair<LocalDateTime, Double>> usageOverTime;
    private List<Pair<LocalDateTime, Double>> supplyOverTime;
    private double usageLowTariff;
    private double usageHighTariff;
    private double supplyLowTariff;
    private double supplyHighTariff;

    public Data(
            List<Pair<LocalDateTime, Double>> usageOverTime,
            List<Pair<LocalDateTime, Double>> supplyOverTime,
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

    public List<Pair<LocalDateTime, Double>> getUsageOverTime() {
        return usageOverTime;
    }

    public List<Pair<LocalDateTime, Double>> getSupplyOverTime() {
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
