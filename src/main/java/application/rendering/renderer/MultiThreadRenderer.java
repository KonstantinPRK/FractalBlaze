package application.rendering.renderer;

import application.configuration.systemConfiguration.settingsRecords.RenderingSettings;
import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;
import application.picture.FractalImage;
import application.picture.Space;
import application.rendering.ColorPalette;
import application.rendering.Layer;
import application.rendering.RenderingContext;
import application.rendering.Stroker;
import application.rendering.layerMerger.LayerMerger;
import application.transformation.Transformation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Stream;

@Component
public class MultiThreadRenderer implements Renderer {
    private final int THREAD_COUNT;
    private final ColorPalette colorPalette;
    private final Stroker stroker;
    private final LayerMerger layerMerger;
    private final ExecutorService executor;

    public MultiThreadRenderer(RenderingSettings settings, ColorPalette colorPalette, Stroker stroker, @Qualifier("multiThreadLayerMerger") LayerMerger layerMerger, @Qualifier("renderingExecutor") ExecutorService executor) {
        THREAD_COUNT = settings.threadCount();
        this.colorPalette = colorPalette;
        this.stroker = stroker;
        this.layerMerger = layerMerger;
        this.executor = executor;
    }

    @Override
    public String getName() {
        return "MULTI_THREAD — параллельно рисует и объединяет слои";
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
        int taskCount = Math.min(THREAD_COUNT, trajectoryCount);

        CompletionService<Layer> completedLayers = new ExecutorCompletionService<>(executor);
        Set<Future<Layer>> pendingLayers = new HashSet<>();

        layerMerger.startMerging(canvas);

        try {
            for (int taskIndex = 0; taskIndex < taskCount; taskIndex++) {
                int firstTrajectory = (int) ((long) trajectoryCount * taskIndex / taskCount);
                int endTrajectory = (int) ((long) trajectoryCount * (taskIndex + 1) / taskCount);

                Future<Layer> pendingLayer = completedLayers.submit(() -> stroker.spray(context, firstTrajectory, endTrajectory));
                pendingLayers.add(pendingLayer);
            }

            Stream.generate(() -> receiveCompletedLayer(completedLayers, pendingLayers))
                    .limit(taskCount)
                    .forEach(layerMerger::merge);

            return layerMerger.getMergingResult();
        } finally {
            pendingLayers.forEach(pendingLayer -> pendingLayer.cancel(true));
        }
    }

    private Layer receiveCompletedLayer(CompletionService<Layer> completedLayers, Set<Future<Layer>> pendingLayers) {
        try {
            Future<Layer> completedLayer = completedLayers.take();
            pendingLayers.remove(completedLayer);

            return completedLayer.get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Генерация изображения прервана", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Не удалось нарисовать слой", exception.getCause());
        }
    }
}
