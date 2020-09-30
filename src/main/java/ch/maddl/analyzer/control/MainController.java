package ch.maddl.analyzer.control;

import ch.maddl.analyzer.App;
import ch.maddl.analyzer.model.Analyzer;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.List;

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
        App.setScene("inputchooser");
        App.getStage().setWidth(476.0);
        App.getStage().setHeight(325.0);
    }

    @FXML
    private void onExportUsageAsCSVMenuItemClicked() {
        saveString(
                asCSV(Analyzer.getInstance().getData().getUsageOverTime()),
                "CSV", "*.csv",
                "Verbrauch als CSV speichern"
        );
    }

    @FXML
    private void onExportSupplyAsCSVMenuItemClicked() {
        saveString(
                asCSV(Analyzer.getInstance().getData().getSupplyOverTime()),
                "CSV", "*.csv",
                "Einspeisung als CSV speichern"
        );
    }

    @FXML
    private void onExportAsJSONMenuItemClicked() {
    }

    @FXML
    private void onPublishToServerMenuItemClicked() {
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
        XYChart.Series<String, Double> usageSeries = seriesFrom(Analyzer.getInstance().getData().getUsageOverTime());
        usageSeries.setName("Verbrauch");

        XYChart.Series<String, Double> supplySeries = seriesFrom(Analyzer.getInstance().getData().getSupplyOverTime());
        supplySeries.setName("Einspeisung");

        timeChart.getData().addAll(usageSeries, supplySeries);
    }

    private XYChart.Series<String, Double> seriesFrom(List<Pair<LocalDateTime, Double>> data) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        int i = 0;
        for (Pair<LocalDateTime, Double> v : data) {
            series.getData().add(i, new XYChart.Data<>(v.getKey().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), v.getValue()));
            i++;
        }
        return series;
    }

    private String asCSV(List<Pair<LocalDateTime, Double>> data) {
        StringBuilder sb = new StringBuilder("timestamp, value\n");
        data.forEach(pair -> sb.append(String.format("%s, %.1f\n",
                pair.getKey().toInstant(ZoneOffset.UTC).toEpochMilli() / 1000L,
                pair.getValue()
        )));

        return sb.toString();
    }

    private void saveString(String data, String extensionName, String extensionPattern, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionName, extensionPattern));
        File file = fileChooser.showSaveDialog(App.getStage());
        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                writer.print(data);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
