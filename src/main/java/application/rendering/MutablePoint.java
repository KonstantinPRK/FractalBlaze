package application.rendering;

public final class MutablePoint {
    private double x;
    private double y;

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean hasFiniteCoordinates() {
        return Double.isFinite(x) && Double.isFinite(y);
    }
}
