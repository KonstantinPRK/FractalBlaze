package application.configuration.setting;

public record RenderResourceSettings(double availableHeapShare) {
    public RenderResourceSettings {
        if (!Double.isFinite(availableHeapShare)
                || availableHeapShare <= 0.0
                || availableHeapShare >= 1.0) {
            throw new IllegalArgumentException(
                    "Доля доступной кучи должна быть больше 0 и меньше 1");
        }
    }
}
