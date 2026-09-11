package application;

import application.configuration.GenerationConfiguration;
import application.execution.ExecutionMode;
import application.execution.TaskRunner;
import application.model.ImageSize;
import application.model.Point;
import application.model.Space;
import application.model.TransformationParameters;
import application.rendering.transformation.Transformation;
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

    private final FractalBlazeApplication application;
    private final TaskRunner taskRunner;
    private final ImageWriter imageWriter;
    private final List<Transformation> transformations;
    private final PrintStream printer;

    @Autowired
    FractalBlazePerformanceTest(FractalBlazeApplication application, TaskRunner taskRunner, @Qualifier("PNG") ImageWriter imageWriter, List<Transformation> transformations, PrintStream printer) {
        this.application = application;
        this.taskRunner = taskRunner;
        this.imageWriter = imageWriter;
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
                new ImageSize(1280, 1280),
                imageWriter,
                new Space(new Point(0.0, 0.0), 2.0, 2.0),
                10_000_000,
                42,
                new TransformationParameters(1.0, Math.toRadians(20), -0.5, 0.5),
                List.copyOf(transformations));
    }

    private PerformanceResult measureExecutionTime(ExecutionMode executionMode, GenerationConfiguration configuration) {
        taskRunner.selectMode(executionMode);

        long startTime = System.nanoTime();
        application.start(configuration);
        long elapsedTime = System.nanoTime() - startTime;

        return new PerformanceResult(taskRunner.getThreadCount(), TimeUnit.NANOSECONDS.toMillis(elapsedTime));
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
