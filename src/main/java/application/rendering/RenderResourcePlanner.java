package application.rendering;

import application.configuration.setting.RenderResourceSettings;
import application.model.FractalImage;
import application.model.ImageSize;
import org.springframework.stereotype.Component;

@Component
public final class RenderResourcePlanner {
    private static final long BYTES_PER_MEBIBYTE = 1024L * 1024L;

    private final RenderResourceSettings settings;

    public RenderResourcePlanner(RenderResourceSettings settings) {
        this.settings = settings;
    }

    public void ensureGenerationCanStart(ImageSize imageSize) {
        HeapBudget heapBudget = readHeapBudget();

        long canvasBytes = FractalImage.estimateMemoryUsage(imageSize);
        long layerBytes = Layer.estimateMemoryUsage(imageSize);
        long temporaryRgbBytes = FractalImage.estimateRgbBufferMemoryUsage(imageSize);

        long minimumRenderPeak = Math.addExact(canvasBytes, layerBytes);
        long smoothingPeak = Math.addExact(canvasBytes, temporaryRgbBytes);
        long encodingPeak = Math.addExact(canvasBytes, temporaryRgbBytes);
        long minimumRequiredBytes = Math.max(minimumRenderPeak, Math.max(smoothingPeak, encodingPeak));

        if (minimumRequiredBytes > heapBudget.allowedBytes()) {
            throw new IllegalStateException(
                    "Недостаточно памяти для изображения " + imageSize.width() + " × " + imageSize.height()
                            + ". Минимально требуется примерно " + toMebibytes(minimumRequiredBytes) + " МиБ"
                            + ", разрешённый бюджет: " + toMebibytes(heapBudget.allowedBytes()) + " МиБ"
                            + ", свободно в куче: " + toMebibytes(heapBudget.availableBytes()) + " МиБ"
                            + ", максимальная куча: " + toMebibytes(heapBudget.maximumBytes()) + " МиБ");
        }
    }

    public int calculateLayerCount(ImageSize imageSize, int availableThreadCount, int trajectoryCount) {
        if (availableThreadCount < 1) throw new IllegalArgumentException("Количество потоков должно быть положительным");
        if (trajectoryCount < 1) throw new IllegalArgumentException("Количество траекторий должно быть положительным");

        HeapBudget heapBudget = readHeapBudget();
        long bytesPerLayer = Layer.estimateMemoryUsage(imageSize);
        long layerCountAllowedByHeap = heapBudget.allowedBytes() / bytesPerLayer;

        if (layerCountAllowedByHeap < 1) {
            throw new IllegalStateException(
                    "Недостаточно памяти для слоя " + imageSize.width() + " × " + imageSize.height()
                            + ". Требуется примерно " + toMebibytes(bytesPerLayer) + " МиБ"
                            + ", разрешённый бюджет: " + toMebibytes(heapBudget.allowedBytes()) + " МиБ"
                            + ", свободно в куче: " + toMebibytes(heapBudget.availableBytes()) + " МиБ"
                            + ", максимальная куча: " + toMebibytes(heapBudget.maximumBytes()) + " МиБ");
        }

        long selectedLayerCount = Math.min(availableThreadCount, layerCountAllowedByHeap);
        selectedLayerCount = Math.min(selectedLayerCount, trajectoryCount);

        return Math.toIntExact(selectedLayerCount);
    }

    private HeapBudget readHeapBudget() {
        Runtime runtime = Runtime.getRuntime();
        long maximumHeap = runtime.maxMemory();
        long currentlyUsedHeap = runtime.totalMemory() - runtime.freeMemory();
        long currentlyAvailableHeap = maximumHeap - currentlyUsedHeap;
        long allowedHeap = (long) Math.floor(currentlyAvailableHeap * settings.availableHeapShare());

        return new HeapBudget(maximumHeap, currentlyAvailableHeap, allowedHeap);
    }

    private long toMebibytes(long bytes) {
        return bytes / BYTES_PER_MEBIBYTE;
    }

    private record HeapBudget(long maximumBytes, long availableBytes, long allowedBytes) {}
}
