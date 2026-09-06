package application.imageGeneration.imageCorrector.correctors;

import application.picture.FractalImage;
import org.springframework.stereotype.Component;

@Component
public interface ImagePostCorrector {
    FractalImage applyCorrection(FractalImage rawImage);
}
