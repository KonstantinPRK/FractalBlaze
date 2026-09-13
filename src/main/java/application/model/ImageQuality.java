package application.model;

public enum ImageQuality {
    HD_720P("HD, 720p — 1280 × 720", 1280, 720),
    FULL_HD_1080P("Full HD, 1080p — 1920 × 1080", 1920, 1080),
    QUAD_HD_1440P("Quad HD, 1440p — 2560 × 1440", 2560, 1440),
    UHD_4K("4K UHD — 3840 × 2160", 3840, 2160);

    private final String displayName;
    private final int referenceWidth;
    private final int referenceHeight;

    ImageQuality(String displayName, int referenceWidth, int referenceHeight) {
        this.displayName = displayName;
        this.referenceWidth = referenceWidth;
        this.referenceHeight = referenceHeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long pixelBudget() {
        return (long) referenceWidth * referenceHeight;
    }
}
