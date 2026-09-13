package application.rendering.transformation;

import application.model.TransformationParameters;
import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public class PointTransformer {

    public void applyTransformation(
            MutablePoint point,
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

        point.set(shiftedX, shiftedY);
        transformation.apply(point);
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

}
