package application.userInterface.localConsole.terminal;

import org.springframework.stereotype.Component;

/**
 * Компонент для работы с терминалом, объединяющий возможности форматирования,
 * вывода и ввода данных. Предоставляет методы для печати с форматированием,
 * вывода нумерованных списков и получения целых чисел от пользователя.
 *
 * @author unknown
 * @version 1.0
 */
@Component
public class Terminal {
    private final Editor edit;
    private final Output output;
    private final Input input;

    /* String SEPARATOR = ": ",
            ERROR = "Ошибка",
            IntegerOutOfRange = "число вне диапазона. ",
            notAnInteger = "введено не целое число. ",
            offerToEnterNumber = "Введите число ",
            from = "от ",
            to = "до ",
            inclusive = "включительно",
            TryAgain = "Попробуйте снова. ";

     */



    public Terminal(Editor edit, Output output, Input input) {
        this.edit = edit;
        this.output = output;
        this.input = input;
    }



    public void unformattedPrint(String... text) {
        for (String line : text) {
            output.print(line);
            output.printEmptyLines(1);
        }
    }



    public void printSystemDescription(String description) {
        output.print(description);
        output.printEmptyLines(1);
    }



    public void printUserParameter(String optionName, String SEPARATOR, String optionValue) {
        output.print(
                edit.bold(optionName),
                edit.bold(SEPARATOR),
                optionValue
        );

        output.printEmptyLines(0);
    }



    public void printNumberedOptions(String... options) {
        for (int number = 1; number <= options.length; number++) {
            output.print(
                    edit.bold(String.valueOf(number)),
                    SEPARATOR,
                    options[number - 1]
            );

            output.printEmptyLines(0);
        }

        output.printEmptyLines(1);
    }




    public int getUserInt(int min, int max) {
        output.print();
        output.printEmptyLines(0);

        while (true) {
            Integer userInt = input.readInt();
            output.printEmptyLines(0);

            if (userInt == null) {
                output.print();
                output.printEmptyLines(0);
                continue;
            }

            if (userInt >= min && userInt <= max) {
                return userInt;
            }

            output.print();
            output.printEmptyLines(0);
        }
    }
}
