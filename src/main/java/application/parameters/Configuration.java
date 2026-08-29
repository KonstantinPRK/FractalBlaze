package application.parameters;

import application.transformation.Transformation;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;

public record Configuration(
        ImageSize imageSize,
        ImageFormat imageFormat,
        Path outputPath,
        Integer iterationCount,
        Random random,
        Transformation[] transformationsArray)
{

    //потом дополни
    public Configuration {
        Objects.requireNonNull(imageSize, "imageSize must not be null");
        Objects.requireNonNull(imageFormat, "imageFormat must not be null");
        Objects.requireNonNull(iterationCount, "iterationCount must not be null");
        Objects.requireNonNull(random, "random must not be null");
        Objects.requireNonNull(transformationsArray, "transformationsArray must not be null");

        for (Transformation transformation : transformationsArray) {
            Objects.requireNonNull(transformation, "transformationsArray must not contain null");
        }
    }

}
