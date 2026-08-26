package application;

public record FractalImage(Pixel[] data, int width, int height) {
    public static FractalImage create(int width, int height) {
        return null;
    }

    boolean contains(int x, int y) {
        return false;
    }

    Pixel pixel(int x, int y) {
        return null;
    }

}
