package ch.maddl.analyzer.control;

import ch.maddl.analyzer.App;
import ch.maddl.analyzer.model.Analyzer;
import javafx.fxml.FXML;
import javafx.scene.control.Control;

import java.io.IOException;

public class MainController {

    @FXML
    private void onOpenMenuItemClicked() throws IOException {
        App.setRoot("inputchooser");
        App.getStage().setWidth(476.0);
        App.getStage().setHeight(325.0);
    }

    @FXML
    private void onExportAsCSVMenuItemClicked() {
    }
}
