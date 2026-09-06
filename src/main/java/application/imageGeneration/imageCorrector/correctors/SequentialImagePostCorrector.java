package application.imageGeneration.imageCorrector.correctors;


import application.imageGeneration.imageCorrector.correctionSteps.ImageCorrectionStep;
import application.picture.FractalImage;

import java.util.List;

public class SequentialImagePostCorrector implements ImagePostCorrector {
    private final List<ImageCorrectionStep> correctorSteps;

    public SequentialImagePostCorrector(List<ImageCorrectionStep> correctorSteps) {
        this.correctorSteps = correctorSteps;
    }

    @Override
    public FractalImage applyCorrection(FractalImage rawImage) {
        return null;
    }
}
