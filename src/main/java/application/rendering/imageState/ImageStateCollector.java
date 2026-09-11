package application.rendering.imageState;

import application.execution.LineTaskExecutor;
import application.model.FractalImage;
import application.rendering.Layer;
import application.rendering.imageState.merge.LayerMerger;

public final class ImageStateCollector {
    private final FractalImage canvas;
    private final LayerMerger layerMerger;
    private final LayerToImageMapper layerToImageMapper;
    private final LineTaskExecutor lineTaskExecutor;
    private Layer accumulatedLayer;

    public ImageStateCollector(FractalImage canvas, LayerMerger layerMerger, LayerToImageMapper layerToImageMapper, LineTaskExecutor lineTaskExecutor) {
        this.canvas = canvas;
        this.layerMerger = layerMerger;
        this.layerToImageMapper = layerToImageMapper;
        this.lineTaskExecutor = lineTaskExecutor;
    }

    public void collectLayer(Layer layer) {
        if (!layer.hasHits())return;

        if (accumulatedLayer == null) {
            accumulatedLayer = layer;
            return;
        }

        lineTaskExecutor.executeLines(canvas.height(), lineIndex -> layerMerger.mergeLine(accumulatedLayer, layer, lineIndex));
    }

    public FractalImage getSnapshot() {
        if (accumulatedLayer == null)return canvas;

        lineTaskExecutor.executeLines(canvas.height(), lineIndex -> layerToImageMapper.mapLine(accumulatedLayer, canvas, lineIndex));

        return canvas;
    }
}
