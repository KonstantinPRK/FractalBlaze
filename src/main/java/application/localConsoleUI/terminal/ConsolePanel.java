package application.localConsoleUI.terminal;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class ConsolePanel {
    private final Output output;
    private final Input input;

    public ConsolePanel(Output output, Input input) {
        this.output = output;
        this.input = input;
    }


    public void printText(String... text) {
        for (String line : text) output.print(line);
    }


    public void printNumberedOptions(List<String> options) {
        for (int index = 0; index < options.size(); index++) {
            int optionNumber = index + 1;

            output.print(optionNumber + ": " + options.get(index));
        }

        output.printEmptyLine(1);
    }


    public Integer getUserInt(int min, int max) {
        output.print("Введите целое число от " + min + " до " + max + " включительно: ");

        while (true) {
            Integer userNumber = input.readInt();

            if (userNumber == null) {
                output.print("Ошибка: введено не целое число. Попробуйте снова: ");
                continue;
            }

            if (userNumber >= min && userNumber <= max)return userNumber;

            output.print("Ошибка: число вне указанного диапазона. Попробуйте снова: ");
        }
    }

    public int[] getUserIntArray(int min, int max) {
        output.print("Введите номера через пробел, запятую или диапазоном, например 1-" + max + ": ");

        while (true) {
            try {
                return parseUserIntArray(input.readLine(), min, max);
            } catch (IllegalArgumentException exception) {
                output.print("Ошибка: укажите номера от " + min + " до " + max + ", например 1 3 5 или 1-" + max + ": ");
            }
        }
    }

    private int[] parseUserIntArray(String enteredValue, int min, int max) {
        if (enteredValue == null || enteredValue.isBlank())throw new IllegalArgumentException();

        Set<Integer> selectedNumbers = new LinkedHashSet<>();
        String[] selectionParts = enteredValue.trim().split("[,\\s]+");

        for (String selectionPart : selectionParts) addSelectedNumbers(selectionPart, min, max, selectedNumbers);

        int[] selectedNumberArray = new int[selectedNumbers.size()];
        int selectedNumberIndex = 0;

        for (int selectedNumber : selectedNumbers) selectedNumberArray[selectedNumberIndex++] = selectedNumber;

        return selectedNumberArray;
    }

    private void addSelectedNumbers(String selectionPart, int min, int max, Set<Integer> selectedNumbers) {
        if (!selectionPart.contains("-")) {
            selectedNumbers.add(parseSelectedNumber(selectionPart, min, max));
            return;
        }

        String[] rangeBorders = selectionPart.split("-", -1);
        if (rangeBorders.length != 2)throw new IllegalArgumentException();

        int rangeStart = parseSelectedNumber(rangeBorders[0], min, max);
        int rangeEnd = parseSelectedNumber(rangeBorders[1], min, max);
        if (rangeStart > rangeEnd)throw new IllegalArgumentException();

        for (int selectedNumber = rangeStart; selectedNumber <= rangeEnd; selectedNumber++) selectedNumbers.add(selectedNumber);
    }

    private int parseSelectedNumber(String enteredNumber, int min, int max) {
        int selectedNumber = Integer.parseInt(enteredNumber);
        if (selectedNumber < min || selectedNumber > max)throw new IllegalArgumentException();
        return selectedNumber;
    }


    public String getUserString() {
        while (true) {
            String enteredText = input.readLine();

            if (enteredText == null || enteredText.isBlank()) {
                output.print("Значение не должно быть пустым. Попробуйте снова: ");
                continue;
            }

            return enteredText.trim();
        }
    }
}
