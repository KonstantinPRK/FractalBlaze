package application.rendering.layerMerger;

import application.picture.FractalImage;
import application.picture.Pixel;
import application.rendering.Layer;
import application.rendering.layerMerger.lineMerger.LayerLineMerger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("multiThreadLayerMerger")
public class MultiThreadLayerMerger implements LayerMerger {
    private final LayerLineMerger layerRowsMerger, layerColumnsMerger;
    private FractalImage canvas;
    private Layer mergedLayer;

    public MultiThreadLayerMerger(@Qualifier("multiThreadLayerRowsMerger") LayerLineMerger layerRowsMerger, @Qualifier("multiThreadLayerColumnsMerger") LayerLineMerger layerColumnsMerger) {
        this.layerRowsMerger = layerRowsMerger;
        this.layerColumnsMerger = layerColumnsMerger;
    }

    @Override
    public void startMerging(FractalImage emptyCanvas) {
        canvas = emptyCanvas;
        mergedLayer = null;
    }

    @Override
    public void merge(Layer layer) {
        if (mergedLayer == null) {
            mergedLayer = layer;
            return;
        }

        if (!layer.hasHits())return;

        LayerLineMerger selectedLineMerger = selectLineMerger(layer);
        selectedLineMerger.mergeLines(mergedLayer, layer);
        mergedLayer.markAsContainingHits();
    }

    @Override
    public FractalImage getMergingResult() {
        FractalImage mergingResult = createFractalImage();

        canvas = null;
        mergedLayer = null;

        return mergingResult;
    }

    private LayerLineMerger selectLineMerger(Layer layer) {
        return layer.width() >= layer.height() ? layerRowsMerger : layerColumnsMerger;
    }

    private FractalImage createFractalImage() {
        for (int pixelIndex = 0; pixelIndex < mergedLayer.pixelCount(); pixelIndex++) {
            int hitCount = mergedLayer.hitCount(pixelIndex);
            if (hitCount == 0)continue;

            int red = (int) (mergedLayer.redSum(pixelIndex) / hitCount);
            int green = (int) (mergedLayer.greenSum(pixelIndex) / hitCount);
            int blue = (int) (mergedLayer.blueSum(pixelIndex) / hitCount);

            canvas.data()[pixelIndex] = new Pixel(red, green, blue, hitCount);
        }

        return canvas;
    }
}
