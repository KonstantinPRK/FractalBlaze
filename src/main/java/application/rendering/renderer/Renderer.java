package application.rendering.renderer;

import application.rendering.transformation.Transformation;
import application.model.TransformationParameters;
import application.model.FractalImage;
import application.model.Space;

import java.util.List;

public interface Renderer {
    String getName();

    FractalImage render(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int userIterationCount,
            long randomSeed);
}
