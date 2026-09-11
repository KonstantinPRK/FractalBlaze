package application.rendering.imageState.merge;

import application.execution.WorkRange;
import application.rendering.Layer;

public interface LayerMerger {
    void merge(Layer destinationLayer, Layer sourceLayer, WorkRange rowRange);
}
