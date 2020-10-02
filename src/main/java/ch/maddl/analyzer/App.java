package ch.maddl.analyzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App, main class
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        scene = new Scene(loadFXML("inputchooser"));
        stage.setScene(scene);
        stage.setTitle("Analizer");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon-white.png")));
        stage.show();
    }

    /**
     * Set a scene to be shown
     * @param fxml the name of the fxml file to be shown (without extension)
     * @throws IOException if the fxml file couldn't be read
     */
    public static void setScene(String fxml) throws IOException {
        scene = new Scene(loadFXML(fxml));
        stage.setScene(scene);
    }

    /**
     * Load an fxml file
     * @param fxml the file name (without extension)
     * @return the loaded fxml file
     * @throws IOException if the fxml file couln't be read
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The main method
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Get the current stage
     * @return the stage
     */
    public static Stage getStage() {
        return stage;
    }
}