package application.renderer;

import application.transformation.Transformation;
import application.world.FractalImage;
import application.world.Magnifier;
import application.world.Space;

import java.util.List;

public interface Renderer {
    FractalImage render(
            FractalImage emptyCanvas,
            Space magnifier,
            List<Transformation> transformations,
            int iterationCount,
            long randomSeed
    );

    String getName();
}
