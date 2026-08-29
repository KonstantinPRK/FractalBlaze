package application;

import java.util.Arrays;

public record FractalImage(Pixel[] data, int width, int height) {

    public static FractalImage create(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image dimensions must be positive");
        }

        Pixel[] data = new Pixel[width * height];
        Arrays.fill(data, new Pixel(0, 0, 0, 0));
        return new FractalImage(data, width, height);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Pixel pixel(int x, int y) {
        return data[index(x, y)];
    }

    public void addHit(int x, int y) {
        int index = index(x, y);
        Pixel pixel = data[index];
        data[index] = new Pixel(pixel.r(), pixel.g(), pixel.b(), pixel.hitCount() + 1);
    }

    public void setPixel(int x, int y, Pixel pixel) {
        data[index(x, y)] = pixel;
    }

    private int index(int x, int y) {
        if (!contains(x, y)) {
            throw new IndexOutOfBoundsException("Pixel outside image: " + x + ", " + y);
        }
        return y * width + x;
    }
}
