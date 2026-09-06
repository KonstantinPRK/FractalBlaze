package application.imageGeneration.imageCorrector;

import application.picture.FractalImage;

@FunctionalInterface
public interface ImageCorrector {
    void process(FractalImage image);
}
