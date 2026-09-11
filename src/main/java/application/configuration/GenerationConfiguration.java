package application.configuration;

import application.model.ImageSize;
import application.model.Space;
import application.rendering.renderer.Renderer;
import application.rendering.transformation.Transformation;
import application.model.TransformationParameters;

import javax.imageio.ImageWriter;
import java.nio.file.Path;
import java.util.List;

public record GenerationConfiguration(
        Path outputPath,
        ImageSize imageSize,
        ImageWriter imageWriter,
        Renderer renderer,
        Space visibleSpace,
        Integer iterationCount,
        Integer randomSeed,
        TransformationParameters transformationParameters,
        List<Transformation> transformations) {}
