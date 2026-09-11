package application.image.processing;

import application.image.processing.step.ImageProcessingStep;
import application.model.FractalImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class SequentialImagePostProcessor implements ImagePostProcessor {
    private final List<ImageProcessingStep> processingSteps;

    public SequentialImagePostProcessor(List<ImageProcessingStep> processingSteps) {
        this.processingSteps = List.copyOf(processingSteps);
    }

    @Override
    public FractalImage process(FractalImage rawImage) {
        for (ImageProcessingStep processingStep : processingSteps) processingStep.process(rawImage);
        return rawImage;
    }
}
