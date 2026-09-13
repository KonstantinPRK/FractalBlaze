package application.model;

public record Space(
        Point focusPoint,
        double visibleWorldWidth,
        double visibleWorldHeight) {


    public boolean contains(double pointX, double pointY) {
        double leftBorder = focusPoint.x() - visibleWorldWidth / 2;

        double rightBorder = focusPoint.x() + visibleWorldWidth / 2;

        double bottomBorder = focusPoint.y() - visibleWorldHeight / 2;

        double topBorder = focusPoint.y() + visibleWorldHeight / 2;

        return pointX >= leftBorder
                && pointX < rightBorder
                && pointY >= bottomBorder
                && pointY < topBorder;
    }
}
