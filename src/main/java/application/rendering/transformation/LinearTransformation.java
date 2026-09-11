package application.rendering.transformation;

import application.model.Point;
import org.springframework.stereotype.Component;

@Component
public final class LinearTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        return point;
    }

    @Override
    public String getName() {
        return "LINEAR — не искривляет точку после масштаба, поворота и сдвига";
    }
}
