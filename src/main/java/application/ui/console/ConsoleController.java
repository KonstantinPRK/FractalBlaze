package application.ui.console;

import application.configuration.GenerationConfiguration;
import application.model.FractalImage;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
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


    public void saveFractalImage(FractalImage image, ImageWriter imageWriter, Path outputDirectory) {
        imageFileSaver.save(image, imageWriter, outputDirectory);
    }
}
