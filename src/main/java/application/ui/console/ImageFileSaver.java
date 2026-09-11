package application.ui.console;

import application.configuration.setting.OutputFileSettings;
import application.image.encoding.ImageFile;
import application.ui.console.io.ConsolePanel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class ImageFileSaver {
    private final ConsolePanel consolePanel;
    private final OutputFileSettings settings;

    public ImageFileSaver(ConsolePanel consolePanel, OutputFileSettings settings) {
        this.consolePanel = consolePanel;
        this.settings = settings;
    }

    public Path save(ImageFile imageFile, Path outputDirectory) {
        Path normalizedOutputDirectory = outputDirectory.toAbsolutePath().normalize();
        String fileName = settings.baseFileName() + "." + imageFile.fileExtension();
        Path outputFile = findAvailableOutputFile(normalizedOutputDirectory, fileName);

        saveFile(imageFile, normalizedOutputDirectory, outputFile);
        consolePanel.showMessage("Изображение сохранено в " + outputFile);

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
