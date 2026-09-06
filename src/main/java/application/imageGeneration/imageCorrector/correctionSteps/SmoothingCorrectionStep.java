package application.imageGeneration.imageCorrector.correctionSteps;

import application.picture.FractalImage;
import application.picture.Pixel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public final class SmoothingCorrectionStep implements ImageCorrectionStep {
    private static final int[][] GAUSSIAN_KERNEL = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
    };
    private static final int KERNEL_RADIUS = 1;

    @Override
    public void applyCorrection(FractalImage image) {
        Pixel[] sourcePixels = image.data().clone();

        for (int pixelY = 0; pixelY < image.height(); pixelY++) {
            for (int pixelX = 0; pixelX < image.width(); pixelX++) {
                Pixel smoothedPixel = calculateSmoothedPixel(pixelX, pixelY, image, sourcePixels);
                image.setPixel(pixelX, pixelY, smoothedPixel);
            }
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
