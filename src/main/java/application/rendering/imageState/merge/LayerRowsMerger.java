package application.rendering.imageState.merge;

import application.rendering.Layer;
import org.springframework.stereotype.Component;

@Component
public final class LayerRowsMerger implements LayerMerger {
    @Override
    public void mergeLine(Layer destinationLayer, Layer sourceLayer, int lineIndex) {
        int firstPixelIndex = lineIndex * sourceLayer.width();
        int endPixelIndex = firstPixelIndex + sourceLayer.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) mergePixel(destinationLayer, sourceLayer, pixelIndex);
    }

    private void mergePixel(Layer destinationLayer, Layer sourceLayer, int pixelIndex) {
        int hitCount = sourceLayer.hitCount(pixelIndex);
        if (hitCount == 0)return;

        destinationLayer.addPixelData(pixelIndex, hitCount, sourceLayer.redSum(pixelIndex), sourceLayer.greenSum(pixelIndex), sourceLayer.blueSum(pixelIndex));
    }
}
