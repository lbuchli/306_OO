package ch.maddl.analyzer.control;

import ch.maddl.analyzer.control.util.NoFileException;
import ch.maddl.analyzer.control.util.data.esl.ESL;
import ch.maddl.analyzer.control.util.data.esl.ValueRow;
import ch.maddl.analyzer.control.util.data.sdat.SDAT;
import ch.maddl.analyzer.model.Analyzer;
import ch.maddl.analyzer.model.Data;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ch.maddl.analyzer.App;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputChooseController {

    @FXML
    private TextField sdatUsageTextField;

    @FXML
    private TextField sdatSupplyTextField;

    @FXML
    private TextField eslTextField;

    @FXML
    private void onSDATUsageChooseButtonClick() {
        try {
            sdatUsageTextField.setText(chooseFile("SDAT-Usage-Datei auswählen", new Pair<>("SDAT (742)", "*.xml")));
        } catch (NoFileException e) {}
    }

    @FXML
    private void onSDATSupplyChooseButtonClick() {
        try {
            sdatSupplyTextField.setText(chooseFile("SDAT-Supply-Datei auswählen", new Pair<>("SDAT (735)", "*.xml")));
        } catch (NoFileException e) {}
    }

    @FXML
    private void onESLChooseButtonClick() {
        try {
            eslTextField.setText(chooseFile("ESL-Datei auswählen", new Pair<>("ESL", "*.xml")));
        } catch (NoFileException e) {}
    }

    @FXML
    private void onNextButtonClick() throws IOException {
        boolean sdatUsageExists = Files.exists(Path.of(sdatUsageTextField.getText()));
        boolean sdatSupplyExists = Files.exists(Path.of(sdatSupplyTextField.getText()));
        boolean eslExists = Files.exists(Path.of(eslTextField.getText()));
        if (sdatUsageExists && sdatSupplyExists && eslExists) {
            try {
                Data data = buildData(
                        parseSDAT(sdatUsageTextField.getText()),
                        parseSDAT(sdatSupplyTextField.getText()),
                        parseESL(eslTextField.getText())
                );

                Analyzer.getInstance().setData(data);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            App.setRoot("main");
            App.getStage().setWidth(1020);
            App.getStage().setHeight(480);
        }
    }

    private String chooseFile(String title, Pair<String, String>... extensions) throws NoFileException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(Arrays.stream(extensions)
                .map(pair -> new FileChooser.ExtensionFilter(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList()));
        File f = fileChooser.showOpenDialog(App.getStage());

        if (f == null) {
            throw new NoFileException();
        }
        return f.getAbsolutePath();
    }

    private SDAT parseSDAT(String path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(SDAT.class);
        return (SDAT) context.createUnmarshaller().unmarshal(new File(path));
    }

    private ESL parseESL(String path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ESL.class);
        return (ESL) context.createUnmarshaller().unmarshal(new File(path));
    }

    private Data buildData(SDAT sdatUsage, SDAT sdatSupply, ESL esl) {
        List<ValueRow> values = esl.getMeter().getTimePeriod().getValueRows();
        Double usageHighTariff = getValueOfObis(values, "1-1:1.8.1");
        Double usageLowTariff = getValueOfObis(values, "1-1:1.8.2");
        Double supplyHighTariff = getValueOfObis(values, "1-1:2.8.1");
        Double supplyLowTariff = getValueOfObis(values, "1-1:2.8.2");

        return new Data(
                getValuesOverTime(sdatUsage),
                getValuesOverTime(sdatSupply),
                usageLowTariff,
                usageHighTariff,
                supplyLowTariff,
                supplyHighTariff
        );
    }

    private List<Pair<LocalDateTime, Double>> getValuesOverTime(SDAT sdat) {
        LocalDateTime startTime = LocalDateTime.ofInstant(sdat.getMeteringData().getInterval().getStartDateTime().toInstant(), ZoneId.systemDefault());
        long resolution = sdat.getMeteringData().getResolution().getResolution();
        Duration interval;
        switch (sdat.getMeteringData().getResolution().getUnit()) {
            case "MIN": interval = Duration.ofMinutes(resolution); break; // TODO check documentation for other units
            case "SEC": interval = Duration.ofSeconds(resolution); break;
            default: interval = Duration.ofMinutes(15);
        }

        return sdat.getMeteringData().getObservations().stream()
                .map(obs -> new Pair<>(startTime.plus(interval.multipliedBy(obs.getPosition().getSequence())), obs.getVolume()))
                .sorted((a, b) -> a.getKey().isAfter(b.getKey()) ? 1 : -1).collect(Collectors.toList());
    }

    private double getValueOfObis(List<ValueRow> values, String obis) {
        for (ValueRow val : values) {
            if (val.getObis().equals(obis)) {
                return val.getValue();
            }
        }
        return 0.0;
    }
}
