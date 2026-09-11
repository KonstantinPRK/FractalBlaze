package application.model;

import application.model.ImageSize;

import java.util.Arrays;
import java.util.Objects;

public record FractalImage(Pixel[] data, ImageSize imageSize) {
    public FractalImage {
    }

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
        return pixelX >= 0
                && pixelX < width()
                && pixelY >= 0
                && pixelY < height();
    }

    public Pixel pixel(int pixelX, int pixelY) {
        return data[pixelIndex(pixelX, pixelY)];
    }

    public void addHit(int pixelX, int pixelY) {
        addHit(pixelX, pixelY, 0xFFFFFF);
    }

    public void addHit(int pixelX, int pixelY, int packedColor) {
        int pixelIndex = pixelIndex(pixelX, pixelY);
        Pixel currentPixel = data[pixelIndex];
        int currentHitCount = currentPixel.hitCount();
        int updatedHitCount = currentHitCount + 1;

        int incomingRed = packedColor >> 16 & 0xFF;
        int incomingGreen = packedColor >> 8 & 0xFF;
        int incomingBlue = packedColor & 0xFF;

        data[pixelIndex] = new Pixel(blendColorComponent(currentPixel.red(), currentHitCount, incomingRed, updatedHitCount), blendColorComponent(currentPixel.green(), currentHitCount, incomingGreen, updatedHitCount), blendColorComponent(currentPixel.blue(), currentHitCount, incomingBlue, updatedHitCount), updatedHitCount);
    }

    public void setPixel(int pixelX, int pixelY, Pixel pixel) {
        data[pixelIndex(pixelX, pixelY)] = Objects.requireNonNull(pixel);
    }

    private int pixelIndex(int pixelX, int pixelY) {
        if (!contains(pixelX, pixelY))throw new IndexOutOfBoundsException("Pixel outside image: " + pixelX + ", " + pixelY);

        return pixelY * width() + pixelX;
    }

    private int blendColorComponent(int currentColorComponent, int currentHitCount, int incomingColorComponent, int updatedHitCount) {
        long accumulatedColor = (long) currentColorComponent * currentHitCount + incomingColorComponent;

        return (int) (accumulatedColor / updatedHitCount);
    }
}
