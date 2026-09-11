package application.rendering.lineMerger;

import application.rendering.Layer;
import org.springframework.stereotype.Component;

@Component("singleThreadLayerColumnsMerger")
public class SingleThreadLayerColumnsMerger implements LayerLineMerger {
    @Override
    public void mergeLines(Layer destinationLayer, Layer sourceLayer) {
        for (int columnIndex = 0; columnIndex < sourceLayer.width(); columnIndex++) {
            mergeColumn(destinationLayer, sourceLayer, columnIndex);
        }
    }

    private void mergeColumn(Layer destinationLayer, Layer sourceLayer, int columnIndex) {
        for (int rowIndex = 0; rowIndex < sourceLayer.height(); rowIndex++) {
            int pixelIndex = rowIndex * sourceLayer.width() + columnIndex;
            mergePixel(destinationLayer, sourceLayer, pixelIndex);
        }
    }

    private void mergePixel(Layer destinationLayer, Layer sourceLayer, int pixelIndex) {
        int hitCount = sourceLayer.hitCount(pixelIndex);
        if (hitCount == 0)return;

        destinationLayer.addPixelData(pixelIndex, hitCount, sourceLayer.redSum(pixelIndex), sourceLayer.greenSum(pixelIndex), sourceLayer.blueSum(pixelIndex));
    }
}
