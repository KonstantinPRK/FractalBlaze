package application.transformation;

import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class PolarTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double angle = Math.atan2(point.x(), point.y());
        double radius = Math.sqrt(
                point.x() * point.x() + point.y() * point.y()
        );

        return new Point(
                angle / Math.PI,
                radius - 1.0
        );
    }

    @Override
    public String getName() {
        return "POLAR";
    }
}
