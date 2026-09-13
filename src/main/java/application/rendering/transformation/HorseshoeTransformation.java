package application.rendering.transformation;

import application.configuration.setting.TransformationCalculationSettings;
import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public final class HorseshoeTransformation implements Transformation {
    private final TransformationCalculationSettings settings;

    public HorseshoeTransformation(TransformationCalculationSettings settings) {
        this.settings = settings;
    }

    @Override
    public void apply(MutablePoint point) {
        double x = point.x();
        double y = point.y();
        double radius = Math.sqrt(x * x + y * y) + settings.epsilon();

        point.set((x - y) * (x + y) / radius, 2.0 * x * y / radius);
    }

    @Override
    public String getName() {
        return "HORSESHOE — изгибает точки в дуги, напоминающие подкову";
    }
}
