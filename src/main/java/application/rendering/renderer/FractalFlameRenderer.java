package application.rendering.renderer;

import application.execution.TaskBatch;
import application.execution.TaskRunner;
import application.execution.WorkRange;
import application.execution.WorkRangePartitioner;
import application.model.FractalImage;
import application.model.Space;
import application.model.TransformationParameters;
import application.rendering.ColorPalette;
import application.rendering.Layer;
import application.rendering.RenderingContext;
import application.rendering.imageState.ImageStateCollector;
import application.rendering.imageState.ImageStateCollectorFactory;
import application.rendering.trajectory.Stroker;
import application.rendering.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

@Component
public final class FractalFlameRenderer implements Renderer {
    private final ColorPalette colorPalette;
    private final Stroker stroker;
    private final ImageStateCollectorFactory imageStateCollectorFactory;
    private final WorkRangePartitioner workRangePartitioner;
    private final TaskRunner taskRunner;

    public FractalFlameRenderer(ColorPalette colorPalette, Stroker stroker, ImageStateCollectorFactory imageStateCollectorFactory, WorkRangePartitioner workRangePartitioner, TaskRunner taskRunner) {
        this.colorPalette = colorPalette;
        this.stroker = stroker;
        this.imageStateCollectorFactory = imageStateCollectorFactory;
        this.workRangePartitioner = workRangePartitioner;
        this.taskRunner = taskRunner;
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
        RenderingContext context = RenderingContext.create(canvas, space, transformationParameters, transformations, userIterationCount, randomSeed, colorPalette);
        return drawLayers(canvas, context);
    }

    private FractalImage drawLayers(FractalImage canvas, RenderingContext context) {
        int trajectoryCount = stroker.calculateTrajectoryCount(context);
        List<WorkRange> trajectoryRanges = workRangePartitioner.partition(trajectoryCount, taskRunner.getThreadCount());

        List<Callable<Layer>> drawingTasks = trajectoryRanges.stream()
                .map(trajectoryRange -> (Callable<Layer>) () -> stroker.spray(context, trajectoryRange.firstIndex(), trajectoryRange.endIndex()))
                .toList();

        ImageStateCollector imageStateCollector = imageStateCollectorFactory.create(canvas);

        try (TaskBatch<Layer> renderedLayers = taskRunner.execute(drawingTasks)) {
            while (renderedLayers.hasNextResult()) imageStateCollector.collectLayer(renderedLayers.takeNextResult());
        }

        return imageStateCollector.getSnapshot();
    }
}
