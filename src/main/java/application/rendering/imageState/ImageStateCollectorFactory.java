package application.rendering.imageState;

import application.execution.LineTaskExecutor;
import application.model.FractalImage;
import application.rendering.imageState.merge.LayerMerger;
import org.springframework.stereotype.Component;

@Component
public final class ImageStateCollectorFactory {
    private final LayerMerger layerMerger;
    private final LayerToImageMapper layerToImageMapper;
    private final LineTaskExecutor lineTaskExecutor;

    public ImageStateCollectorFactory(LayerMerger layerMerger, LayerToImageMapper layerToImageMapper, LineTaskExecutor lineTaskExecutor) {
        this.layerMerger = layerMerger;
        this.layerToImageMapper = layerToImageMapper;
        this.lineTaskExecutor = lineTaskExecutor;
    }

    public ImageStateCollector create(FractalImage canvas) {
        return new ImageStateCollector(canvas, layerMerger, layerToImageMapper, lineTaskExecutor);
    }
}
