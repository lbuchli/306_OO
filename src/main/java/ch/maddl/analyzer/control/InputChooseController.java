package ch.maddl.analyzer.control;

import ch.maddl.analyzer.control.util.NoFileException;
import ch.maddl.analyzer.control.util.data.esl.ESL;
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
import java.util.Arrays;
import java.util.stream.Collectors;

public class InputChooseController {

    @FXML
    private TextField sdatTextField;

    @FXML
    private TextField eslTextField;

    @FXML
    private void onSDATChooseButtonClick() {
        try {
            sdatTextField.setText(chooseFile("SDAT-Datei auswählen", new Pair<>("SDAT", "*.xml")));
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
        boolean sdatExists = Files.exists(Path.of(sdatTextField.getText()));
        boolean eslExists = Files.exists(Path.of(eslTextField.getText()));
        if (sdatExists && eslExists) {
            try {
                Data data = buildData(parseSDAT(sdatTextField.getText()), parseESL(eslTextField.getText()));
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

    private Data buildData(SDAT sdat, ESL esl) {
        System.out.println(sdat.getHeaderInformation().getInstanceDocument().getDocumentID());
        System.out.println(sdat.getMeteringData().getInterval().getStartDateTime());
        System.out.println(sdat.getMeteringData().getResolution().getResolution());
        sdat.getMeteringData().getObservations().forEach(obs -> System.out.printf("Obs %d: %f\n", obs.getPosition().getSequence(), obs.getVolume()));
        return new Data(
          sdat.getHeaderInformation().getInstanceDocument().getDocumentID(),
          null, // TODO
          0,0 // TODO
        );
    }
}
