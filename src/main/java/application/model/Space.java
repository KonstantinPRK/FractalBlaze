package application.model;

public record Space(
        Point focusPoint,
        double visibleWorldWidth,
        double visibleWorldHeight) {


    public boolean contains(Point point) {
        double leftBorder = focusPoint.x() - visibleWorldWidth / 2;

        double rightBorder = focusPoint.x() + visibleWorldWidth / 2;

        double bottomBorder = focusPoint.y() - visibleWorldHeight / 2;

        double topBorder = focusPoint.y() + visibleWorldHeight / 2;

        return point.x() >= leftBorder
                && point.x() < rightBorder
                && point.y() >= bottomBorder
                && point.y() < topBorder;
    }
}
