package application.ui.console.io;

import application.configuration.restriction.IntegerRange;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public final class ConsoleInputParser {

    public int parseInt(String enteredValue) {
        return Integer.parseInt(requireEnteredValue(enteredValue));
    }


    public int[] parseIntArray(String enteredValue, IntegerRange restrictions) {
        String[] selectionParts = requireEnteredValue(enteredValue).split("[,\\s]+");
        Set<Integer> selectedNumbers = new LinkedHashSet<>();

        for (String selectionPart : selectionParts) addSelectedNumbers(selectionPart, restrictions, selectedNumbers);
        if (selectedNumbers.isEmpty()) throw new IllegalArgumentException("Необходимо выбрать хотя бы один номер");

        return selectedNumbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }


    public Path parsePath(String enteredValue) {
        return Path.of(requireEnteredValue(enteredValue)).toAbsolutePath().normalize();
    }


    private void addSelectedNumbers(String selectionPart, IntegerRange restrictions, Set<Integer> selectedNumbers) {
        String[] rangeBorders = selectionPart.split("-", 2);

        int rangeStart = parseInt(rangeBorders[0]);
        int rangeEnd = rangeBorders.length == 1 ? rangeStart : parseInt(rangeBorders[1]);

        boolean rangeIsInvalid = rangeStart > rangeEnd
                || !restrictions.contains(rangeStart)
                || !restrictions.contains(rangeEnd);

        if (rangeIsInvalid) throw new IllegalArgumentException("Указан недопустимый диапазон чисел");

        for (int selectedNumber = rangeStart; selectedNumber <= rangeEnd; selectedNumber++) selectedNumbers.add(selectedNumber);
    }


    private String requireEnteredValue(String enteredValue) {
        if (enteredValue == null || enteredValue.isBlank()) throw new IllegalArgumentException("Введённое значение не должно быть пустым");
        return enteredValue.trim();
    }
}
