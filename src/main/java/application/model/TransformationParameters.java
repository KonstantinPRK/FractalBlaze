package application.model;

public record TransformationParameters(
        double scale,
        double rotationAngleInRadians,
        double shiftX,
        double shiftY) {}
