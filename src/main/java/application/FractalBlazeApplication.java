package application;

import application.configuration.GenerationConfiguration;
import application.image.encoding.ImageEncoder;
import application.image.encoding.ImageFile;
import application.image.processing.ImagePostProcessor;
import application.model.FractalImage;
import application.rendering.renderer.Renderer;
import application.ui.console.ConsoleController;
import org.springframework.stereotype.Component;

@Component
public final class FractalBlazeApplication {
    private final ConsoleController userInterface;
    private final ImagePostProcessor imagePostProcessor;
    private final ImageEncoder imageEncoder;

    public FractalBlazeApplication(ConsoleController userInterface, ImagePostProcessor imagePostProcessor, ImageEncoder imageEncoder) {
        this.userInterface = userInterface;
        this.imagePostProcessor = imagePostProcessor;
        this.imageEncoder = imageEncoder;
    }

    public void start() {
        GenerationConfiguration configuration = userInterface.requestConfiguration();

        Renderer imageRenderer = configuration.renderer();

        FractalImage emptyCanvas = FractalImage.create(configuration.imageSize());
        FractalImage renderedImage = imageRenderer.render(
                emptyCanvas,
                configuration.visibleSpace(),
                configuration.transformationParameters(),
                configuration.transformations(),
                configuration.iterationCount(),
                configuration.randomSeed());

        FractalImage correctedImage = imagePostProcessor.process(renderedImage);

        ImageFile imageFile = imageEncoder.encode(correctedImage, configuration.imageWriter());
        userInterface.saveFractalImage(imageFile, configuration.outputPath());
    }
}
