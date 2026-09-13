package application.ui.console.io;

import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class Input {
    private final Scanner scanner;

    public Input(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine() {
        if (!scanner.hasNextLine()) throw new NoSuchElementException("Консольный ввод завершён");

        return scanner.nextLine();
    }
}
