package application.image.processing.step;

import application.execution.LineTaskExecutor;
import application.model.FractalImage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public final class LogDensityStep implements ImageProcessingStep {
    private final LineTaskExecutor lineTaskExecutor;

    public LogDensityStep(LineTaskExecutor lineTaskExecutor) {
        this.lineTaskExecutor = lineTaskExecutor;
    }

    @Override
    public void process(FractalImage image) {
        int maxHitCount = findMaximumHitCount(image);
        if (maxHitCount == 0) return;

        double maxDensity = Math.log1p(maxHitCount);
        lineTaskExecutor.executeLines(image.height(), lineIndex -> processLine(image, lineIndex, maxDensity));
    }

    private int findMaximumHitCount(FractalImage image) {
        return lineTaskExecutor.executeRanges(
                        image.height(),
                        (firstLine, endLine) -> findMaximumHitCount(image, firstLine, endLine))
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    private int findMaximumHitCount(FractalImage image, int firstLine, int endLine) {
        int maximumHitCount = 0;

        for (int lineIndex = firstLine; lineIndex < endLine; lineIndex++) {
            int firstPixelIndex = lineIndex * image.width();
            int endPixelIndex = firstPixelIndex + image.width();

            for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) {
                maximumHitCount = Math.max(maximumHitCount, image.hitCount(pixelIndex));
            }
        }

        return maximumHitCount;
    }

    private void processLine(FractalImage image, int lineIndex, double maxDensity) {
        int firstPixelIndex = lineIndex * image.width();
        int endPixelIndex = firstPixelIndex + image.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) {
            correctPixel(image, pixelIndex, maxDensity);
        }
    }

    private void correctPixel(FractalImage image, int pixelIndex, double maxDensity) {
        int hitCount = image.hitCount(pixelIndex);
        if (hitCount == 0) return;

        double relativeDensity = Math.log1p(hitCount) / maxDensity;

        if (hasNoColor(image, pixelIndex)) {
            int brightness = scaleColorComponent(255, relativeDensity);
            image.setColor(pixelIndex, brightness, brightness, brightness);
            return;
        }

        image.setColor(
                pixelIndex,
                scaleColorComponent(image.red(pixelIndex), relativeDensity),
                scaleColorComponent(image.green(pixelIndex), relativeDensity),
                scaleColorComponent(image.blue(pixelIndex), relativeDensity));
    }

    private boolean hasNoColor(FractalImage image, int pixelIndex) {
        return image.rgb(pixelIndex) == 0;
    }

    private int scaleColorComponent(int colorComponent, double relativeDensity) {
        return (int) Math.round(colorComponent * relativeDensity);
    }
}
