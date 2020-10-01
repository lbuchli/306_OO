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
            try {
                List<SDAT> sdats = new ArrayList<>();
                for (String path : sdatTextField.getText().split(";")) {
                    sdats.add(parseSDAT(path));
                }

                Data data = buildData(
                        sdats,
                        parseESL(eslTextField.getText())
                );

                Analyzer.getInstance().setData(data);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            App.setScene("main");
        }
    }

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

    private ESL parseESL(String path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ESL.class);
        return (ESL) context.createUnmarshaller().unmarshal(new File(path));
    }

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

    private double getValueOfObis(List<ValueRow> values, String obis) {
        for (ValueRow val : values) {
            if (val.getObis().equals(obis)) {
                return val.getValue();
            }
        }
        return 0.0;
    }

    private List<Pair<LocalDateTime, Double>> makeSorted(Map<LocalDateTime, Double> data) {
        return data.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .sorted((a, b) -> b.getKey().isAfter(a.getKey()) ? 1 : -1)
                .collect(Collectors.toList());
    }
}
