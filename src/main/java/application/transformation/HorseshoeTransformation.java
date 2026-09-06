package application.transformation;

import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class HorseshoeTransformation implements Transformation {
    private static final double EPSILON = 1.0e-6;

    @Override
    public Point apply(Point point) {
        double radius = Math.sqrt(
                point.x() * point.x() + point.y() * point.y()
        ) + EPSILON;

        return new Point(
                (point.x() - point.y()) * (point.x() + point.y()) / radius,
                2.0 * point.x() * point.y() / radius
        );
    }

    @Override
    public String getName() {
        return "HORSESHOE";
    }
}
