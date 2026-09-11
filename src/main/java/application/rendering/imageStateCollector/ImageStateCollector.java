package application.rendering.imageStateCollector;

import application.picture.FractalImage;
import application.picture.Pixel;
import application.rendering.Layer;
import application.rendering.lineMerger.LayerLineMerger;

public final class ImageStateCollector {
    private final FractalImage canvas;
    private final LayerLineMerger layerLineMerger;
    private Layer accumulatedLayer;

    public ImageStateCollector(FractalImage canvas, LayerLineMerger layerLineMerger) {
        this.canvas = canvas;
        this.layerLineMerger = layerLineMerger;
    }

    public void collectLayer(Layer layer) {
        if (!layer.hasHits())return;

        if (accumulatedLayer == null) {
            accumulatedLayer = layer;
            return;
        }

        layerLineMerger.mergeLines(accumulatedLayer, layer);
    }

    public FractalImage getSnapshot() {
        if (accumulatedLayer == null)return canvas;

        for (int pixelIndex = 0; pixelIndex < accumulatedLayer.pixelCount(); pixelIndex++) {
            int hitCount = accumulatedLayer.hitCount(pixelIndex);
            if (hitCount == 0)continue;

            int red = (int) (accumulatedLayer.redSum(pixelIndex) / hitCount);
            int green = (int) (accumulatedLayer.greenSum(pixelIndex) / hitCount);
            int blue = (int) (accumulatedLayer.blueSum(pixelIndex) / hitCount);

            canvas.data()[pixelIndex] = new Pixel(red, green, blue, hitCount);
        }

        return canvas;
    }
}
