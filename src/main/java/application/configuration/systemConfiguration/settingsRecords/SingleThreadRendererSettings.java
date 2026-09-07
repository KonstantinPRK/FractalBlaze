package application.configuration.systemConfiguration.settingsRecords;

public record SingleThreadRendererSettings(
        int burnIn,
        int iterationsPerTrajectory,
        float colorSaturation,
        float colorBrightness) {}
