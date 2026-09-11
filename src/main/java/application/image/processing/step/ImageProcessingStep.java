package application.image.processing.step;

import application.model.FractalImage;

@FunctionalInterface
public interface ImageProcessingStep {
    void process(FractalImage image);
}
