package application.localConsoleUI.terminal;

import org.springframework.stereotype.Component;

import java.util.List;

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

            if (userNumber >= min && userNumber <= max) {
                return userNumber;
            }

            output.print("Ошибка: число вне указанного диапазона. Попробуйте снова: ");
        }
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
