package application.rendering.transformation;

import application.model.TransformationParameters;
import application.model.Point;
import org.springframework.stereotype.Component;

@Component
public class PointTransformer {

    public Point applyTransformation(
            Point point,
            Transformation transformation,
            TransformationParameters parameters,
            double cosOfRotation,
            double sinOfRotation)
    {
        double scaledX = scaleCoordinate(point.x(), parameters.scale());
        double scaledY = scaleCoordinate(point.y(), parameters.scale());

        double rotatedX = calculateRotatedX(scaledX, scaledY, cosOfRotation, sinOfRotation);
        double rotatedY = calculateRotatedY(scaledX, scaledY, cosOfRotation, sinOfRotation);

        double shiftedX = shiftCoordinate(rotatedX, parameters.shiftX());
        double shiftedY = shiftCoordinate(rotatedY, parameters.shiftY());

        Point transformedPoint = applyVariation(shiftedX, shiftedY, transformation);
        return transformedPoint;
    }

    private double scaleCoordinate(double coordinate, double scale) {
        return coordinate * scale;
    }

    private double calculateRotatedX(double scaledX, double scaledY, double cosOfRotation, double sinOfRotation) {
        return scaledX * cosOfRotation - scaledY * sinOfRotation;
    }

    private double calculateRotatedY(double scaledX, double scaledY, double cosOfRotation, double sinOfRotation) {
        return scaledX * sinOfRotation + scaledY * cosOfRotation;
    }

    private double shiftCoordinate(double coordinate, double shift) {
        return coordinate + shift;
    }

    private Point applyVariation(double coordinateX, double coordinateY, Transformation transformation) {
        return transformation.apply(new Point(coordinateX, coordinateY));
    }
}
