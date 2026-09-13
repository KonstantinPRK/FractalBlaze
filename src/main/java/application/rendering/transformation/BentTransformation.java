package application.rendering.transformation;

import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public final class BentTransformation implements Transformation {
    @Override
    public void apply(MutablePoint point) {
        double transformedX = point.x() < 0.0 ? point.x() * 2.0 : point.x();
        double transformedY = point.y() < 0.0 ? point.y() / 2.0 : point.y();

        point.set(transformedX, transformedY);
    }

    @Override
    public String getName() {
        return "BENT — асимметрично растягивает левую и сжимает нижнюю часть";
    }
}
