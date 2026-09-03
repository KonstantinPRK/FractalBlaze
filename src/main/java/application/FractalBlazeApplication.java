package application;

import application.imageCorrector.ImageCorrector;
import application.parameters.Configuration;
import application.userInterface.ImageFileWriter;
import application.userInterface.UserInterface;
import application.world.FractalImage;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
public final class FractalBlazeApplication {
    private final UserInterface userInterface;
    private final List<ImageCorrector> imageCorrectors;

    public FractalBlazeApplication(
            UserInterface userInterface,
            List<ImageCorrector> imageCorrectors
    ) {
        this.userInterface = userInterface;
        this.imageCorrectors = imageCorrectors;
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
                configuration.transformations(),
                configuration.iterationCount(),
                configuration.randomSeed()
        );

        for (ImageCorrector imageCorrector : imageCorrectors) {
            imageCorrector.process(renderedImage);
        }

        ImageFileWriter imageWriter = new ImageFileWriter();
        Path savedImagePath = imageWriter.write(renderedImage, configuration.outputPath(), configuration.imageWriter());

        userInterface.returnFractalImage(savedImagePath);
    }
}


