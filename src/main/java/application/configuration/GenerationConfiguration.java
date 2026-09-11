package application.configuration;

import application.model.ImageSize;
import application.rendering.renderer.Renderer;
import application.model.Space;
import application.rendering.transformation.Transformation;
import application.model.TransformationParameters;

import javax.imageio.ImageWriter;
import java.nio.file.Path;
import java.util.List;

public record GenerationConfiguration(
        Path outputPath,
        ImageSize imageSize,
        ImageWriter imageWriter,
        Space visibleSpace,
        Integer iterationCount,
        Integer randomSeed,
        Renderer renderer,
        TransformationParameters transformationParameters,
        List<Transformation> transformations) {}
