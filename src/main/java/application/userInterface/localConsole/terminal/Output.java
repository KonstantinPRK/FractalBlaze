package application.userInterface.localConsole.terminal;

import org.springframework.stereotype.Component;

import java.io.PrintStream;

/**
 * Компонент для вывода текстовых данных в стандартный поток вывода.
 * Предоставляет методы для печати строк и пустых строк.
 *
 * @author unknown
 * @version 1.0
 */
@Component
public class Output {
    private final PrintStream printer;


    /**
     * Создаёт экземпляр Output с указанным потоком вывода.
     *
     * @param printer поток вывода, в который будут направляться данные
     */
    public Output(PrintStream printer) {
        this.printer = printer;
    }


    /**
     * Печатает переданные строки последовательно без разделителей и без перевода строки.
     * Все строки выводятся подряд в одной строке.
     *
     * @param str строки для вывода (могут быть переданы несколько)
     */
    public void print(String... str) {
        for (String line : str) printer.print(line);
    }


    /**
     * Печатает указанное количество пустых строк (переводов строки).
     * Если значение {@code count} равно 0, печатается одна пустая строка.
     * При {@code count} больше 0 печатается ровно {@code count} пустых строк.
     *
     * @param count количество пустых строк, которое требуется напечатать
     */
    public void printEmptyLines(int count) {
        if (count == 0) printer.println();
        for (int i = 0; i < count; i++) printer.println();
    }
}
