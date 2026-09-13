package application.configuration.spring;

import application.configuration.setting.ColorSettings;
import application.configuration.setting.ConsoleLayoutSettings;
import application.configuration.setting.GammaCorrectionSettings;
import application.configuration.setting.GenerationSettings;
import application.configuration.setting.OutputFileSettings;
import application.configuration.setting.RenderResourceSettings;
import application.configuration.setting.TaskRunnerSettings;
import application.configuration.setting.TrajectorySettings;
import application.configuration.setting.TransformationCalculationSettings;
import application.model.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettingsConfig {
    @Bean
    public ConsoleLayoutSettings consoleLayoutSettings() {
        return new ConsoleLayoutSettings(2, "Пример: ", "Допустимый диапазон: ", "> ");
    }

    @Bean
    public GenerationSettings generationSettings() {
        return new GenerationSettings(new Point(0.0, 0.0), 2.0, 2.0, 1_000_000);
    }

    @Bean
    public TrajectorySettings trajectorySettings() {
        return new TrajectorySettings(20, 10_000);
    }

    @Bean
    public TaskRunnerSettings taskRunnerSettings(
            @Value("${fractal.execution.automatic-thread-count-percent}") int automaticThreadCountPercent,
            @Value("${fractal.execution.thread-count}") String configuredThreadCount)
    {
        if (automaticThreadCountPercent < 1 || automaticThreadCountPercent > 100) {
            throw new IllegalArgumentException(
                    "Процент процессоров для автоматического расчёта должен находиться в диапазоне от 1 до 100");
        }

        int availableProcessorCount = Runtime.getRuntime().availableProcessors();
        int automaticThreadCount = Math.max(
                1,
                (int) ((long) availableProcessorCount * automaticThreadCountPercent / 100));
        int threadCount = resolveThreadCount(configuredThreadCount, automaticThreadCount);

        return new TaskRunnerSettings(threadCount);
    }

    private int resolveThreadCount(String configuredThreadCount, int automaticThreadCount) {
        String normalizedThreadCount = configuredThreadCount.trim();
        if (normalizedThreadCount.equalsIgnoreCase("auto")) return automaticThreadCount;

        try {
            int explicitThreadCount = Integer.parseInt(normalizedThreadCount);
            if (explicitThreadCount < 1) {
                throw new IllegalArgumentException(
                        "Количество потоков должно быть значением auto или положительным целым числом");
            }

            return Math.min(explicitThreadCount, automaticThreadCount);

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Количество потоков должно быть значением auto или положительным целым числом",
                    exception);
        }
    }

    @Bean
    public RenderResourceSettings renderResourceSettings(
            @Value("${fractal.render.available-heap-percent}") int availableHeapPercent) {

        return new RenderResourceSettings(availableHeapPercent);
    }

    @Bean
    public ColorSettings colorSettings() {
        return new ColorSettings(0.85f, 1.0f);
    }

    @Bean
    public TransformationCalculationSettings transformationCalculationSettings() {
        return new TransformationCalculationSettings(1.0e-6);
    }

    @Bean
    public GammaCorrectionSettings gammaCorrectionSettings() {
        return new GammaCorrectionSettings(2.2);
    }

    @Bean
    public OutputFileSettings outputFileSettings() {
        return new OutputFileSettings("fractal");
    }
}
