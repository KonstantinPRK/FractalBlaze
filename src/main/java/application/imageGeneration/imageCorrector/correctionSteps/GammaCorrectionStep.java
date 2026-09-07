package application.imageGeneration.imageCorrector.correctionSteps;

import application.core.settings.GammaCorrectionSettings;
import application.picture.FractalImage;
import application.picture.Pixel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public final class GammaCorrectionStep implements ImageCorrectionStep {
    private final GammaCorrectionSettings settings;

    public GammaCorrectionStep(GammaCorrectionSettings settings) {
        this.settings = settings;
    }

    @Override
    public void applyCorrection(FractalImage image) {
        for (int pixelY = 0; pixelY < image.height(); pixelY++) {
            for (int pixelX = 0; pixelX < image.width(); pixelX++) {
                Pixel currentPixel = image.pixel(pixelX, pixelY);

                if (isBlack(currentPixel))continue;

                image.setPixel(pixelX, pixelY, new Pixel(applyGamma(currentPixel.red()), applyGamma(currentPixel.green()), applyGamma(currentPixel.blue()), currentPixel.hitCount()));
            }
        }
    }

    private boolean isBlack(Pixel pixel) {
        return pixel.red() == 0 && pixel.green() == 0 && pixel.blue() == 0;
    }

    private int applyGamma(int colorComponent) {
        double normalizedColor = colorComponent / 255.0;
        double correctedColor = Math.pow(normalizedColor, 1.0 / settings.gamma());
        return clampColorComponent((int) Math.round(correctedColor * 255.0));
    }

    private int clampColorComponent(int colorComponent) {
        return Math.max(0, Math.min(255, colorComponent));
    }
}
