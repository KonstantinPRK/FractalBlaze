package application.imageGeneration.imageCorrector.correctors;

import application.picture.FractalImage;

public interface ImagePostCorrector {
    FractalImage applyCorrection(FractalImage rawImage);
}
