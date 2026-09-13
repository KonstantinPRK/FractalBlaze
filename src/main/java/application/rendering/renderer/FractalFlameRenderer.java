package application.rendering.renderer;

import application.execution.TaskBatch;
import application.execution.TaskRunner;
import application.model.FractalImage;
import application.model.Space;
import application.model.TransformationParameters;
import application.rendering.ColorPalette;
import application.rendering.Layer;
import application.rendering.RenderResourcePlanner;
import application.rendering.RenderingContext;
import application.rendering.imageState.ImageStateCollector;
import application.rendering.imageState.ImageStateCollectorFactory;
import application.rendering.trajectory.Stroker;
import application.rendering.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class FractalFlameRenderer implements Renderer {
    private final ColorPalette colorPalette;
    private final Stroker stroker;
    private final ImageStateCollectorFactory imageStateCollectorFactory;
    private final TaskRunner taskRunner;
    private final RenderResourcePlanner renderResourcePlanner;

    public FractalFlameRenderer(ColorPalette colorPalette, Stroker stroker, ImageStateCollectorFactory imageStateCollectorFactory, TaskRunner taskRunner, RenderResourcePlanner renderResourcePlanner) {
        this.colorPalette = colorPalette;
        this.stroker = stroker;
        this.imageStateCollectorFactory = imageStateCollectorFactory;
        this.taskRunner = taskRunner;
        this.renderResourcePlanner = renderResourcePlanner;
    }

    @Override
    public String getName() {
        return "FRACTAL_FLAME — генерирует цветное фрактальное пламя методом Chaos Game";
    }

    @Override
    public FractalImage render(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int userIterationCount,
            long randomSeed)
    {
        RenderingContext context = RenderingContext.create(
                canvas.imageSize(),
                space,
                transformationParameters,
                transformations,
                userIterationCount,
                randomSeed,
                colorPalette);

        return drawImage(canvas, context);
    }

    private FractalImage drawImage(FractalImage canvas, RenderingContext context) {
        int trajectoryCount = stroker.calculateTrajectoryCount(context.iterationCount());
        int layerCount = renderResourcePlanner.calculateLayerCount(
                canvas.imageSize(),
                taskRunner.getThreadCount(),
                trajectoryCount);
        ImageStateCollector imageStateCollector = imageStateCollectorFactory.create(canvas);

        try (TaskBatch<Layer> renderedLayers = taskRunner.executeRanges(
                trajectoryCount,
                layerCount,
                (firstTrajectory, endTrajectory) -> stroker.spray(context, firstTrajectory, endTrajectory)))
        {
            while (renderedLayers.hasNextResult()) imageStateCollector.collectLayer(renderedLayers.takeNextResult());
        }

        return imageStateCollector.getSnapshot();
    }
}
