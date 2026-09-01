package application.world;

import application.parameters.ImageSize;

import java.util.Arrays;
import java.util.Objects;

public record FractalImage(Pixel[] data, ImageSize imageSize) {
    public FractalImage {
        Objects.requireNonNull(data, "data must not be null");
        Objects.requireNonNull(imageSize, "imageSize must not be null");

        if (data.length != imageSize.width() * imageSize.height()) {
            throw new IllegalArgumentException("Pixel array size does not match image size");
        }
    }

    public static FractalImage create(ImageSize imageSize) {
        Objects.requireNonNull(imageSize, "imageSize must not be null");

        Pixel[] pixels = new Pixel[imageSize.width() * imageSize.height()];
        Arrays.fill(pixels, new Pixel(0, 0, 0, 0));
        return new FractalImage(pixels, imageSize);
    }

    public int width() {
        return imageSize.width();
    }

    public int height() {
        return imageSize.height();
    }

    public boolean contains(int pixelX, int pixelY) {
        return pixelX >= 0 && pixelX < width()
                && pixelY >= 0 && pixelY < height();
    }

    public Pixel pixel(int pixelX, int pixelY) {
        return data[pixelIndex(pixelX, pixelY)];
    }

    public void addHit(int pixelX, int pixelY) {
        int pixelIndex = pixelIndex(pixelX, pixelY);
        Pixel currentPixel = data[pixelIndex];
        data[pixelIndex] = new Pixel(
                currentPixel.red(),
                currentPixel.green(),
                currentPixel.blue(),
                currentPixel.hitCount() + 1
        );
    }

    public void setPixel(int pixelX, int pixelY, Pixel pixel) {
        data[pixelIndex(pixelX, pixelY)] = Objects.requireNonNull(pixel);
    }

    private int pixelIndex(int pixelX, int pixelY) {
        if (!contains(pixelX, pixelY)) {
            throw new IndexOutOfBoundsException(
                    "Pixel outside image: " + pixelX + ", " + pixelY
            );
        }
        return pixelY * width() + pixelX;
    }
}
