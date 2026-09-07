package application.localConsoleUI.consolePanel;

import application.core.restrictions.IntegerRange;
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
        if (selectedNumbers.isEmpty())throw new IllegalArgumentException("At least one number must be selected");

        int[] selectedNumberArray = new int[selectedNumbers.size()];
        int selectedNumberIndex = 0;
        for (int selectedNumber : selectedNumbers) selectedNumberArray[selectedNumberIndex++] = selectedNumber;

        return selectedNumberArray;
    }

    public Path parsePath(String enteredValue) {
        return Path.of(requireEnteredValue(enteredValue)).toAbsolutePath().normalize();
    }

    private void addSelectedNumbers(String selectionPart, IntegerRange restrictions, Set<Integer> selectedNumbers) {
        if (!selectionPart.contains("-")) {
            selectedNumbers.add(parseRestrictedInt(selectionPart, restrictions));
            return;
        }

        String[] rangeBorders = selectionPart.split("-", -1);
        if (rangeBorders.length != 2)throw new IllegalArgumentException("Invalid number range");

        int rangeStart = parseRestrictedInt(rangeBorders[0], restrictions);
        int rangeEnd = parseRestrictedInt(rangeBorders[1], restrictions);
        if (rangeStart > rangeEnd)throw new IllegalArgumentException("Range start must not exceed range end");

        for (int selectedNumber = rangeStart; selectedNumber <= rangeEnd; selectedNumber++) selectedNumbers.add(selectedNumber);
    }

    private int parseRestrictedInt(String enteredValue, IntegerRange restrictions) {
        int enteredNumber = parseInt(enteredValue);
        if (!restrictions.contains(enteredNumber))throw new IllegalArgumentException("Number is outside the allowed range");
        return enteredNumber;
    }

    private String requireEnteredValue(String enteredValue) {
        if (enteredValue == null || enteredValue.isBlank())throw new IllegalArgumentException("Entered value must not be blank");
        return enteredValue.trim();
    }
}
