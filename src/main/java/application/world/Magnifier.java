package application.world;

public record Magnifier(double x, double y, double width, double height) {
    public Magnifier {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Rectangle dimensions must be positive");
        }
    }

    public boolean contains(Point point) {
        return point.x() >= x
                && point.x() < x + width
                && point.y() >= y
                && point.y() < y + height;
    }
}
