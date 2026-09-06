package application.localConsoleUI.terminal;

import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
public class Output {
    private final PrintStream printer;

    public Output() {
        this(System.out);
    }

    public Output(PrintStream printer) {
        this.printer = printer;
    }


    public void print(String text) {
        printer.println(text);
        printer.flush();
    }


    public void printEmptyLine(int count) {
        for (int i = 0; i < count; i++) printer.println();
    }
}
