package application.rendering.transformation;

import application.configuration.setting.TransformationCalculationSettings;
import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public final class SphericalTransformation implements Transformation {
    private final TransformationCalculationSettings settings;

    public SphericalTransformation(TransformationCalculationSettings settings) {
        this.settings = settings;
    }

    @Override
    public void apply(MutablePoint point) {
        double x = point.x();
        double y = point.y();
        double squaredRadius = x * x + y * y + settings.epsilon();

        point.set(x / squaredRadius, y / squaredRadius);
    }

    @Override
    public String getName() {
        return "SPHERICAL — выворачивает пространство относительно его центра";
    }
}
