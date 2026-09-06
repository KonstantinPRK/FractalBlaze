package application.userConfiguration;

import application.userConfiguration.parameters.ImageSize;
import application.renderer.Renderer;
import application.picture.Space;
import application.transformation.Transformation;
import application.transformation.TransformationParameters;

import javax.imageio.ImageWriter;
import java.nio.file.Path;
import java.util.List;

public record Configuration(
        Path outputPath,
        ImageSize imageSize,
        ImageWriter imageWriter,
        Space visibleSpace,
        Integer iterationCount,
        Integer randomSeed,
        Renderer renderer,
        TransformationParameters transformationParameters,
        List<Transformation> transformations
) {}
