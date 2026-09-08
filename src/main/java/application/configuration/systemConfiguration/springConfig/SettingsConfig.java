package application.configuration.systemConfiguration.springConfig;

import application.configuration.systemConfiguration.settingsRecords.ColorSettings;
import application.configuration.systemConfiguration.settingsRecords.ConsoleLayoutSettings;
import application.configuration.systemConfiguration.settingsRecords.GammaCorrectionSettings;
import application.configuration.systemConfiguration.settingsRecords.GenerationSettings;
import application.configuration.systemConfiguration.settingsRecords.OutputFileSettings;
import application.configuration.systemConfiguration.settingsRecords.SmoothingSettings;
import application.configuration.systemConfiguration.settingsRecords.TrajectorySettings;
import application.configuration.systemConfiguration.settingsRecords.TransformationCalculationSettings;
import application.picture.Point;
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
