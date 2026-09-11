package application.rendering.imageState.merge;

import application.rendering.Layer;

public interface LayerMerger {
    void mergeLine(Layer destinationLayer, Layer sourceLayer, int lineIndex);
}
