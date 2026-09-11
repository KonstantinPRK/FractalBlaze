package application.image.processing.step;

import application.configuration.setting.GammaCorrectionSettings;
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
@Order(3)
public final class GammaCorrectionStep implements ImageProcessingStep {
    private final GammaCorrectionSettings settings;
    private final TaskRunner taskRunner;
    private final WorkRangePartitioner workRangePartitioner;

    public GammaCorrectionStep(GammaCorrectionSettings settings, TaskRunner taskRunner, WorkRangePartitioner workRangePartitioner) {
        this.settings = settings;
        this.taskRunner = taskRunner;
        this.workRangePartitioner = workRangePartitioner;
    }

    @Override
    public void process(FractalImage image) {
        List<Callable<Void>> correctionTasks = createRowRanges(image).stream()
                .map(rowRange -> (Callable<Void>) () -> {
                    processRows(image, rowRange);
                    return null;
                })
                .toList();

        completeTasks(correctionTasks);
    }

    private void processRows(FractalImage image, WorkRange rowRange) {
        for (int pixelY = rowRange.firstIndex(); pixelY < rowRange.endIndex(); pixelY++) {
            for (int pixelX = 0; pixelX < image.width(); pixelX++) {
                Pixel currentPixel = image.pixel(pixelX, pixelY);

                if (isBlack(currentPixel))continue;

                image.setPixel(pixelX, pixelY, new Pixel(applyGamma(currentPixel.red()), applyGamma(currentPixel.green()), applyGamma(currentPixel.blue()), currentPixel.hitCount()));
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
