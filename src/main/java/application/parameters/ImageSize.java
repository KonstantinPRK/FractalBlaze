package application.parameters;

public record ImageSize(int width, int height) {
    public ImageSize {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image dimensions must be positive");
        }
    }
}
