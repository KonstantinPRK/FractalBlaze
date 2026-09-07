package application.configuration.userConfiguration.userParameterRecords;

public record TransformationParameters(
        double scale,
        double rotationAngleInRadians,
        double shiftX,
        double shiftY) {}
