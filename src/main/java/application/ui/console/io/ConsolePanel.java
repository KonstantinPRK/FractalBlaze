package application.ui.console.io;

import application.configuration.restriction.IntegerRange;
import application.configuration.setting.ConsoleLayoutSettings;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public final class ConsolePanel {
    private final Output output;
    private final Input input;
    private final ConsoleInputParser parser;
    private final ConsoleLayoutSettings layoutSettings;

    public ConsolePanel(Output output, Input input, ConsoleInputParser parser, ConsoleLayoutSettings layoutSettings) {
        this.output = output;
        this.input = input;
        this.parser = parser;
        this.layoutSettings = layoutSettings;
    }

    public void showMessage(String message) {
        output.printEmptyLines(layoutSettings.emptyLinesBeforeRequest());
        output.printLine(message);
    }

    public String requestText(String parameterName, String example) {
        showRequest(parameterName, example, null, null);

        while (true) {
            String enteredValue = input.readLine();
            if (enteredValue != null && !enteredValue.isBlank())return enteredValue.trim();
            showError("значение не должно быть пустым");
        }
    }

    public Path requestPath(String parameterName, String example) {
        showRequest(parameterName, example, null, null);

        while (true) {
            try {
                return parser.parsePath(input.readLine());
            } catch (IllegalArgumentException exception) {
                showError("не удалось распознать путь");
            }
        }
    }

    public int requestInt(String parameterName, String example, IntegerRange restrictions) {
        showRequest(parameterName, example, null, restrictions);
        return readInt(restrictions);
    }

    public String requestOption(String parameterName, List<String> options) {
        requireOptions(options);
        IntegerRange optionRestrictions = new IntegerRange(1, options.size());
        showRequest(parameterName, null, options, optionRestrictions);
        return options.get(readInt(optionRestrictions) - 1);
    }

    public List<String> requestOptions(String parameterName, String example, List<String> options) {
        requireOptions(options);
        IntegerRange optionRestrictions = new IntegerRange(1, options.size());
        showRequest(parameterName, example, options, optionRestrictions);

        while (true) {
            try {
                int[] selectedOptionNumbers = parser.parseIntArray(input.readLine(), optionRestrictions);
                List<String> selectedOptions = new ArrayList<>(selectedOptionNumbers.length);

                for (int selectedOptionNumber : selectedOptionNumbers) {
                    if (!optionRestrictions.contains(selectedOptionNumber))throw new IllegalArgumentException("Option number is outside the allowed range");
                    selectedOptions.add(options.get(selectedOptionNumber - 1));
                }

                return List.copyOf(selectedOptions);
            } catch (IllegalArgumentException exception) {
                showError("укажите один или несколько номеров из предложенного диапазона");
            }
        }
    }

    public void printNumberedOptions(List<String> options) {
        for (int optionIndex = 0; optionIndex < options.size(); optionIndex++) output.printLine(optionIndex + 1 + ": " + options.get(optionIndex));
    }

    private int readInt(IntegerRange restrictions) {
        while (true) {
            try {
                int enteredNumber = parser.parseInt(input.readLine());
                if (restrictions.contains(enteredNumber))return enteredNumber;
                showError("число находится вне допустимого диапазона");
            } catch (IllegalArgumentException exception) {
                showError("введено не целое число");
            }
        }
    }

    private void showRequest(String parameterName, String example, List<String> options, IntegerRange restrictions) {
        output.printEmptyLines(layoutSettings.emptyLinesBeforeRequest());
        output.printLine(parameterName);

        if (example != null && !example.isBlank())output.printLine(layoutSettings.examplePrefix() + example);
        if (options != null)printNumberedOptions(options);
        if (restrictions != null)output.printLine(layoutSettings.restrictionsPrefix() + "от " + restrictions.minimum() + " до " + restrictions.maximum() + " включительно");

        output.print(layoutSettings.inputPrompt());
    }

    private void showError(String errorMessage) {
        output.printLine("Ошибка: " + errorMessage + ". Попробуйте снова.");
        output.print(layoutSettings.inputPrompt());
    }

    private void requireOptions(List<String> options) {
        Objects.requireNonNull(options, "Options must not be null");
        if (options.isEmpty())throw new IllegalArgumentException("Options must not be empty");
    }
}
