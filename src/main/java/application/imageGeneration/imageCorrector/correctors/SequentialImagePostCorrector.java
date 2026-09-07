package application.imageGeneration.imageCorrector.correctors;

import application.imageGeneration.imageCorrector.correctionSteps.ImageCorrectionStep;
import application.picture.FractalImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class SequentialImagePostCorrector implements ImagePostCorrector {
    private final List<ImageCorrectionStep> correctorSteps;

    public SequentialImagePostCorrector(List<ImageCorrectionStep> correctorSteps) {
        this.correctorSteps = List.copyOf(correctorSteps);
    }

    @Override
    public FractalImage applyCorrection(FractalImage rawImage) {
        for (ImageCorrectionStep correctorStep : correctorSteps) correctorStep.applyCorrection(rawImage);
        return rawImage;
    }
}
