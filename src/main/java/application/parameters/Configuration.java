package application.parameters;

import application.renderer.Renderer;
import application.world.Magnifier;
import application.transformation.Transformation;

import javax.imageio.ImageWriter;
import java.nio.file.Path;
import java.util.List;

public record Configuration(
        Path outputPath,
        ImageSize imageSize,
        ImageWriter imageWriter,
        Magnifier magnifierZoom,
        Integer iterationCount,
        Integer randomSeed,
        Renderer renderer,
        List<Transformation> transformations
) {}
