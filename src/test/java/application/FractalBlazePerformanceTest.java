package application;

import application.configuration.GenerationConfiguration;
import application.execution.ExecutionMode;
import application.execution.TaskRunner;
import application.image.encoding.ImageEncoder;
import application.image.encoding.ImageFile;
import application.image.processing.ImagePostProcessor;
import application.model.FractalImage;
import application.model.ImageSize;
import application.model.Point;
import application.model.Space;
import application.model.TransformationParameters;
import application.rendering.renderer.Renderer;
import application.rendering.transformation.Transformation;
import application.ui.console.ImageFileSaver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageWriter;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Disabled("Запускается вручную для сравнения производительности")
@SpringBootTest
class FractalBlazePerformanceTest {
    private record PerformanceResult(int threadCount, long executionTimeInMilliseconds) {}

    private final TaskRunner taskRunner;
    private final ImagePostProcessor imagePostProcessor;
    private final ImageEncoder imageEncoder;
    private final ImageFileSaver imageFileSaver;
    private final ImageWriter imageWriter;
    private final Renderer renderer;
    private final List<Transformation> transformations;
    private final PrintStream printer;

    @Autowired
    FractalBlazePerformanceTest(TaskRunner taskRunner, ImagePostProcessor imagePostProcessor, ImageEncoder imageEncoder, ImageFileSaver imageFileSaver, @Qualifier("PNG") ImageWriter imageWriter, Renderer renderer, List<Transformation> transformations, PrintStream printer) {
        this.taskRunner = taskRunner;
        this.imagePostProcessor = imagePostProcessor;
        this.imageEncoder = imageEncoder;
        this.imageFileSaver = imageFileSaver;
        this.imageWriter = imageWriter;
        this.renderer = renderer;
        this.transformations = transformations;
        this.printer = printer;
    }

    @Test
    void compareSingleThreadAndMultiThreadExecutionTime() {
        GenerationConfiguration configuration = createConfiguration();

        PerformanceResult singleThreadResult = measureExecutionTime(ExecutionMode.SINGLE_THREAD, configuration);
        PerformanceResult multiThreadResult = measureExecutionTime(ExecutionMode.MULTI_THREAD, configuration);

        printSystemConfiguration();
        printPerformanceResult("Однопоточный режим", singleThreadResult);
        printPerformanceResult("Многопоточный режим", multiThreadResult);
        printer.println(createComparisonMessage(singleThreadResult.executionTimeInMilliseconds(), multiThreadResult.executionTimeInMilliseconds()));
    }

    private GenerationConfiguration createConfiguration() {
        Path downloadsDirectory = Path.of(System.getProperty("user.home"), "Downloads");

        return new GenerationConfiguration(
                downloadsDirectory,
                new ImageSize(4320, 7680),
                imageWriter,
                renderer,
                new Space(new Point(0.0, 0.0), 2.0, 2.0),
                50_000_000,
                42,
                new TransformationParameters(1.0, Math.toRadians(20), -0.5, 0.5),
                List.copyOf(transformations));
    }

    private PerformanceResult measureExecutionTime(ExecutionMode executionMode, GenerationConfiguration configuration) {
        taskRunner.selectMode(executionMode);

        long startTime = System.nanoTime();
        executeFullGenerationCycle(configuration);
        long elapsedTime = System.nanoTime() - startTime;

        return new PerformanceResult(taskRunner.getThreadCount(), TimeUnit.NANOSECONDS.toMillis(elapsedTime));
    }

    private void executeFullGenerationCycle(GenerationConfiguration configuration) {
        FractalImage emptyCanvas = FractalImage.create(configuration.imageSize());
        FractalImage renderedImage = configuration.renderer().render(
                emptyCanvas,
                configuration.visibleSpace(),
                configuration.transformationParameters(),
                configuration.transformations(),
                configuration.iterationCount(),
                configuration.randomSeed());

        FractalImage correctedImage = imagePostProcessor.process(renderedImage);
        ImageFile imageFile = imageEncoder.encode(correctedImage, configuration.imageWriter());
        imageFileSaver.save(imageFile, configuration.outputPath());
    }

    private void printSystemConfiguration() {
        printer.println("Операционная система: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        printer.println("Java: " + System.getProperty("java.version"));
        printer.println("Доступных процессоров: " + Runtime.getRuntime().availableProcessors());
    }

    private void printPerformanceResult(String modeName, PerformanceResult result) {
        printer.println(modeName + ": " + result.executionTimeInMilliseconds() + " мс, потоков: " + result.threadCount());
    }

    private String createComparisonMessage(long singleThreadTime, long multiThreadTime) {
        if (multiThreadTime < singleThreadTime) {
            double acceleration = (double) singleThreadTime / multiThreadTime;
            return "Многопоточный режим быстрее в " + String.format("%.2f", acceleration) + " раза";
        }

        return "Многопоточный режим не показал ускорения";
    }
}
