package application.userInterface.localConsole.terminal;

import org.springframework.stereotype.Component;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

@Component
public class Terminal {
    private static final String OPTION_SEPARATOR = ": ";

    private final Output output;
    private final Input input;

    public Terminal(Output output, Input input) {
        this.output = output;
        this.input = input;
    }

    public void unformattedPrint(String... text) {
        for (String line : text) {
            output.printLine(line);
        }
    }

    public void printNumberedOptions(List<String> options) {
        for (int optionIndex = 0; optionIndex < options.size(); optionIndex++) {
            int optionNumber = optionIndex + 1;

            output.printLine(
                    optionNumber
                            + OPTION_SEPARATOR
                            + options.get(optionIndex)
            );
        }

        output.printLine("");
    }

    public int getUserInt(int min, int max) {
        output.print(
                "Введите целое число от "
                        + min
                        + " до "
                        + max
                        + " включительно: "
        );

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

    public Path getPath() {
        while (true) {
            String enteredPath = input.readLine();

            if (enteredPath == null || enteredPath.isBlank()) {
                output.print("Путь не должен быть пустым. Попробуйте снова: ");
                continue;
            }

            try {
                return Path.of(enteredPath.trim()).toAbsolutePath().normalize();
            } catch (InvalidPathException exception) {
                output.print("Не удалось распознать путь. Попробуйте снова: ");
            }
        }
    }
}
