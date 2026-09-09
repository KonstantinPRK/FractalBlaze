package application.rendering.renderer;

import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;
import application.picture.FractalImage;
import application.picture.Space;
import application.rendering.ColorPalette;
import application.rendering.Layer;
import application.rendering.RenderingContext;
import application.rendering.Stroker;
import application.rendering.layerMerger.LayerMerger;
import application.transformation.Transformation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SingleThreadRenderer implements Renderer {
    private final ColorPalette colorPalette;
    private final Stroker stroker;
    private final LayerMerger layerMerger;

    public SingleThreadRenderer(ColorPalette colorPalette, Stroker stroker, @Qualifier("singleThreadLayerMerger") LayerMerger layerMerger) {
        this.colorPalette = colorPalette;
        this.stroker = stroker;
        this.layerMerger = layerMerger;
    }

    @Override
    public String getName() {
        return "SINGLE_THREAD — последовательно рисует и объединяет слои";
    }

    @Override
    public FractalImage render(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int userIterationCount,
            long randomSeed)
    {
        RenderingContext context = RenderingContext.create(canvas, space, transformationParameters, transformations, userIterationCount, randomSeed, colorPalette);

        return drawLayers(canvas, context);
    }

    private FractalImage drawLayers(FractalImage canvas, RenderingContext context) {
        int trajectoryCount = stroker.calculateTrajectoryCount(context);

        layerMerger.startMerging(canvas);

        Layer renderedLayer = stroker.spray(context, 0, trajectoryCount);
        layerMerger.merge(renderedLayer);

        return layerMerger.getMergingResult();
    }
}
