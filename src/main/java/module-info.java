module ch.maddl.analyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.xml.bind;
    requires java.sql;
    requires com.google.gson;

    opens ch.maddl.analyzer.control to javafx.fxml;
    opens ch.maddl.analyzer.control.util.xml.sdat to java.xml.bind;
    opens ch.maddl.analyzer.control.util.xml.esl to java.xml.bind;
    opens ch.maddl.analyzer.control.util.json to java.xml.bind;
    exports ch.maddl.analyzer;
}