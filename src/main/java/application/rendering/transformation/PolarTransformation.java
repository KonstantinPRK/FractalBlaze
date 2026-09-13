package application.rendering.transformation;

import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public final class PolarTransformation implements Transformation {
    @Override
    public void apply(MutablePoint point) {
        double x = point.x();
        double y = point.y();
        double angle = Math.atan2(x, y);
        double radius = Math.sqrt(x * x + y * y);

        point.set(angle / Math.PI, radius - 1.0);
    }

    @Override
    public String getName() {
        return "POLAR — переводит координаты в полярную форму и создаёт круговые узоры";
    }
}
