package application.rendering.lineMerger;

import application.rendering.Layer;

public interface LayerLineMerger {
    void mergeLines(Layer destinationLayer, Layer sourceLayer);
}
