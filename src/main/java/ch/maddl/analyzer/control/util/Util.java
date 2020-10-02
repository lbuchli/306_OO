package ch.maddl.analyzer.control.util;

import ch.maddl.analyzer.App;
import ch.maddl.analyzer.control.util.json.DataValue;
import ch.maddl.analyzer.control.util.json.SensorData;
import com.google.gson.Gson;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    /**
     * Represent the data as CSV
     * @param data the data to be represented
     * @return the CSV data as a string
     */
    public static String asCSV(List<Pair<LocalDateTime, Double>> data) {
        StringBuilder sb = new StringBuilder("timestamp, value\n");
        data.forEach(pair -> sb.append(String.format("%s, %.1f\n",
                localDateTimeToTimestamp(pair.getKey()),
                pair.getValue()
        )));

        return sb.toString();
    }

    /**
     * Represent the data as JSON
     * @param usage the usage data
     * @param supply the supply data
     * @return the JSON data as a string
     */
    public static String asJSON(List<Pair<LocalDateTime, Double>> usage, List<Pair<LocalDateTime, Double>> supply) {
        SensorData usageData = new SensorData();
        usageData.setSensorID("742");
        usageData.setData(toDataValues(usage));

        SensorData supplyData = new SensorData();
        supplyData.setSensorID("735");
        supplyData.setData(toDataValues(supply));

        return new Gson().toJson(Arrays.asList(usageData, supplyData));
    }

    /**
     * Convert data to a marshallable bean class format
     * @param data the data to be converted
     * @return the converted data
     */
    private static List<DataValue> toDataValues(List<Pair<LocalDateTime, Double>> data) {
        return data.stream().map(pair -> {
            DataValue val = new DataValue();
            val.setTs(String.valueOf(localDateTimeToTimestamp(pair.getKey())));
            val.setValue(pair.getValue());
            return val;
        }).collect(Collectors.toList());
    }


    /**
     * Convert a {@link LocalDateTime} to a unix timestamp
     * @param time the {@link LocalDateTime} to be converted
     * @return the unix timestamp
     */
    private static long localDateTimeToTimestamp(LocalDateTime time) {
        return time.toInstant(ZoneOffset.UTC).toEpochMilli() / 1000L;
    }

    /**
     * Save a string to a file
     * @param data the data to be
     * @param name the initial file name
     * @param extensionName
     * @param extensionPattern
     * @param title
     */
    public static void saveString(String data, String name, String extensionName, String extensionPattern, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionName, extensionPattern));
        File file = fileChooser.showSaveDialog(App.getStage());
        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                writer.print(data);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
