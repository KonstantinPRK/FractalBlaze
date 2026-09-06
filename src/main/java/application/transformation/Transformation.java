package application.transformation;

import application.picture.Point;

import java.util.function.Function;

public interface Transformation extends Function<Point, Point> {
    String getName();
}

