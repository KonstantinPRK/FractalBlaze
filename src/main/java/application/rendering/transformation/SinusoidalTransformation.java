package application.rendering.transformation;

import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public final class SinusoidalTransformation implements Transformation {
    @Override
    public void apply(MutablePoint point) {
        point.set(Math.sin(point.x()), Math.sin(point.y()));
    }

    @Override
    public String getName() {
        return "SINUSOIDAL — преобразует координаты в плавные синусоидальные волны";
    }
}
