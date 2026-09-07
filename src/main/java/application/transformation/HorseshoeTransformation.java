package application.transformation;

import application.core.settings.TransformationCalculationSettings;
import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class HorseshoeTransformation implements Transformation {
    private final TransformationCalculationSettings settings;

    public HorseshoeTransformation(TransformationCalculationSettings settings) {
        this.settings = settings;
    }

    @Override
    public Point apply(Point point) {
        double radius = Math.sqrt(point.x() * point.x() + point.y() * point.y()) + settings.epsilon();

        return new Point((point.x() - point.y()) * (point.x() + point.y()) / radius, 2.0 * point.x() * point.y() / radius);
    }

    @Override
    public String getName() {
        return "HORSESHOE — изгибает точки в дуги, напоминающие подкову";
    }
}
