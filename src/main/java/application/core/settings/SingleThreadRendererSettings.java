package application.core.settings;

public record SingleThreadRendererSettings(
        int burnIn,
        int iterationsPerTrajectory,
        float colorSaturation,
        float colorBrightness) {}
