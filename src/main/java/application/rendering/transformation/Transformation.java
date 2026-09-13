package application.rendering.transformation;

import application.rendering.MutablePoint;

public interface Transformation {
    void apply(MutablePoint point);

    String getName();
}
