package application.imageProcessor;

import application.FractalImage;

@FunctionalInterface
public interface ImageProcessor {
    void process(FractalImage image);
}
