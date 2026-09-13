package application.rendering.transformation;

import application.rendering.MutablePoint;
import org.springframework.stereotype.Component;

@Component
public final class SwirlTransformation implements Transformation {

    @Override
    public void apply(MutablePoint point) {
        double x = point.x();
        double y = point.y();
        double squaredRadius = x * x + y * y;
        double sine = Math.sin(squaredRadius);
        double cosine = Math.cos(squaredRadius);

        point.set(x * sine - y * cosine, x * cosine + y * sine);
    }


    @Override
    public String getName() {
        return "SWIRL — закручивает точки вокруг центра в вихрь";
    }
}
