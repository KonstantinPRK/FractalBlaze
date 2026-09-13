package application.ui.console;

import application.configuration.setting.OutputFileSettings;
import application.image.encoding.ImageEncoder;
import application.model.FractalImage;
import application.ui.console.io.ConsolePanel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileAlreadyExistsException;
import java.util.Locale;

@Component
public class ImageFileSaver {
    private final ConsolePanel consolePanel;
    private final OutputFileSettings settings;
    private final ImageEncoder imageEncoder;

    public ImageFileSaver(ConsolePanel consolePanel, OutputFileSettings settings, ImageEncoder imageEncoder) {
        this.consolePanel = consolePanel;
        this.settings = settings;
        this.imageEncoder = imageEncoder;
    }

    public Path save(FractalImage image, ImageWriter imageWriter, Path outputDirectory) {
        Path normalizedOutputDirectory = outputDirectory.toAbsolutePath().normalize();
        String fileExtension = imageWriter.getOriginatingProvider().getFileSuffixes()[0].toLowerCase(Locale.ROOT);
        String fileName = settings.baseFileName() + "." + fileExtension;
        Path outputFile = reserveOutputFile(normalizedOutputDirectory, fileName);

        try {
            imageEncoder.encode(image, imageWriter, outputFile);
        } catch (RuntimeException | Error exception) {
            deleteIncompleteFile(outputFile, exception);
            throw exception;
        }

        consolePanel.showMessage("Изображение сохранено в " + outputFile);

        return outputFile;
    }

    private Path reserveOutputFile(Path outputDirectory, String fileName) {
        int extensionStart = fileName.lastIndexOf('.');
        String nameWithoutExtension = fileName.substring(0, extensionStart);
        String extension = fileName.substring(extensionStart);

        try {
            Files.createDirectories(outputDirectory);

            for (int fileNumber = 0; ; fileNumber++) {
                String availableFileName = fileNumber == 0
                        ? fileName
                        : nameWithoutExtension + "-" + fileNumber + extension;

                try {
                    return Files.createFile(outputDirectory.resolve(availableFileName));
                } catch (FileAlreadyExistsException ignored) {
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Не удалось подготовить файл изображения в каталоге: " + outputDirectory, exception);
        }
    }

    private void deleteIncompleteFile(Path outputFile, Throwable originalException) {
        try {
            Files.deleteIfExists(outputFile);
        } catch (IOException deletionException) {
            originalException.addSuppressed(deletionException);
        }
    }
}
