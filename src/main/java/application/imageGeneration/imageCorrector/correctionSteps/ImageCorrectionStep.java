package application.imageGeneration.imageCorrector.correctionSteps;

import application.picture.FractalImage;

@FunctionalInterface
public interface ImageCorrectionStep {
    void applyCorrection(FractalImage image);
}
