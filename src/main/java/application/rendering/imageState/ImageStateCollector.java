package application.rendering.imageState;

import application.execution.TaskBatch;
import application.execution.TaskRunner;
import application.execution.WorkRange;
import application.execution.WorkRangePartitioner;
import application.model.FractalImage;
import application.rendering.Layer;
import application.rendering.imageState.merge.LayerMerger;

import java.util.List;
import java.util.concurrent.Callable;

public final class ImageStateCollector {
    private final FractalImage canvas;
    private final LayerMerger layerMerger;
    private final LayerToImageMapper layerToImageMapper;
    private final WorkRangePartitioner workRangePartitioner;
    private final TaskRunner taskRunner;
    private Layer accumulatedLayer;

    public ImageStateCollector(FractalImage canvas, LayerMerger layerMerger, LayerToImageMapper layerToImageMapper, WorkRangePartitioner workRangePartitioner, TaskRunner taskRunner) {
        this.canvas = canvas;
        this.layerMerger = layerMerger;
        this.layerToImageMapper = layerToImageMapper;
        this.workRangePartitioner = workRangePartitioner;
        this.taskRunner = taskRunner;
    }

    public void collectLayer(Layer layer) {
        if (!layer.hasHits())return;

        if (accumulatedLayer == null) {
            accumulatedLayer = layer;
            return;
        }

        List<WorkRange> rowRanges = createRowRanges();
        List<Callable<Void>> mergingTasks = rowRanges.stream()
                .map(rowRange -> (Callable<Void>) () -> {
                    layerMerger.merge(accumulatedLayer, layer, rowRange);
                    return null;
                })
                .toList();

        completeTasks(mergingTasks);
    }

    public FractalImage getSnapshot() {
        if (accumulatedLayer == null)return canvas;

        List<Callable<Void>> mappingTasks = createRowRanges().stream()
                .map(rowRange -> (Callable<Void>) () -> {
                    layerToImageMapper.mapRows(accumulatedLayer, canvas, rowRange);
                    return null;
                })
                .toList();

        completeTasks(mappingTasks);

        return canvas;
    }

    private List<WorkRange> createRowRanges() {
        return workRangePartitioner.partition(canvas.height(), taskRunner.getThreadCount());
    }

    private void completeTasks(List<Callable<Void>> tasks) {
        try (TaskBatch<Void> taskBatch = taskRunner.execute(tasks)) {
            while (taskBatch.hasNextResult()) taskBatch.takeNextResult();
        }
    }
}
