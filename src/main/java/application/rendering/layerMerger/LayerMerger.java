package application.rendering.layerMerger;

import application.picture.FractalImage;
import application.rendering.Layer;

public interface LayerMerger {
    void startMerging(FractalImage emptyCanvas);

    void merge(Layer layer);

    FractalImage getMergingResult();
}
