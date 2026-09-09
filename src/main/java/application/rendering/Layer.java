package application.rendering;

public final class Layer {
    private final int width, height;
    private final int[] hitCounts;
    private final long[] redSums, greenSums, blueSums;
    private boolean hasHits;

    public Layer(int width, int height) {
        this.width = width;
        this.height = height;

        int pixelCount = width * height;

        hitCounts = new int[pixelCount];
        redSums = new long[pixelCount];
        greenSums = new long[pixelCount];
        blueSums = new long[pixelCount];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int pixelCount() {
        return hitCounts.length;
    }

    public boolean hasHits() {
        return hasHits;
    }

    public boolean contains(int pixelX, int pixelY) {
        return pixelX >= 0
                && pixelX < width
                && pixelY >= 0
                && pixelY < height;
    }

    public void addHit(int pixelX, int pixelY, int color) {
        int pixelIndex = pixelY * width + pixelX;

        hitCounts[pixelIndex]++;
        redSums[pixelIndex] += red(color);
        greenSums[pixelIndex] += green(color);
        blueSums[pixelIndex] += blue(color);
        hasHits = true;
    }

    public void addPixelData(int pixelIndex, int hitCount, long redSum, long greenSum, long blueSum) {
        hitCounts[pixelIndex] += hitCount;
        redSums[pixelIndex] += redSum;
        greenSums[pixelIndex] += greenSum;
        blueSums[pixelIndex] += blueSum;
    }

    public void markAsContainingHits() {
        hasHits = true;
    }

    public int hitCount(int pixelIndex) {
        return hitCounts[pixelIndex];
    }

    public long redSum(int pixelIndex) {
        return redSums[pixelIndex];
    }

    public long greenSum(int pixelIndex) {
        return greenSums[pixelIndex];
    }

    public long blueSum(int pixelIndex) {
        return blueSums[pixelIndex];
    }

    private int red(int color) {
        return color >> 16 & 0xFF;
    }

    private int green(int color) {
        return color >> 8 & 0xFF;
    }

    private int blue(int color) {
        return color & 0xFF;
    }
}
