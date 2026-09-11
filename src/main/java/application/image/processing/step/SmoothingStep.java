package application.image.processing.step;

import application.configuration.setting.SmoothingSettings;
import application.execution.TaskBatch;
import application.execution.TaskRunner;
import application.execution.WorkRange;
import application.execution.WorkRangePartitioner;
import application.model.FractalImage;
import application.model.Pixel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

@Component
@Order(2)
public final class SmoothingStep implements ImageProcessingStep {
    private static final int[][] GAUSSIAN_KERNEL = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
    };

    private final int kernelRadius;
    private final TaskRunner taskRunner;
    private final WorkRangePartitioner workRangePartitioner;

    public SmoothingStep(SmoothingSettings settings, TaskRunner taskRunner, WorkRangePartitioner workRangePartitioner) {
        kernelRadius = settings.kernelRadius();
        this.taskRunner = taskRunner;
        this.workRangePartitioner = workRangePartitioner;
    }

    @Override
    public void process(FractalImage image) {
        Pixel[] sourcePixels = image.data().clone();

        List<Callable<Void>> smoothingTasks = createRowRanges(image).stream()
                .map(rowRange -> (Callable<Void>) () -> {
                    processRows(image, sourcePixels, rowRange);
                    return null;
                })
                .toList();

        completeTasks(smoothingTasks);
    }

    private void processRows(FractalImage image, Pixel[] sourcePixels, WorkRange rowRange) {
        for (int pixelY = rowRange.firstIndex(); pixelY < rowRange.endIndex(); pixelY++) {
            for (int pixelX = 0; pixelX < image.width(); pixelX++) {
                Pixel smoothedPixel = calculateSmoothedPixel(pixelX, pixelY, image, sourcePixels);
                image.setPixel(pixelX, pixelY, smoothedPixel);
            }
        }
    }

    private List<WorkRange> createRowRanges(FractalImage image) {
        return workRangePartitioner.partition(image.height(), taskRunner.getThreadCount());
    }

    private void completeTasks(List<Callable<Void>> tasks) {
        try (TaskBatch<Void> taskBatch = taskRunner.execute(tasks)) {
            while (taskBatch.hasNextResult()) taskBatch.takeNextResult();
        }
    }

    private Pixel calculateSmoothedPixel(int centerPixelX, int centerPixelY, FractalImage image, Pixel[] sourcePixels) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int appliedWeightSum = 0;

        for (int verticalOffset = -kernelRadius; verticalOffset <= kernelRadius; verticalOffset++) {
            for (int horizontalOffset = -kernelRadius; horizontalOffset <= kernelRadius; horizontalOffset++) {
                int neighborPixelX = centerPixelX + horizontalOffset;
                int neighborPixelY = centerPixelY + verticalOffset;

                if (!image.contains(neighborPixelX, neighborPixelY))continue;

                int kernelWeight = GAUSSIAN_KERNEL[verticalOffset + kernelRadius][horizontalOffset + kernelRadius];
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
