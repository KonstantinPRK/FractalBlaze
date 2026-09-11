package application.rendering.renderer;

import application.model.TransformationParameters;
import application.model.FractalImage;
import application.model.Space;
import application.rendering.ColorPalette;
import application.rendering.Layer;
import application.rendering.RenderingContext;
import application.rendering.trajectory.Stroker;
import application.rendering.state.ImageStateCollector;
import application.rendering.state.ImageStateCollectorFactory;
import application.rendering.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SingleThreadRenderer implements Renderer {
    private final ColorPalette colorPalette;
    private final Stroker stroker;
    private final ImageStateCollectorFactory imageStateCollectorFactory;

    public SingleThreadRenderer(ColorPalette colorPalette, Stroker stroker, ImageStateCollectorFactory imageStateCollectorFactory) {
        this.colorPalette = colorPalette;
        this.stroker = stroker;
        this.imageStateCollectorFactory = imageStateCollectorFactory;
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
        ImageStateCollector imageStateCollector = imageStateCollectorFactory.createForSingleThread(canvas);

        Layer renderedLayer = stroker.spray(context, 0, trajectoryCount);
        imageStateCollector.collectLayer(renderedLayer);

        return imageStateCollector.getSnapshot();
    }
}
