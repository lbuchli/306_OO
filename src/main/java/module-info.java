module ch.maddl.analyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens ch.maddl.analyzer.control to javafx.fxml;
    exports ch.maddl.analyzer;
}