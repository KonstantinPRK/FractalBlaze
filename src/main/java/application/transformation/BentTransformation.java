package application.transformation;

import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class BentTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double transformedX = point.x() < 0.0 ? point.x() * 2.0 : point.x();
        double transformedY = point.y() < 0.0 ? point.y() / 2.0 : point.y();

        return new Point(transformedX, transformedY);
    }

    @Override
    public String getName() {
        return "BENT — асимметрично растягивает левую и сжимает нижнюю часть";
    }
}
