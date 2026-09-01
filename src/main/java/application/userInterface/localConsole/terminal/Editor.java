package application.userInterface.localConsole.terminal;

import org.springframework.stereotype.Component;

/**
 * Утилитный класс для форматирования текста в консольных приложениях.
 * Предоставляет методы для создания рамок и применения ANSI-стилей.
 */
@Component
public class Editor {
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char TOP_RIGHT_CORNER = '╗';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char BOTTOM_RIGHT_CORNER = '╝';
    private static final char HORIZONTAL_BORDER = '═';
    private static final char VERTICAL_BORDER = '║';
    private static final char SPACE = ' ';
    private static final String ANSI_BOLD_START = "\033[1m";
    private static final String ANSI_BOLD_END = "\033[0m";


    /**
     * Обрамляет переданное сообщение в рамку из псевдографических символов.
     * Пример вывода:
     * <pre>
     * ╔═══════════════════╗
     * ║  Привет, мир!     ║
     * ╚═══════════════════╝
     * </pre>
     *
     * @param message текст, который нужно поместить в рамку (не должен быть null)
     * @return строка с рамкой, содержащая переданное сообщение
     */
    public String frame(String message) {
        int messageLength = message.length();
        String horizontalLine = String.valueOf(HORIZONTAL_BORDER).repeat(messageLength + 2);

        return new StringBuilder()
                .append(TOP_LEFT_CORNER)
                .append(horizontalLine)
                .append(TOP_RIGHT_CORNER)
                .append('\n')
                .append(VERTICAL_BORDER)
                .append(SPACE)
                .append(message)
                .append(SPACE)
                .append(VERTICAL_BORDER)
                .append('\n')
                .append(BOTTOM_LEFT_CORNER)
                .append(horizontalLine)
                .append(BOTTOM_RIGHT_CORNER)
                .toString();
    }


    /**
     * Преобразует текст в жирное начертание с использованием ANSI-кодов.
     *
     * @param text исходный текст
     * @return текст, обёрнутый в ANSI-коды жирного стиля
     */
    public String bold(String text) {
        return ANSI_BOLD_START + text + ANSI_BOLD_END;
    }
}
