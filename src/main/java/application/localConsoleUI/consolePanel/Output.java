package application.localConsoleUI.consolePanel;

import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
public class Output {
    private final PrintStream printer;

    public Output(PrintStream printer) {
        this.printer = printer;
    }

    public void print(String text) {
        printer.print(text);
        printer.flush();
    }

    public void printLine(String text) {
        printer.println(text);
        printer.flush();
    }

    public void printEmptyLines(int count) {
        for (int i = 0; i < count; i++) printer.println();
        printer.flush();
    }
}
