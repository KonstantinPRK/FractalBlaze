package application.image.processing.step;

import application.configuration.setting.GammaCorrectionSettings;
import application.execution.LineTaskExecutor;
import application.model.FractalImage;
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
            if (image.rgb(pixelIndex) == 0) continue;

            image.setColor(
                    pixelIndex,
                    applyGamma(image.red(pixelIndex)),
                    applyGamma(image.green(pixelIndex)),
                    applyGamma(image.blue(pixelIndex)));
        }
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
