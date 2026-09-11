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
    private final Renderer imageRenderer;
    private final ImagePostProcessor imagePostProcessor;
    private final ImageEncoder imageEncoder;

    public FractalBlazeApplication(ConsoleController userInterface, Renderer imageRenderer, ImagePostProcessor imagePostProcessor, ImageEncoder imageEncoder) {
        this.userInterface = userInterface;
        this.imageRenderer = imageRenderer;
        this.imagePostProcessor = imagePostProcessor;
        this.imageEncoder = imageEncoder;
    }

    public void start() {
        GenerationConfiguration configuration = userInterface.requestConfiguration();
        start(configuration);
    }

    public void start(GenerationConfiguration configuration) {
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
