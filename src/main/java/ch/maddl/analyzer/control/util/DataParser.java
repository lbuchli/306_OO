package ch.maddl.analyzer.control.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class DataParser {

    private Document sdat;
    private Document esl;

    public DataParser(String sdat, String esl) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder sdatBuilder = factory.newDocumentBuilder();
        DocumentBuilder eslBuilder = factory.newDocumentBuilder();
        this.sdat = sdatBuilder.parse(sdat);
        this.esl = eslBuilder.parse(esl);
    }

    public String getID() {
        throw new RuntimeException("Not implemented");
    }

    public Map<Date, Float> getUsageOverTime() {
        throw new RuntimeException("Not implemented");
    }

    public Float getUsageLowTariff() {
        throw new RuntimeException("Not implemented");
    }

    public Float getUsageHighTariff() {
        throw new RuntimeException("Not implemented");
    }
}
