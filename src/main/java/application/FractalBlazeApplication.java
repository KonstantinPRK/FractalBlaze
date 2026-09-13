package application;

import application.configuration.GenerationConfiguration;
import application.execution.TaskRunner;
import application.image.processing.ImagePostProcessor;
import application.model.FractalImage;
import application.rendering.RenderResourcePlanner;
import application.rendering.renderer.Renderer;
import application.ui.console.ConsoleController;
import org.springframework.stereotype.Component;

@Component
public final class FractalBlazeApplication {
    private final TaskRunner taskRunner;
    private final RenderResourcePlanner renderResourcePlanner;
    private final ConsoleController userInterface;
    private final ImagePostProcessor imagePostProcessor;

    public FractalBlazeApplication(
            TaskRunner taskRunner,
            RenderResourcePlanner renderResourcePlanner,
            ConsoleController userInterface,
            ImagePostProcessor imagePostProcessor)
    {
        this.taskRunner = taskRunner;
        this.renderResourcePlanner = renderResourcePlanner;
        this.userInterface = userInterface;
        this.imagePostProcessor = imagePostProcessor;
    }

    public void start() {
        GenerationConfiguration configuration = userInterface.requestConfiguration();
        taskRunner.selectMode(configuration.executionMode());
        renderResourcePlanner.ensureGenerationCanStart(configuration.imageSize());

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

        userInterface.saveFractalImage(correctedImage, configuration.imageWriter(), configuration.outputPath());
    }
}
