package application.model;

import java.util.Objects;

public final class FractalImage {
    private static final int COLOR_COMPONENT_MASK = 0xFF;
    private static final long BYTES_PER_PIXEL = 2L * Integer.BYTES;

    private final int[] rgbData;
    private final int[] hitCounts;
    private final ImageSize imageSize;

    private FractalImage(int[] rgbData, int[] hitCounts, ImageSize imageSize) {
        this.rgbData = rgbData;
        this.hitCounts = hitCounts;
        this.imageSize = imageSize;
    }

    public static FractalImage create(ImageSize imageSize) {
        Objects.requireNonNull(imageSize, "Размер изображения не должен быть null");
        int pixelCount = Math.toIntExact(imageSize.pixelCount());

        return new FractalImage(new int[pixelCount], new int[pixelCount], imageSize);
    }

    public static long estimateMemoryUsage(ImageSize imageSize) {
        return Math.multiplyExact(imageSize.pixelCount(), BYTES_PER_PIXEL);
    }

    public static long estimateRgbBufferMemoryUsage(ImageSize imageSize) {
        return Math.multiplyExact(imageSize.pixelCount(), Integer.BYTES);
    }

    public ImageSize imageSize() {
        return imageSize;
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

    public int rgb(int pixelIndex) {
        return rgbData[pixelIndex];
    }

    public int red(int pixelIndex) {
        return redComponent(rgbData[pixelIndex]);
    }

    public int green(int pixelIndex) {
        return greenComponent(rgbData[pixelIndex]);
    }

    public int blue(int pixelIndex) {
        return blueComponent(rgbData[pixelIndex]);
    }

    public int hitCount(int pixelIndex) {
        return hitCounts[pixelIndex];
    }

    public void setPixel(int pixelIndex, int red, int green, int blue, int hitCount) {
        rgbData[pixelIndex] = packRgb(red, green, blue);
        hitCounts[pixelIndex] = hitCount;
    }

    public void setColor(int pixelIndex, int red, int green, int blue) {
        rgbData[pixelIndex] = packRgb(red, green, blue);
    }

    public void setRgb(int pixelIndex, int rgb) {
        rgbData[pixelIndex] = rgb;
    }

    public int[] copyRgb() {
        return rgbData.clone();
    }

    public void copyRgbTo(int[] destination) {
        Objects.requireNonNull(destination, "Массив назначения не должен быть null");
        if (destination.length < rgbData.length) {
            throw new IllegalArgumentException("Массив назначения меньше изображения");
        }

        System.arraycopy(rgbData, 0, destination, 0, rgbData.length);
    }

    public static int packRgb(int red, int green, int blue) {
        return red << 16 | green << 8 | blue;
    }

    public static int redComponent(int rgb) {
        return rgb >>> 16 & COLOR_COMPONENT_MASK;
    }

    public static int greenComponent(int rgb) {
        return rgb >>> 8 & COLOR_COMPONENT_MASK;
    }

    public static int blueComponent(int rgb) {
        return rgb & COLOR_COMPONENT_MASK;
    }
}
