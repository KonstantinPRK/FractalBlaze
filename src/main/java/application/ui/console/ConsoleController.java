package application.ui.console;

import application.image.encoding.ImageFile;
import application.configuration.GenerationConfiguration;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ConsoleController {
    private final ConfigurationReader reader;
    private final ImageFileSaver imageFileSaver;

    public ConsoleController(ConfigurationReader reader, ImageFileSaver imageFileSaver) {
        this.reader = reader;
        this.imageFileSaver = imageFileSaver;
    }


    public GenerationConfiguration requestConfiguration() {
        return reader.requestConfiguration();
    }


    public void saveFractalImage(ImageFile imageFile, Path outputDirectory) {
        imageFileSaver.save(imageFile, outputDirectory);
    }
}
