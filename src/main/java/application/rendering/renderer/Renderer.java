package application.rendering.renderer;

import application.transformation.Transformation;
import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;
import application.picture.FractalImage;
import application.picture.Space;

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
