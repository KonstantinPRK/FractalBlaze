package application.model;

public enum ImageShape {
    LANDSCAPE("Альбомная"),
    PORTRAIT("Вертикальная");

    private final String displayName;

    ImageShape(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
