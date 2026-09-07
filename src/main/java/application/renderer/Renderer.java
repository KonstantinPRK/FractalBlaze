package application.renderer;

import application.transformation.Transformation;
import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;
import application.picture.FractalImage;
import application.picture.Space;

import java.util.List;

public interface Renderer {
    FractalImage render(FractalImage emptyCanvas, Space magnifier, TransformationParameters transformationParameters, List<Transformation> transformations, int iterationCount, long randomSeed);

    String getName();
}
