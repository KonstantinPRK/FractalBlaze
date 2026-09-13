package application.configuration.setting;

public record RenderResourceSettings(int availableHeapPercent) {
    public RenderResourceSettings {
        if (availableHeapPercent < 1 || availableHeapPercent > 99) {
            throw new IllegalArgumentException(
                    "Процент доступной кучи должен находиться в диапазоне от 1 до 99");
        }
    }
}
