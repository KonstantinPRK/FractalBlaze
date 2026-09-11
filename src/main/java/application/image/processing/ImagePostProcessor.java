package application.image.processing;

import application.model.FractalImage;

public interface ImagePostProcessor {
    FractalImage process(FractalImage rawImage);
}
