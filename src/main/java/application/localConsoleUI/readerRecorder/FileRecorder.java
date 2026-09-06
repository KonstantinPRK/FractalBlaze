package application.localConsoleUI.readerRecorder;

import application.imageGeneration.imageWriter.ImageFile;
import application.localConsoleUI.terminal.ConsolePanel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class FileRecorder {
    private static final String IMAGE_FILE_NAME = "fractal";

    private final ConsolePanel consolePanel;

    public FileRecorder(ConsolePanel consolePanel) {
        this.consolePanel = consolePanel;
    }

    public Path returnFractalImage(ImageFile imageFile, Path outputDirectory) {
        Path normalizedOutputDirectory = outputDirectory.toAbsolutePath().normalize();
        String fileName = IMAGE_FILE_NAME + "." + imageFile.fileExtension();
        Path outputFile = findAvailableOutputFile(normalizedOutputDirectory, fileName);

        saveFile(imageFile, normalizedOutputDirectory, outputFile);
        consolePanel.printText("Изображение сохранено в " + outputFile);

        return outputFile;
    }

    private Path findAvailableOutputFile(Path outputDirectory, String fileName) {
        Path requestedOutputFile = outputDirectory.resolve(fileName);

        if (!Files.exists(requestedOutputFile))return requestedOutputFile;

        int extensionStart = fileName.lastIndexOf('.');
        String nameWithoutExtension = fileName.substring(0, extensionStart);
        String extension = fileName.substring(extensionStart);
        int fileNumber = 1;

        Path availableOutputFile;
        do {
            availableOutputFile = outputDirectory.resolve(nameWithoutExtension + "-" + fileNumber + extension);
            fileNumber++;
        } while (Files.exists(availableOutputFile));

        return availableOutputFile;
    }

    private void saveFile(ImageFile imageFile, Path outputDirectory, Path outputFile) {
        try {
            Files.createDirectories(outputDirectory);
            Files.write(outputFile, imageFile.content(), StandardOpenOption.CREATE_NEW);
        } catch (IOException exception) {
            throw new UncheckedIOException("Не удалось сохранить файл: " + outputFile, exception);
        }
    }
}
