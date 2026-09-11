package application.rendering.imageState.merge;

import application.execution.WorkRange;
import application.rendering.Layer;
import org.springframework.stereotype.Component;

@Component
public final class LayerRowsMerger implements LayerMerger {
    @Override
    public void merge(Layer destinationLayer, Layer sourceLayer, WorkRange rowRange) {
        for (int rowIndex = rowRange.firstIndex(); rowIndex < rowRange.endIndex(); rowIndex++) mergeRow(destinationLayer, sourceLayer, rowIndex);
    }

    private void mergeRow(Layer destinationLayer, Layer sourceLayer, int rowIndex) {
        int firstPixelIndex = rowIndex * sourceLayer.width();
        int endPixelIndex = firstPixelIndex + sourceLayer.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) mergePixel(destinationLayer, sourceLayer, pixelIndex);
    }

    private void mergePixel(Layer destinationLayer, Layer sourceLayer, int pixelIndex) {
        int hitCount = sourceLayer.hitCount(pixelIndex);
        if (hitCount == 0)return;

        destinationLayer.addPixelData(pixelIndex, hitCount, sourceLayer.redSum(pixelIndex), sourceLayer.greenSum(pixelIndex), sourceLayer.blueSum(pixelIndex));
    }
}
