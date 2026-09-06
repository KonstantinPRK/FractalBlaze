package application.userConfiguration.parameters;

public record ImageSize(int width, int height) {
    public static int maxSize() {
        return 1920;
    }

    public static int minSize() {
        return 1280;
    }
}
