package ch.maddl.analyzer.control;

import ch.maddl.analyzer.App;
import ch.maddl.analyzer.control.util.Util;
import ch.maddl.analyzer.control.util.json.DataValue;
import ch.maddl.analyzer.control.util.json.SensorData;
import ch.maddl.analyzer.model.Analyzer;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

/**
 * The controller for the main screen
 */
public class MainController {

    @FXML
    private BarChart<String, Double> billingChart;

    @FXML
    private LineChart<String, Double> timeChart;

    @FXML
    private void initialize() {
        fillBillingChart();
        fillTimeChart();
        Analyzer.getInstance().addDataChangedListener(() -> {
            timeChart.getData().clear();
            fillTimeChart();
        });
    }

    @FXML
    private void onOpenMenuItemClicked() throws IOException {
        App.setScene("inputchooser");
        App.getStage().setWidth(476.0);
        App.getStage().setHeight(325.0);
    }

    @FXML
    private void onExportUsageAsCSVMenuItemClicked() {
        Util.saveString(
                Util.asCSV(Analyzer.getInstance().getData().getUsageOverTime()),
                "742.csv","CSV", "*.csv",
                "Verbrauch als CSV speichern"
        );
    }

    @FXML
    private void onExportSupplyAsCSVMenuItemClicked() {
        Util.saveString(
                Util.asCSV(Analyzer.getInstance().getData().getSupplyOverTime()),
                "735.csv","CSV", "*.csv",
                "Einspeisung als CSV speichern"
        );
    }

    @FXML
    private void onExportAsJSONMenuItemClicked() {
        Util.saveString(
                Util.asJSON(Analyzer.getInstance().getData().getUsageOverTime(), Analyzer.getInstance().getData().getSupplyOverTime()),
                "data.json","JSON", "*.json",
                "Daten als JSON speichern"
        );
    }

    @FXML
    private void onPublishToServerMenuItemClicked() throws IOException {
        Stage serverStage = new Stage();
        serverStage.initModality(Modality.WINDOW_MODAL);
        serverStage.initOwner(App.getStage().getScene().getWindow());
        serverStage.setTitle("Auf Server veröffentlichen");
        serverStage.setScene(new Scene(App.loadFXML("serverconnection")));
        serverStage.show();
    }

    @FXML
    private void onFilterPerDayMenuItemClicked() throws IOException {
        Stage filterStage = new Stage();
        filterStage.initModality(Modality.WINDOW_MODAL);
        filterStage.initOwner(App.getStage().getScene().getWindow());
        filterStage.setTitle("Filteroptionen");
        filterStage.setScene(new Scene(App.loadFXML("filter")));
        filterStage.show();
    }

    /**
     * Fill the billing chart with the current analyzer data
     */
    private void fillBillingChart() {
        XYChart.Series<String, Double> usageSeries = new XYChart.Series<>();
        usageSeries.getData().addAll(
                new XYChart.Data<>("Hochtarif", Analyzer.getInstance().getData().getUsageHighTariff()),
                new XYChart.Data<>("Niedertarif", Analyzer.getInstance().getData().getUsageLowTariff())
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

    /**
     * Fill the time chart with the current analyzer data
     */
    protected void fillTimeChart() {
        XYChart.Series<String, Double> usageSeries = seriesFrom(Analyzer.getInstance().getData().getUsageOverTime());
        usageSeries.setName("Verbrauch");

        XYChart.Series<String, Double> supplySeries = seriesFrom(Analyzer.getInstance().getData().getSupplyOverTime());
        supplySeries.setName("Einspeisung");

        timeChart.getData().addAll(usageSeries, supplySeries);
    }

    /**
     * Create a chart series from a dataset
     * @param data the dataset
     * @return the series
     */
    private XYChart.Series<String, Double> seriesFrom(List<Pair<LocalDateTime, Double>> data) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        int stepSize = data.size()/128;
        for (int i = 0; i*stepSize < data.size(); i += 1) {
            Pair<LocalDateTime, Double> v = data.get(i*stepSize);
            series.getData().add(i, new XYChart.Data<>(v.getKey().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), v.getValue()));
        }
        return series;
    }

}
