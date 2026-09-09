package application.rendering.layerMerger.lineMerger;

import application.rendering.Layer;
import org.springframework.stereotype.Component;

@Component("singleThreadLayerRowsMerger")
public class SingleThreadLayerRowsMerger implements LayerLineMerger {
    @Override
    public void mergeLines(Layer destinationLayer, Layer sourceLayer) {
        for (int rowIndex = 0; rowIndex < sourceLayer.height(); rowIndex++) {
            mergeRow(destinationLayer, sourceLayer, rowIndex);
        }
    }

    private void mergeRow(Layer destinationLayer, Layer sourceLayer, int rowIndex) {
        int firstPixelIndex = rowIndex * sourceLayer.width();
        int endPixelIndex = firstPixelIndex + sourceLayer.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) {
            mergePixel(destinationLayer, sourceLayer, pixelIndex);
        }
    }

    private void mergePixel(Layer destinationLayer, Layer sourceLayer, int pixelIndex) {
        int hitCount = sourceLayer.hitCount(pixelIndex);
        if (hitCount == 0)return;

        destinationLayer.addPixelData(pixelIndex, hitCount, sourceLayer.redSum(pixelIndex), sourceLayer.greenSum(pixelIndex), sourceLayer.blueSum(pixelIndex));
    }
}
