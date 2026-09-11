package application.rendering.state.merge;

import application.rendering.Layer;

public interface LayerLineMerger {
    void mergeLines(Layer destinationLayer, Layer sourceLayer);
}
