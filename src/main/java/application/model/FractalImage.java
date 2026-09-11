package application.model;

import application.model.ImageSize;

import java.util.Arrays;
import java.util.Objects;

public record FractalImage(Pixel[] data, ImageSize imageSize) {
    public static FractalImage create(ImageSize imageSize) {
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
        return     pixelX >= 0
                && pixelX < width()
                && pixelY >= 0
                && pixelY < height();
    }

    public Pixel pixel(int pixelX, int pixelY) {
        return data[pixelIndex(pixelX, pixelY)];
    }

    public void setPixel(int pixelX, int pixelY, Pixel pixel) {
        data[pixelIndex(pixelX, pixelY)] = Objects.requireNonNull(pixel, "Пиксель не должен быть null");
    }

    private int pixelIndex(int pixelX, int pixelY) {
        if (!contains(pixelX, pixelY))throw new IndexOutOfBoundsException("Пиксель находится за пределами изображения: " + pixelX + ", " + pixelY);

        return pixelY * width() + pixelX;
    }
}
