package application.image.processing.step;

import application.execution.LineTaskExecutor;
import application.execution.WorkRange;
import application.model.FractalImage;
import application.model.Pixel;
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
        if (maxHitCount == 0)return;

        double maxDensity = Math.log1p(maxHitCount);
        lineTaskExecutor.executeLines(image.height(), lineIndex -> processLine(image, lineIndex, maxDensity));
    }

    private int findMaximumHitCount(FractalImage image) {
        return lineTaskExecutor.executeRanges(image.height(), lineRange -> findMaximumHitCount(image, lineRange))
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    private int findMaximumHitCount(FractalImage image, WorkRange lineRange) {
        int maximumHitCount = 0;

        for (int lineIndex = lineRange.firstIndex(); lineIndex < lineRange.endIndex(); lineIndex++) {
            int firstPixelIndex = lineIndex * image.width();
            int endPixelIndex = firstPixelIndex + image.width();

            for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) maximumHitCount = Math.max(maximumHitCount, image.data()[pixelIndex].hitCount());
        }

        return maximumHitCount;
    }

    private void processLine(FractalImage image, int lineIndex, double maxDensity) {
        int firstPixelIndex = lineIndex * image.width();
        int endPixelIndex = firstPixelIndex + image.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) {
            Pixel currentPixel = image.data()[pixelIndex];
            image.data()[pixelIndex] = correctPixel(currentPixel, maxDensity);
        }
    }

    private Pixel correctPixel(Pixel pixel, double maxDensity) {
        if (pixel.hitCount() == 0)return pixel;

        double relativeDensity = Math.log1p(pixel.hitCount()) / maxDensity;

        if (hasNoColor(pixel)) {
            int brightness = scaleColorComponent(255, relativeDensity);
            return new Pixel(brightness, brightness, brightness, pixel.hitCount());
        }

        return new Pixel(scaleColorComponent(pixel.red(), relativeDensity), scaleColorComponent(pixel.green(), relativeDensity), scaleColorComponent(pixel.blue(), relativeDensity), pixel.hitCount());
    }

    private boolean hasNoColor(Pixel pixel) {
        return pixel.red() == 0 && pixel.green() == 0 && pixel.blue() == 0;
    }

    private int scaleColorComponent(int colorComponent, double relativeDensity) {
        return (int) Math.round(colorComponent * relativeDensity);
    }
}
