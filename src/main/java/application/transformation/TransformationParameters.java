package application.transformation;

public record TransformationParameters(
        double scale,
        double rotationAngleInRadians,
        double shiftX,
        double shiftY
) {}
