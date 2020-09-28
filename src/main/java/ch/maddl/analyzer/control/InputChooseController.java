package ch.maddl.analyzer.control;

import ch.maddl.analyzer.control.util.DataParser;
import ch.maddl.analyzer.control.util.NoFileException;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ch.maddl.analyzer.App;
import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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

    public void onSDATChooseButtonClick() {
        try {
            sdatTextField.setText(chooseFile("SDAT-Datei auswählen", new Pair<>("SDAT", "*.xml")));
        } catch (NoFileException e) {}
    }

    public void onESLChooseButtonClick() {
        try {
            eslTextField.setText(chooseFile("ESL-Datei auswählen", new Pair<>("ESL", "*.xml")));
        } catch (NoFileException e) {}
    }

    public void onNextButtonClick() throws IOException {
        boolean sdatExists = Files.exists(Path.of(sdatTextField.getText()));
        boolean eslExists = Files.exists(Path.of(eslTextField.getText()));
        if (sdatExists && eslExists) {
            try {
                DataParser parser = new DataParser(sdatTextField.getText(), eslTextField.getText());
            } catch (ParserConfigurationException | SAXException e) {
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
}
