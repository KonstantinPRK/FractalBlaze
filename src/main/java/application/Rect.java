package application;

public record Rect(double x, double y, double width, double height) {
    boolean contains(Point p) {
        return false;
    }
}
