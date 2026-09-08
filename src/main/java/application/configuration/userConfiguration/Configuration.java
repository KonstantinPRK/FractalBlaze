package application.configuration.userConfiguration;

import application.configuration.userConfiguration.userParameterRecords.ImageSize;
import application.rendering.renderer.Renderer;
import application.picture.Space;
import application.transformation.Transformation;
import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;

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
        List<Transformation> transformations) {}
