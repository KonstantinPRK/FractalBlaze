package application.localConsoleUI;

import application.imageGeneration.imageWriter.ImageFile;
import application.userConfiguration.Configuration;
import application.localConsoleUI.readerRecorder.ConfigurationReader;
import application.localConsoleUI.readerRecorder.FileRecorder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ConsoleController {
    private final ConfigurationReader reader;
    private final FileRecorder recorder;

    public ConsoleController(ConfigurationReader reader, FileRecorder recorder) {
        this.reader = reader;
        this.recorder = recorder;
    }


    public Configuration requestConfiguration() {
        return reader.requestConfiguration();
    }


    public void returnFractalImage(ImageFile imageFile, Path outputDirectory) {
        recorder.returnFractalImage(imageFile, outputDirectory);
    }
}
