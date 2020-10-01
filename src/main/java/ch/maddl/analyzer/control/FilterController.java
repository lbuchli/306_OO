package ch.maddl.analyzer.control;

import ch.maddl.analyzer.model.Analyzer;
import ch.maddl.analyzer.model.Data;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FilterController {

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    private void initialize() {
        List<Pair<LocalDateTime, Double>> usage = Analyzer.getInstance().getData().getUsageOverTime();
        LocalDate from;
        LocalDate to;
        try {
            from = usage.get(0).getKey().toLocalDate();
            to   = usage.get(usage.size()-1).getKey().toLocalDate();
        } catch (NullPointerException e) {
            return;
        }

        StringConverter<LocalDate> converter = new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        datePickerFrom.setValue(from);
        datePickerFrom.setConverter(converter);
        datePickerTo.setValue(to);
        datePickerTo.setConverter(converter);
    }

    @FXML
    private void onClearFilterClicked() {
        Analyzer.getInstance().setData(Analyzer.getInstance().getUnfiltered());
    }

    @FXML
    private void onOkClicked() {
        LocalDate from = datePickerFrom.getValue();
        LocalDate to = datePickerTo.getValue();
        Data unfiltered = Analyzer.getInstance().getUnfiltered();
        Analyzer.getInstance().setData(new Data(
                filter(unfiltered.getUsageOverTime(), from, to),
                filter(unfiltered.getSupplyOverTime(), from, to),
                unfiltered.getUsageLowTariff(),
                unfiltered.getUsageHighTariff(),
                unfiltered.getSupplyLowTariff(),
                unfiltered.getSupplyHighTariff()
        ));

        Stage stage = (Stage) datePickerFrom.getScene().getWindow();
        stage.close();
    }

    private List<Pair<LocalDateTime, Double>> filter(List<Pair<LocalDateTime, Double>> data, LocalDate from, LocalDate to) {
        return List.copyOf(data).stream()
                .dropWhile(p -> p.getKey().toLocalDate().isBefore(from))
                .takeWhile(p -> p.getKey().toLocalDate().isBefore(to) || p.getKey().toLocalDate().isEqual(to))
                .collect(Collectors.toList());
    }
}
