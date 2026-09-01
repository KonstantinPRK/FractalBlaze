package application.parameters;

import application.renderer.Renderer;
import application.world.Magnifier;
import application.transformation.Transformation;

import java.nio.file.Path;
import java.util.List;

public record Configuration(
        Path outputPath,
        ImageSize imageSize,
        ImageFormat imageFormat,
        Magnifier magnifierZoom,
        Integer iterationCount,
        Long randomSeed,
        Renderer renderer,
        List<Transformation> transformations
) {}
