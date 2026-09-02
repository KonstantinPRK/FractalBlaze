package application.world;

import java.util.Objects;

public record Magnifier(
        Point focusPoint,
        double visibleWorldWidth,
        double visibleWorldHeight
) {
    public Magnifier {
        Objects.requireNonNull(focusPoint, "Focus point must not be null");

        if (visibleWorldWidth <= 0 || visibleWorldHeight <= 0) {
            throw new IllegalArgumentException(
                    "Visible world dimensions must be positive"
            );
        }
    }

    public boolean contains(Point point) {
        double leftBorder =
                focusPoint.x() - visibleWorldWidth / 2;

        double rightBorder =
                focusPoint.x() + visibleWorldWidth / 2;

        double bottomBorder =
                focusPoint.y() - visibleWorldHeight / 2;

        double topBorder =
                focusPoint.y() + visibleWorldHeight / 2;

        return point.x() >= leftBorder
                && point.x() < rightBorder
                && point.y() >= bottomBorder
                && point.y() < topBorder;
    }
}
