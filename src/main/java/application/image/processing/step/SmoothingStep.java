package application.image.processing.step;

import application.execution.LineTaskExecutor;
import application.model.FractalImage;
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
        int[] sourceRgb = image.copyRgb();

        lineTaskExecutor.executeLines(image.height(), lineIndex -> processLine(image, sourceRgb, lineIndex));
    }

    private void processLine(FractalImage image, int[] sourceRgb, int lineIndex) {
        for (int pixelX = 0; pixelX < image.width(); pixelX++) {
            int pixelIndex = lineIndex * image.width() + pixelX;
            image.setRgb(pixelIndex, calculateSmoothedRgb(pixelX, lineIndex, image, sourceRgb));
        }
    }

    private int calculateSmoothedRgb(
            int centerPixelX,
            int centerPixelY,
            FractalImage image,
            int[] sourceRgb)
    {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int appliedWeightSum = 0;

        for (int verticalOffset = -KERNEL_RADIUS; verticalOffset <= KERNEL_RADIUS; verticalOffset++) {
            for (int horizontalOffset = -KERNEL_RADIUS; horizontalOffset <= KERNEL_RADIUS; horizontalOffset++) {
                int neighborPixelX = centerPixelX + horizontalOffset;
                int neighborPixelY = centerPixelY + verticalOffset;

                if (!image.contains(neighborPixelX, neighborPixelY)) continue;

                int kernelWeight = GAUSSIAN_KERNEL[verticalOffset + KERNEL_RADIUS][horizontalOffset + KERNEL_RADIUS];
                int neighborRgb = sourceRgb[neighborPixelY * image.width() + neighborPixelX];

                redSum += FractalImage.redComponent(neighborRgb) * kernelWeight;
                greenSum += FractalImage.greenComponent(neighborRgb) * kernelWeight;
                blueSum += FractalImage.blueComponent(neighborRgb) * kernelWeight;
                appliedWeightSum += kernelWeight;
            }
        }

        return FractalImage.packRgb(
                divideAndRound(redSum, appliedWeightSum),
                divideAndRound(greenSum, appliedWeightSum),
                divideAndRound(blueSum, appliedWeightSum));
    }

    private int divideAndRound(int colorSum, int weightSum) {
        return (int) Math.round((double) colorSum / weightSum);
    }
}
