package application.transformation;

import application.picture.Point;
import org.springframework.stereotype.Component;

@Component
public final class SwirlTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double squaredRadius = point.x() * point.x()
                + point.y() * point.y();
        double sine = Math.sin(squaredRadius);
        double cosine = Math.cos(squaredRadius);

        return new Point(
                point.x() * sine - point.y() * cosine,
                point.x() * cosine + point.y() * sine
        );
    }

    @Override
    public String getName() {
        return "SWIRL";
    }
}
