package application.transformation;

import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class SphericalTransformation implements Transformation {
    private static final double EPSILON = 1.0e-6;

    @Override
    public Point apply(Point point) {
        double squaredRadius = point.x() * point.x()
                + point.y() * point.y()
                + EPSILON;

        return new Point(
                point.x() / squaredRadius,
                point.y() / squaredRadius
        );
    }

    @Override
    public String getName() {
        return "SPHERICAL";
    }
}
