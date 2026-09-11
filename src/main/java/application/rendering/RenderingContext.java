package application.rendering;

import application.model.TransformationParameters;
import application.model.FractalImage;
import application.model.Space;
import application.rendering.transformation.Transformation;

import java.util.List;
import java.util.SplittableRandom;

public record RenderingContext(
        int imageWidth,
        int imageHeight,
        Space space,
        TransformationParameters transformationParameters,
        List<Transformation> transformations,
        int[] transformationColors,
        double cosOfRotation,
        double sinOfRotation,
        int iterationCount,
        long randomSeed)
{
    public static RenderingContext create(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int iterationCount,
            long randomSeed,
            ColorPalette colorPalette)
    {
        SplittableRandom colorRandom = new SplittableRandom(randomSeed).split();
        int[] transformationColors = colorPalette.setColorForTransformations(transformations.size(), colorRandom);

        double cosOfRotation = Math.cos(transformationParameters.rotationAngleInRadians());
        double sinOfRotation = Math.sin(transformationParameters.rotationAngleInRadians());

        return new RenderingContext(
                canvas.width(),
                canvas.height(),
                space,
                transformationParameters,
                List.copyOf(transformations),
                transformationColors,
                cosOfRotation,
                sinOfRotation,
                iterationCount,
                randomSeed);
    }
}
