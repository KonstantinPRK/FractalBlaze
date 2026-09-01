package application.userInterface.localConsole.terminal;

import org.springframework.stereotype.Component;

import java.util.Scanner;


/**
 * Компонент для чтения ввода пользователя из стандартного потока ввода.
 * Предоставляет метод для считывания целых чисел с обработкой ошибок.
 *
 * @author unknown
 * @version 1.0
 */
@Component
public class Input {
    private final Scanner scan;


    /**
     * Создаёт экземпляр Input с указанным сканером.
     *
     * @param scan сканер для чтения данных из стандартного ввода
     */
    public Input(Scanner scan) {
        this.scan = scan;
    }


    /**
     * Считывает целое число из ввода. Если введено некорректное значение
     * (не целое число), метод возвращает {@code null}, предварительно
     * очищая буфер ввода.
     *
     * @return считанное целое число или {@code null}, если ввод не является целым числом
     */
    public Integer readInt() {
        if (scan.hasNextInt()) {
            int value = scan.nextInt();
            scan.nextLine();
            return value;
        } else {
            scan.nextLine();
            return null;
        }
    }
}
