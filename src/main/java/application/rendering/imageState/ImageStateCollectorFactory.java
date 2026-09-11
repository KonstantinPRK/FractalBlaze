package application.rendering.imageState;

import application.execution.TaskRunner;
import application.execution.WorkRangePartitioner;
import application.model.FractalImage;
import application.rendering.imageState.merge.LayerMerger;
import org.springframework.stereotype.Component;

@Component
public final class ImageStateCollectorFactory {
    private final LayerMerger layerMerger;
    private final LayerToImageMapper layerToImageMapper;
    private final WorkRangePartitioner workRangePartitioner;
    private final TaskRunner taskRunner;

    public ImageStateCollectorFactory(LayerMerger layerMerger, LayerToImageMapper layerToImageMapper, WorkRangePartitioner workRangePartitioner, TaskRunner taskRunner) {
        this.layerMerger = layerMerger;
        this.layerToImageMapper = layerToImageMapper;
        this.workRangePartitioner = workRangePartitioner;
        this.taskRunner = taskRunner;
    }

    public ImageStateCollector create(FractalImage canvas) {
        return new ImageStateCollector(canvas, layerMerger, layerToImageMapper, workRangePartitioner, taskRunner);
    }
}
