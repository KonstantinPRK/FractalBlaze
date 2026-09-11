package application.configuration.spring;

import application.configuration.setting.ColorSettings;
import application.configuration.setting.ConsoleLayoutSettings;
import application.configuration.setting.GammaCorrectionSettings;
import application.configuration.setting.GenerationSettings;
import application.configuration.setting.OutputFileSettings;
import application.configuration.setting.RenderingSettings;
import application.configuration.setting.SmoothingSettings;
import application.configuration.setting.TrajectorySettings;
import application.configuration.setting.TransformationCalculationSettings;
import application.model.Point;
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
    public RenderingSettings renderingSettings() {
        int availableProcessorCount = Runtime.getRuntime().availableProcessors();

        return new RenderingSettings(availableProcessorCount);
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
    public SmoothingSettings smoothingSettings() {
        return new SmoothingSettings(1);
    }

    @Bean
    public OutputFileSettings outputFileSettings() {
        return new OutputFileSettings("fractal");
    }
}
