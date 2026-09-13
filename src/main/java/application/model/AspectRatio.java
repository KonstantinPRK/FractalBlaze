package application.model;

public enum AspectRatio {
    SQUARE("1:1 — квадратное", 1, 1),
    STANDARD("4:3 — стандартное", 4, 3),
    WIDESCREEN("16:9 — широкоформатное", 16, 9),
    ULTRAWIDE("21:9 — сверхширокое", 21, 9);

    private final String displayName;
    private final int horizontalParts;
    private final int verticalParts;

    AspectRatio(String displayName, int horizontalParts, int verticalParts) {
        this.displayName = displayName;
        this.horizontalParts = horizontalParts;
        this.verticalParts = verticalParts;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int horizontalParts() {
        return horizontalParts;
    }

    public int verticalParts() {
        return verticalParts;
    }
}
