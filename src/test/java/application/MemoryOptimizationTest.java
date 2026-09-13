package application;

import application.configuration.setting.RenderResourceSettings;
import application.execution.ExecutionMode;
import application.execution.LineTaskExecutor;
import application.execution.TaskBatch;
import application.execution.TaskRunner;
import application.image.encoding.ImageEncoder;
import application.image.processing.step.SmoothingStep;
import application.model.FractalImage;
import application.model.ImageSize;
import application.rendering.RenderResourcePlanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryOptimizationTest {
    @Test
    void limitsRangeTasksWithoutLosingItems() {
        try (TaskRunner taskRunner = new TaskRunner(4)) {
            taskRunner.selectMode(ExecutionMode.MULTI_THREAD);

            try (TaskBatch<Integer> taskBatch = taskRunner.executeRanges(
                    100,
                    2,
                    (firstIndex, endIndex) -> endIndex - firstIndex))
            {
                int taskCount = 0;
                int processedItemCount = 0;

                while (taskBatch.hasNextResult()) {
                    taskCount++;
                    processedItemCount += taskBatch.takeNextResult();
                }

                assertEquals(2, taskCount);
                assertEquals(100, processedItemCount);
            }
        }
    }

    @Test
    void plansNoMoreLayersThanThreadsOrTrajectories() {
        RenderResourcePlanner planner = new RenderResourcePlanner(new RenderResourceSettings(0.5));

        assertEquals(3, planner.calculateLayerCount(new ImageSize(1, 1), 4, 3));
        assertThrows(
                IllegalStateException.class,
                () -> planner.calculateLayerCount(new ImageSize(100_000, 100_000), 4, 10));
    }

    @Test
    void smoothsPackedRgbWithoutChangingHitCounts() {
        FractalImage image = FractalImage.create(new ImageSize(3, 1));
        image.setPixel(0, 0, 0, 0, 1);
        image.setPixel(1, 255, 0, 0, 2);
        image.setPixel(2, 0, 0, 0, 3);

        try (TaskRunner taskRunner = new TaskRunner(2)) {
            new SmoothingStep(new LineTaskExecutor(taskRunner)).process(image);
        }

        assertEquals(85, image.red(0));
        assertEquals(128, image.red(1));
        assertEquals(85, image.red(2));
        assertEquals(1, image.hitCount(0));
        assertEquals(2, image.hitCount(1));
        assertEquals(3, image.hitCount(2));
    }

    @Test
    void writesPackedRgbDirectlyInEverySupportedFormat(@TempDir Path temporaryDirectory) throws IOException {
        FractalImage image = FractalImage.create(new ImageSize(2, 1));
        image.setPixel(0, 255, 0, 0, 1);
        image.setPixel(1, 0, 128, 255, 1);

        for (String formatName : List.of("JPEG", "BMP", "PNG")) {
            Path outputFile = Files.createFile(temporaryDirectory.resolve("image." + formatName.toLowerCase()));
            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(formatName).next();

            try {
                new ImageEncoder().encode(image, imageWriter, outputFile);
            } finally {
                imageWriter.dispose();
            }

            BufferedImage decodedImage = ImageIO.read(outputFile.toFile());
            assertTrue(Files.size(outputFile) > 0);
            assertNotNull(decodedImage);
            assertEquals(2, decodedImage.getWidth());
            assertEquals(1, decodedImage.getHeight());

            if (formatName.equals("PNG")) {
                assertEquals(0xFF0000, decodedImage.getRGB(0, 0) & 0xFFFFFF);
                assertEquals(0x0080FF, decodedImage.getRGB(1, 0) & 0xFFFFFF);
            }
        }
    }
}
