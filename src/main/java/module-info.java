module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens ch.maddl.analyzer.control to javafx.fxml;
    exports ch.maddl.analyzer;
}