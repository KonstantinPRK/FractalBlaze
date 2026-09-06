package application.localConsoleUI.terminal;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Input {
    private final Scanner scanner;

    public Input() {
        this(new Scanner(System.in));
    }

    Input(Scanner scanner) {
        this.scanner = scanner;
    }

    public Integer readInt() {
        String enteredValue = readLine();

        if (enteredValue == null) return null;

        try {
            return Integer.valueOf(enteredValue.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public String readLine() {
        return scanner.nextLine();
    }
}
