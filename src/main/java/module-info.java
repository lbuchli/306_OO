module ch.maddl.analyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.xml.bind;
    requires java.sql;

    opens ch.maddl.analyzer.control to javafx.fxml;
    opens ch.maddl.analyzer.control.util.data.sdat to java.xml.bind;
    opens ch.maddl.analyzer.control.util.data.esl to java.xml.bind;
    exports ch.maddl.analyzer;
}