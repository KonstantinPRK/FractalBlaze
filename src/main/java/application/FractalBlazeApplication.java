package application;

import application.imageGeneration.imageCorrector.correctors.ImagePostCorrector;
import application.imageGeneration.imageWriter.ImageFile;
import application.imageGeneration.imageWriter.ImageFileWriter;
import application.localConsoleUI.ConsoleController;
import application.picture.FractalImage;
import application.renderer.Renderer;
import application.userConfiguration.Configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class FractalBlazeApplication {
    private final ConsoleController userInterface;
    private final ImagePostCorrector imagePostCorrector;
    private final ImageFileWriter imageFileWriter;

    public FractalBlazeApplication(ConsoleController userInterface, ImagePostCorrector imagePostCorrector, ImageFileWriter imageFileWriter) {
        this.userInterface = userInterface;
        this.imagePostCorrector = imagePostCorrector;
        this.imageFileWriter = imageFileWriter;
    }

    public static void main(String[] args) {
        SpringApplication.run(FractalBlazeApplication.class, args);
    }

    @PostConstruct
    public void start() {
        Configuration configuration = userInterface.requestConfiguration();
        Renderer imageRenderer = configuration.renderer();

        FractalImage emptyCanvas = FractalImage.create(configuration.imageSize());

        FractalImage renderedImage = imageRenderer.render(
                emptyCanvas,
                configuration.visibleSpace(),
                configuration.transformationParameters(),
                configuration.transformations(),
                configuration.iterationCount(),
                configuration.randomSeed());

        FractalImage correctedImage = imagePostCorrector.applyCorrection(renderedImage);

        ImageFile imageFile = imageFileWriter.create(correctedImage, configuration.imageWriter());
        userInterface.returnFractalImage(imageFile, configuration.outputPath());
    }
}
