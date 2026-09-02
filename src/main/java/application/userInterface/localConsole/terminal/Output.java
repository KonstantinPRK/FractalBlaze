package application.userInterface.localConsole.terminal;

import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
public class Output {
    private final PrintStream printer;

    public Output() {
        this(System.out);
    }

    Output(PrintStream printer) {
        this.printer = printer;
    }

    public void print(String text) {
        printer.print(text);
        printer.flush();
    }

    public void printLine(String text) {
        printer.println(text);
    }
}
