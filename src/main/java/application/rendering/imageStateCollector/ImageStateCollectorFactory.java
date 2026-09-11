package application.rendering.imageStateCollector;

import application.picture.FractalImage;
import application.rendering.lineMerger.LayerLineMerger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public final class ImageStateCollectorFactory {
    private final LayerLineMerger singleThreadRowsMerger, singleThreadColumnsMerger, multiThreadRowsMerger, multiThreadColumnsMerger;

    public ImageStateCollectorFactory(
            @Qualifier("singleThreadLayerRowsMerger") LayerLineMerger singleThreadRowsMerger,
            @Qualifier("singleThreadLayerColumnsMerger") LayerLineMerger singleThreadColumnsMerger,
            @Qualifier("multiThreadLayerRowsMerger") LayerLineMerger multiThreadRowsMerger,
            @Qualifier("multiThreadLayerColumnsMerger") LayerLineMerger multiThreadColumnsMerger)
    {
        this.singleThreadRowsMerger = singleThreadRowsMerger;
        this.singleThreadColumnsMerger = singleThreadColumnsMerger;
        this.multiThreadRowsMerger = multiThreadRowsMerger;
        this.multiThreadColumnsMerger = multiThreadColumnsMerger;
    }

    public ImageStateCollector createForSingleThread(FractalImage canvas) {
        return createCollector(canvas, singleThreadRowsMerger, singleThreadColumnsMerger);
    }

    public ImageStateCollector createForMultiThread(FractalImage canvas) {
        return createCollector(canvas, multiThreadRowsMerger, multiThreadColumnsMerger);
    }

    private ImageStateCollector createCollector(FractalImage canvas, LayerLineMerger rowsMerger, LayerLineMerger columnsMerger) {
        LayerLineMerger selectedMerger = canvas.width() >= canvas.height() ? rowsMerger : columnsMerger;
        return new ImageStateCollector(canvas, selectedMerger);
    }
}
