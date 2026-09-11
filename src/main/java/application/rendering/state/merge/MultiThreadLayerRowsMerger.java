package application.rendering.state.merge;

import application.rendering.Layer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component("multiThreadLayerRowsMerger")
public class MultiThreadLayerRowsMerger implements LayerLineMerger {
    private final ExecutorService executor;

    public MultiThreadLayerRowsMerger(@Qualifier("renderingExecutor") ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void mergeLines(Layer destinationLayer, Layer sourceLayer) {
        List<Future<Void>> rowMergingTasks = new ArrayList<>(sourceLayer.height());

        try {
            for (int rowIndex = 0; rowIndex < sourceLayer.height(); rowIndex++) {
                int currentRowIndex = rowIndex;

                Future<Void> rowMergingTask = executor.submit(() -> {
                    mergeRow(destinationLayer, sourceLayer, currentRowIndex);
                    return null;
                });

                rowMergingTasks.add(rowMergingTask);
            }

            for (Future<Void> rowMergingTask : rowMergingTasks) {
                rowMergingTask.get();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Построчное объединение слоев прервано", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Не удалось объединить строки слоев", exception.getCause());
        } finally {
            rowMergingTasks.forEach(rowMergingTask -> rowMergingTask.cancel(true));
        }
    }

    private void mergeRow(Layer destinationLayer, Layer sourceLayer, int rowIndex) {
        int firstPixelIndex = rowIndex * sourceLayer.width();
        int endPixelIndex = firstPixelIndex + sourceLayer.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) {
            mergePixel(destinationLayer, sourceLayer, pixelIndex);
        }
    }

    private void mergePixel(Layer destinationLayer, Layer sourceLayer, int pixelIndex) {
        int hitCount = sourceLayer.hitCount(pixelIndex);
        if (hitCount == 0)return;

        destinationLayer.addPixelData(pixelIndex, hitCount, sourceLayer.redSum(pixelIndex), sourceLayer.greenSum(pixelIndex), sourceLayer.blueSum(pixelIndex));
    }
}
