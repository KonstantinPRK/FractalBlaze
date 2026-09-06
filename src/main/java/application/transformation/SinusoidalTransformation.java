package application.transformation;

import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class SinusoidalTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        return new Point(
                Math.sin(point.x()),
                Math.sin(point.y())
        );
    }

    @Override
    public String getName() {
        return "SINUSOIDAL";
    }
}
