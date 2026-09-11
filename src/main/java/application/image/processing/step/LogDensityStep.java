package application.image.processing.step;

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
@Order(1)
public final class LogDensityStep implements ImageProcessingStep {
    private final TaskRunner taskRunner;
    private final WorkRangePartitioner workRangePartitioner;

    public LogDensityStep(TaskRunner taskRunner, WorkRangePartitioner workRangePartitioner) {
        this.taskRunner = taskRunner;
        this.workRangePartitioner = workRangePartitioner;
    }

    @Override
    public void process(FractalImage image) {
        List<WorkRange> rowRanges = workRangePartitioner.partition(image.height(), taskRunner.getThreadCount());
        int maxHitCount = findMaximumHitCount(image, rowRanges);
        if (maxHitCount == 0)return;

        double maxDensity = Math.log1p(maxHitCount);
        List<Callable<Void>> correctionTasks = rowRanges.stream()
                .map(rowRange -> (Callable<Void>) () -> {
                    processRows(image, rowRange, maxDensity);
                    return null;
                })
                .toList();

        completeTasks(correctionTasks);
    }

    private int findMaximumHitCount(FractalImage image, List<WorkRange> rowRanges) {
        List<Callable<Integer>> maximumSearchTasks = rowRanges.stream()
                .map(rowRange -> (Callable<Integer>) () -> findMaximumHitCount(image, rowRange))
                .toList();

        int maximumHitCount = 0;

        try (TaskBatch<Integer> searchResults = taskRunner.execute(maximumSearchTasks)) {
            while (searchResults.hasNextResult()) maximumHitCount = Math.max(maximumHitCount, searchResults.takeNextResult());
        }

        return maximumHitCount;
    }

    private int findMaximumHitCount(FractalImage image, WorkRange rowRange) {
        int maximumHitCount = 0;

        for (int pixelY = rowRange.firstIndex(); pixelY < rowRange.endIndex(); pixelY++) {
            int firstPixelIndex = pixelY * image.width();
            int endPixelIndex = firstPixelIndex + image.width();

            for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) maximumHitCount = Math.max(maximumHitCount, image.data()[pixelIndex].hitCount());
        }

        return maximumHitCount;
    }

    private void processRows(FractalImage image, WorkRange rowRange, double maxDensity) {
        for (int pixelY = rowRange.firstIndex(); pixelY < rowRange.endIndex(); pixelY++) {
            for (int pixelX = 0; pixelX < image.width(); pixelX++) {
                Pixel currentPixel = image.pixel(pixelX, pixelY);

                if (currentPixel.hitCount() == 0)continue;

                double relativeDensity = Math.log1p(currentPixel.hitCount()) / maxDensity;

                int correctedRed;
                int correctedGreen;
                int correctedBlue;

                if (hasNoColor(currentPixel)) {
                    int brightness = scaleColorComponent(255, relativeDensity);
                    correctedRed = brightness;
                    correctedGreen = brightness;
                    correctedBlue = brightness;
                } else {
                    correctedRed = scaleColorComponent(currentPixel.red(), relativeDensity);
                    correctedGreen = scaleColorComponent(currentPixel.green(), relativeDensity);
                    correctedBlue = scaleColorComponent(currentPixel.blue(), relativeDensity);
                }

                image.setPixel(pixelX, pixelY, new Pixel(correctedRed, correctedGreen, correctedBlue, currentPixel.hitCount()));
            }
        }
    }

    private void completeTasks(List<Callable<Void>> tasks) {
        try (TaskBatch<Void> taskBatch = taskRunner.execute(tasks)) {
            while (taskBatch.hasNextResult()) taskBatch.takeNextResult();
        }
    }

    private boolean hasNoColor(Pixel pixel) {
        return pixel.red() == 0 && pixel.green() == 0 && pixel.blue() == 0;
    }

    private int scaleColorComponent(int colorComponent, double relativeDensity) {
        return (int) Math.round(colorComponent * relativeDensity);
    }
}
