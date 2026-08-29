package application.transformation;

import application.Point;

import java.util.Objects;

public final class LinearTransformation implements Transformation {
    private final double scale;
    private final double shiftX;
    private final double shiftY;

    public LinearTransformation(double scale, double shiftX, double shiftY) {
        this.scale = scale;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    @Override
    public Point apply(Point point) {
        Objects.requireNonNull(point, "point");
        return new Point(
                point.x() * scale + shiftX,
                point.y() * scale + shiftY
        );
    }
}
