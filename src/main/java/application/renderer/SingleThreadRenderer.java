package application.renderer;

import application.transformation.Transformation;
import application.world.FractalImage;
import application.world.Magnifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SingleThreadRenderer implements Renderer {
    private static final int BURN_IN = 20;

    public FractalImage render(
            FractalImage canvas,
            Magnifier world,
            List<Transformation> transformations,
            int iterationCount,
            long seed
    ) {
        return canvas;
    }

    @Override
    public FractalImage render(FractalImage emptyCanvas, Magnifier magnifier, List<Transformation> transformations, Integer integer, Integer randomSeed) {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
