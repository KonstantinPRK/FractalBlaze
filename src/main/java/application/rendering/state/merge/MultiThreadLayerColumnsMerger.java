package application.rendering.state.merge;

import application.rendering.Layer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component("multiThreadLayerColumnsMerger")
public class MultiThreadLayerColumnsMerger implements LayerLineMerger {
    private final ExecutorService executor;

    public MultiThreadLayerColumnsMerger(@Qualifier("renderingExecutor") ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void mergeLines(Layer destinationLayer, Layer sourceLayer) {
        List<Future<Void>> columnMergingTasks = new ArrayList<>(sourceLayer.width());

        try {
            for (int columnIndex = 0; columnIndex < sourceLayer.width(); columnIndex++) {
                int currentColumnIndex = columnIndex;

                Future<Void> columnMergingTask = executor.submit(() -> {
                    mergeColumn(destinationLayer, sourceLayer, currentColumnIndex);
                    return null;
                });

                columnMergingTasks.add(columnMergingTask);
            }

            for (Future<Void> columnMergingTask : columnMergingTasks) {
                columnMergingTask.get();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Объединение столбцов слоев прервано", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Не удалось объединить столбцы слоев", exception.getCause());
        } finally {
            columnMergingTasks.forEach(columnMergingTask -> columnMergingTask.cancel(true));
        }
    }

    private void mergeColumn(Layer destinationLayer, Layer sourceLayer, int columnIndex) {
        for (int rowIndex = 0; rowIndex < sourceLayer.height(); rowIndex++) {
            int pixelIndex = rowIndex * sourceLayer.width() + columnIndex;
            mergePixel(destinationLayer, sourceLayer, pixelIndex);
        }
    }

    private void mergePixel(Layer destinationLayer, Layer sourceLayer, int pixelIndex) {
        int hitCount = sourceLayer.hitCount(pixelIndex);
        if (hitCount == 0)return;

        destinationLayer.addPixelData(pixelIndex, hitCount, sourceLayer.redSum(pixelIndex), sourceLayer.greenSum(pixelIndex), sourceLayer.blueSum(pixelIndex));
    }
}
