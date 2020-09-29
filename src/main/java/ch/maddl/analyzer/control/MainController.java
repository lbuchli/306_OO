package ch.maddl.analyzer.control;

import ch.maddl.analyzer.App;
import ch.maddl.analyzer.model.Analyzer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MainController {

    @FXML
    private BarChart<String, Double> billingChart;

    @FXML
    private LineChart<String, Double> timeChart;

    @FXML
    private void initialize() {
        fillBillingChart();
        fillTimeChart();
    }

    @FXML
    private void onOpenMenuItemClicked() throws IOException {
        App.setRoot("inputchooser");
        App.getStage().setWidth(476.0);
        App.getStage().setHeight(325.0);
    }

    @FXML
    private void onExportAsCSVMenuItemClicked() {
    }

    private void fillBillingChart() {
        XYChart.Series<String, Double> usageSeries = new XYChart.Series<>();
        usageSeries.getData().addAll(
                new XYChart.Data<String, Double>("Hochtarif", Analyzer.getInstance().getData().getUsageHighTariff()),
                new XYChart.Data<String, Double>("Niedertarif", Analyzer.getInstance().getData().getUsageLowTariff())
        );
        usageSeries.setName("Verbrauch");

        XYChart.Series<String, Double> supplySeries = new XYChart.Series<>();
        supplySeries.getData().addAll(
                new XYChart.Data<String, Double>("Hochtarif", Analyzer.getInstance().getData().getSupplyHighTariff()),
                new XYChart.Data<String, Double>("Niedertarif", Analyzer.getInstance().getData().getSupplyLowTariff())
        );
        supplySeries.setName("Einspeisung");

        billingChart.getData().addAll(usageSeries, supplySeries);
    }

    private void fillTimeChart() {
        CategoryAxis xAxis = (CategoryAxis) timeChart.getXAxis();
        xAxis.setCategories(buildXAxis());

        XYChart.Series<String, Double> usageSeries = new XYChart.Series<>();
        Analyzer.getInstance().getData().getUsageOverTime().stream().map(
                entry -> usageSeries.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()))
        );
        usageSeries.setName("Verbrauch");

        XYChart.Series<String, Double> supplySeries = new XYChart.Series<>();
        Analyzer.getInstance().getData().getSupplyOverTime().stream().map(
                entry -> supplySeries.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()))
        );
        supplySeries.setName("Einspeisung");

        Analyzer.getInstance().getData().getUsageOverTime().forEach(pair -> System.out.printf("%s %f\n", pair.getKey().toString(), pair.getValue())); // TODO remove

        timeChart.getData().addAll(usageSeries, supplySeries);
    }

    private ObservableList<String> buildXAxis() {
        List<String> result = new ArrayList<>();
        for (Pair<LocalDateTime, Double> value : Analyzer.getInstance().getData().getUsageOverTime()) {
            result.add(value.getKey().toString());
        }
        return FXCollections.observableList(result);
    }
}
