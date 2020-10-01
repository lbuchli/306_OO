package ch.maddl.analyzer.control;

import ch.maddl.analyzer.App;
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
    private void onExportAsJSONMenuItemClicked() throws JAXBException, IOException {
        saveString(
                asJSON(Analyzer.getInstance().getData().getUsageOverTime(), Analyzer.getInstance().getData().getSupplyOverTime()),
                "JSON", "*.json",
                "Daten als JSON speichern"
        );
    }

    @FXML
    private void onPublishToServerMenuItemClicked() {
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

    protected void fillTimeChart() {
        XYChart.Series<String, Double> usageSeries = seriesFrom(Analyzer.getInstance().getData().getUsageOverTime());
        usageSeries.setName("Verbrauch");

        XYChart.Series<String, Double> supplySeries = seriesFrom(Analyzer.getInstance().getData().getSupplyOverTime());
        supplySeries.setName("Einspeisung");

        timeChart.getData().addAll(usageSeries, supplySeries);
    }

    private XYChart.Series<String, Double> seriesFrom(List<Pair<LocalDateTime, Double>> data) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        int stepSize = data.size()/128;
        for (int i = 0; i*stepSize < data.size(); i += 1) {
            Pair<LocalDateTime, Double> v = data.get(i*stepSize);
            series.getData().add(i, new XYChart.Data<>(v.getKey().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")), v.getValue()));
        }
        return series;
    }

    private String asCSV(List<Pair<LocalDateTime, Double>> data) {
        StringBuilder sb = new StringBuilder("timestamp, value\n");
        data.forEach(pair -> sb.append(String.format("%s, %.1f\n",
                localDateTimeToTimestamp(pair.getKey()),
                pair.getValue()
        )));

        return sb.toString();
    }

    private String asJSON(List<Pair<LocalDateTime, Double>> usage, List<Pair<LocalDateTime, Double>> supply) {
        SensorData usageData = new SensorData();
        usageData.setSensorID("742");
        usageData.setData(toDataValues(usage));

        SensorData supplyData = new SensorData();
        supplyData.setSensorID("735");
        supplyData.setData(toDataValues(supply));

        return new Gson().toJson(Arrays.asList(usageData, supplyData));
    }

    private List<DataValue> toDataValues(List<Pair<LocalDateTime, Double>> data) {
        return data.stream().map(pair -> {
            DataValue val = new DataValue();
            val.setTs(String.valueOf(localDateTimeToTimestamp(pair.getKey())));
            val.setValue(pair.getValue());
            return val;
        }).collect(Collectors.toList());
    }


    private long localDateTimeToTimestamp(LocalDateTime time) {
        return time.toInstant(ZoneOffset.UTC).toEpochMilli() / 1000L;
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
