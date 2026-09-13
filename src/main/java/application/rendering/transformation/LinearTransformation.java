package application.rendering.transformation;

import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public final class LinearTransformation implements Transformation {
    @Override
    public void apply(MutablePoint point) {
    }

    @Override
    public String getName() {
        return "LINEAR — не искривляет точку после масштаба, поворота и сдвига";
    }
}
