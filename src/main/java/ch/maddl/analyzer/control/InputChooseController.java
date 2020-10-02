package ch.maddl.analyzer.control;

import ch.maddl.analyzer.App;
import ch.maddl.analyzer.control.util.NoFileException;
import ch.maddl.analyzer.control.util.xml.esl.ESL;
import ch.maddl.analyzer.control.util.xml.esl.ValueRow;
import ch.maddl.analyzer.control.util.xml.sdat.SDAT;
import ch.maddl.analyzer.control.util.xml.sdat.SDAT12;
import ch.maddl.analyzer.control.util.xml.sdat.SDAT13;
import ch.maddl.analyzer.control.util.xml.sdat.SDAT14;
import ch.maddl.analyzer.model.Analyzer;
import ch.maddl.analyzer.model.Data;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The controller for the input choose window
 */
public class InputChooseController {

    @FXML
    private TextField sdatTextField;

    @FXML
    private TextField eslTextField;

    @FXML
    private void onSDATUsageChooseButtonClick() {
        try {
            sdatTextField.setText(chooseFile(
                    "SDAT-Dateien auswählen",
                    true,
                    new Pair<>("SDAT", "*.xml")
            ));
        } catch (NoFileException e) {}
    }

    @FXML
    private void onESLChooseButtonClick() {
        try {
            eslTextField.setText(chooseFile(
                    "ESL-Datei auswählen",
                    false,
                    new Pair<>("ESL", "*.xml")
            ));
        } catch (NoFileException e) {}
    }

    @FXML
    private void onNextButtonClick() throws IOException {
        boolean sdatExist = Arrays.stream(sdatTextField.getText().split(";")).allMatch(s -> Files.exists(Path.of(s)));
        boolean eslExists = Files.exists(Path.of(eslTextField.getText()));
        if (sdatExist && eslExists) {
            List<SDAT> sdats = new ArrayList<>();
            ESL esl;
            try {
                for (String path : sdatTextField.getText().split(";")) {
                    sdats.add(parseSDAT(path));
                }
                esl = parseESL(eslTextField.getText());
            } catch (JAXBException | RuntimeException e) {
                return;
            }

            Data data = buildData(sdats, esl);
            Analyzer.getInstance().setData(data);
            Analyzer.getInstance().setUnfiltered(data);
            App.setScene("main");
        }
    }

    /**
     * Choose one or multiple files
     * @param title the title of the file chooser window
     * @param multiple whether multiple files can be chosen
     * @param extensions choosable file extensions
     * @return the path(s) to the chosen files
     * @throws NoFileException if no file was chosen
     */
    private String chooseFile(String title, boolean multiple, Pair<String, String>... extensions) throws NoFileException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(Arrays.stream(extensions)
                .map(pair -> new FileChooser.ExtensionFilter(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList()));

        if (multiple) {
            List<File> files = fileChooser.showOpenMultipleDialog(App.getStage());
            if (files == null) {
                throw new NoFileException();
            }
            return files.stream().map(File::getAbsolutePath).collect(Collectors.joining(";"));
        } else {
            File f = fileChooser.showOpenDialog(App.getStage());
            if (f == null) {
                throw new NoFileException();
            }
            return f.getAbsolutePath();
        }
    }

    /**
     * Parse an SDAT file
     * @param path the path to the SDAT file
     * @return the parsed SDAT file
     * @throws JAXBException if the parsing failed
     */
    private SDAT parseSDAT(String path) throws JAXBException {
        List<Class<? extends SDAT>> sdats = Arrays.asList(SDAT12.class, SDAT13.class, SDAT14.class);

        for (Class<? extends SDAT> sdat : sdats) {
            JAXBContext context = JAXBContext.newInstance(sdat);
            try {
                return (SDAT) context.createUnmarshaller().unmarshal(new File(path));
            } catch (UnmarshalException e) {
                continue;
            }
        }

        throw new RuntimeException("Couldn't parse SDAT");
    }

    /**
     * Parse an ESL file
     * @param path the path to the ESL file
     * @return the parsed ESL file
     * @throws JAXBException if the parsing failed
     */
    private ESL parseESL(String path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ESL.class);
        return (ESL) context.createUnmarshaller().unmarshal(new File(path));
    }

    /**
     * Build data from SDAT and ESL files
     * @param sdats SDAT data
     * @param esl ESL data
     * @return the built data
     */
    private Data buildData(List<SDAT> sdats, ESL esl) {
        List<ValueRow> values = esl.getMeter().getTimePeriod().getValueRows();
        double usageHighTariff = getValueOfObis(values, "1-1:1.8.1");
        double usageLowTariff = getValueOfObis(values, "1-1:1.8.2");
        double supplyHighTariff = getValueOfObis(values, "1-1:2.8.1");
        double supplyLowTariff = getValueOfObis(values, "1-1:2.8.2");

        Map<LocalDateTime, Double> usageOverTime = new HashMap<>();
        Map<LocalDateTime, Double> supplyOverTime = new HashMap<>();

        for (SDAT sdat : sdats) {
            String[] ids = sdat.getHeaderInformation().getInstanceDocument().getDocumentID().split("_");
            Map<LocalDateTime, Double> vals = getValuesOverTime(sdat);
            switch (ids[ids.length-1]) {
                case "ID742": usageOverTime.putAll(vals); break;
                case "ID735": supplyOverTime.putAll(vals); break;
                default: throw new RuntimeException("Not a valid identifier: " + ids[ids.length-1]);
            }
        }

        return new Data(
                makeSorted(usageOverTime),
                makeSorted(supplyOverTime),
                usageLowTariff,
                usageHighTariff,
                supplyLowTariff,
                supplyHighTariff
        );
    }

    /**
     * Create a map of values over time from an SDAT file
     * @param sdat the SDAT data
     * @return the map
     */
    private Map<LocalDateTime, Double> getValuesOverTime(SDAT sdat) {
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
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * Get the value of a specific obis id
     * @param values the ESL values
     * @param obis the obis id
     * @return the value or 0.0 if it wasn't found
     */
    private double getValueOfObis(List<ValueRow> values, String obis) {
        for (ValueRow val : values) {
            if (val.getObis().equals(obis)) {
                return val.getValue();
            }
        }
        return 0.0;
    }

    /**
     * Create a sorted list from a time
     * @param data the data to be sorted
     * @return the sorted data
     */
    private List<Pair<LocalDateTime, Double>> makeSorted(Map<LocalDateTime, Double> data) {
        return data.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .sorted((a, b) -> b.getKey().isAfter(a.getKey()) ? -1 : 1)
                .collect(Collectors.toList());
    }
}
