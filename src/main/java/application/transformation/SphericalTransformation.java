package application.transformation;

import application.core.settings.TransformationCalculationSettings;
import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class SphericalTransformation implements Transformation {
    private final TransformationCalculationSettings settings;

    public SphericalTransformation(TransformationCalculationSettings settings) {
        this.settings = settings;
    }

    @Override
    public Point apply(Point point) {
        double squaredRadius = point.x() * point.x() + point.y() * point.y() + settings.epsilon();

        return new Point(point.x() / squaredRadius, point.y() / squaredRadius);
    }

    @Override
    public String getName() {
        return "SPHERICAL — выворачивает пространство относительно его центра";
    }
}
