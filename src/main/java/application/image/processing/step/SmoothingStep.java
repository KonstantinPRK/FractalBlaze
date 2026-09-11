package application.image.processing.step;

import application.execution.LineTaskExecutor;
import application.model.FractalImage;
import application.model.Pixel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public final class SmoothingStep implements ImageProcessingStep {
    private static final int[][] GAUSSIAN_KERNEL = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
    };
    private static final int KERNEL_RADIUS = GAUSSIAN_KERNEL.length / 2;

    private final LineTaskExecutor lineTaskExecutor;

    public SmoothingStep(LineTaskExecutor lineTaskExecutor) {
        this.lineTaskExecutor = lineTaskExecutor;
    }

    @Override
    public void process(FractalImage image) {
        Pixel[] sourcePixels = image.data().clone();

        lineTaskExecutor.executeLines(image.height(), lineIndex -> processLine(image, sourcePixels, lineIndex));
    }

    private void processLine(FractalImage image, Pixel[] sourcePixels, int lineIndex) {
        for (int pixelX = 0; pixelX < image.width(); pixelX++) {
            Pixel smoothedPixel = calculateSmoothedPixel(pixelX, lineIndex, image, sourcePixels);
            image.setPixel(pixelX, lineIndex, smoothedPixel);
        }
    }

    private Pixel calculateSmoothedPixel(int centerPixelX, int centerPixelY, FractalImage image, Pixel[] sourcePixels) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int appliedWeightSum = 0;

        for (int verticalOffset = -KERNEL_RADIUS; verticalOffset <= KERNEL_RADIUS; verticalOffset++) {
            for (int horizontalOffset = -KERNEL_RADIUS; horizontalOffset <= KERNEL_RADIUS; horizontalOffset++) {
                int neighborPixelX = centerPixelX + horizontalOffset;
                int neighborPixelY = centerPixelY + verticalOffset;

                if (!image.contains(neighborPixelX, neighborPixelY))continue;

                int kernelWeight = GAUSSIAN_KERNEL[verticalOffset + KERNEL_RADIUS][horizontalOffset + KERNEL_RADIUS];
                Pixel neighborPixel = sourcePixels[neighborPixelY * image.width() + neighborPixelX];

                redSum += neighborPixel.red() * kernelWeight;
                greenSum += neighborPixel.green() * kernelWeight;
                blueSum += neighborPixel.blue() * kernelWeight;
                appliedWeightSum += kernelWeight;
            }
        }

        Pixel originalPixel = sourcePixels[centerPixelY * image.width() + centerPixelX];

        return new Pixel(divideAndRound(redSum, appliedWeightSum), divideAndRound(greenSum, appliedWeightSum), divideAndRound(blueSum, appliedWeightSum), originalPixel.hitCount());
    }

    private int divideAndRound(int colorSum, int weightSum) {
        return (int) Math.round((double) colorSum / weightSum);
    }
}
