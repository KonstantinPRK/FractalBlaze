package application.image.processing.step;

import application.configuration.setting.GammaCorrectionSettings;
import application.execution.LineTaskExecutor;
import application.model.FractalImage;
import application.model.Pixel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public final class GammaCorrectionStep implements ImageProcessingStep {
    private final GammaCorrectionSettings settings;
    private final LineTaskExecutor lineTaskExecutor;

    public GammaCorrectionStep(GammaCorrectionSettings settings, LineTaskExecutor lineTaskExecutor) {
        this.settings = settings;
        this.lineTaskExecutor = lineTaskExecutor;
    }

    @Override
    public void process(FractalImage image) {
        lineTaskExecutor.executeLines(image.height(), lineIndex -> processLine(image, lineIndex));
    }

    private void processLine(FractalImage image, int lineIndex) {
        int firstPixelIndex = lineIndex * image.width();
        int endPixelIndex = firstPixelIndex + image.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) {
            Pixel currentPixel = image.data()[pixelIndex];

            if (isBlack(currentPixel))continue;

            image.data()[pixelIndex] = new Pixel(applyGamma(currentPixel.red()), applyGamma(currentPixel.green()), applyGamma(currentPixel.blue()), currentPixel.hitCount());
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
