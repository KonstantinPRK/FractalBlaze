package application.userConfiguration.parameters;

public record TransformationParameters(
        double scale,
        double rotationAngleInRadians,
        double shiftX,
        double shiftY) {}
