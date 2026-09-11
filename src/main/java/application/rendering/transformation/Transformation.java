package application.rendering.transformation;

import application.model.Point;

import java.util.function.Function;

public interface Transformation extends Function<Point, Point> {
    String getName();
}

