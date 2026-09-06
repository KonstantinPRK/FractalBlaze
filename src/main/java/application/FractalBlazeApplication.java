package application;

import application.imageGeneration.imageCorrector.ImageCorrector;
import application.userConfiguration.Configuration;
import application.imageGeneration.imageWriter.ImageFile;
import application.imageGeneration.imageWriter.ImageFileWriter;
import application.localConsoleUI.ConsoleController;
import application.picture.FractalImage;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public final class FractalBlazeApplication {
    private final ConsoleController userInterface;
    private final List<ImageCorrector> imageCorrectors;
    private final ImageFileWriter imageFileWriter;

    public FractalBlazeApplication(
            ConsoleController userInterface,
            List<ImageCorrector> imageCorrectors,
            ImageFileWriter imageFileWriter
    ) {
        this.userInterface = userInterface;
        this.imageCorrectors = imageCorrectors;
        this.imageFileWriter = imageFileWriter;
    }

    public static void main(String[] args) {
        SpringApplication.run(FractalBlazeApplication.class, args);
    }

    @PostConstruct
    public void start() {
        Configuration configuration = userInterface.requestConfiguration();
        FractalImage emptyCanvas = FractalImage.create(configuration.imageSize());
        FractalImage renderedImage = configuration.renderer().render(
                emptyCanvas,
                configuration.visibleSpace(),
                configuration.transformationParameters(),
                configuration.transformations(),
                configuration.iterationCount(),
                configuration.randomSeed()
        );

        for (ImageCorrector imageCorrector : imageCorrectors) {
            imageCorrector.process(renderedImage);
        }

        ImageFile imageFile = imageFileWriter.create(
                renderedImage,
                configuration.imageWriter()
        );

        userInterface.returnFractalImage(
                imageFile,
                configuration.outputPath()
        );
    }
}
