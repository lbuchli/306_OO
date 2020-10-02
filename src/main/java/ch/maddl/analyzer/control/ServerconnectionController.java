package ch.maddl.analyzer.control;

import ch.maddl.analyzer.control.util.Util;
import ch.maddl.analyzer.model.Analyzer;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * The controller for the serverconnection screen
 */
public class ServerconnectionController {

    @FXML
    private TextField urlField;

    @FXML
    private void onCancelClicked() {
        Stage stage = (Stage) urlField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onPublishClicked() {
        String data = Util.asJSON(
                Analyzer.getInstance().getData().getUsageOverTime(),
                Analyzer.getInstance().getData().getSupplyOverTime()
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlField.getText()))
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();
        } catch (URISyntaxException e) {
            return;
        }

        while (true) {
            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
                Stage stage = (Stage) urlField.getScene().getWindow();
                stage.close();
                return;
            } catch (IOException e) {
                return;
            } catch (InterruptedException e) {
                continue;
            }
        }
    }
}
