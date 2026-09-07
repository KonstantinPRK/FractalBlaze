package application.renderer;

import application.configuration.systemConfiguration.settingsRecords.SingleThreadRendererSettings;
import application.picture.ColorPalette;
import application.picture.FractalImage;
import application.picture.Point;
import application.picture.Space;
import application.transformation.Transformation;
import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SplittableRandom;

@Component
public class SingleThreadRenderer implements Renderer {
    private final ColorPalette colorPalette;
    private final int BURN_IN, ITERATIONS_PER_TRAJECTORY;


    private record TrajectoryState(Point point, int color) {}


    public SingleThreadRenderer(SingleThreadRendererSettings settings, ColorPalette colorPalette) {
        BURN_IN = settings.burnIn();
        ITERATIONS_PER_TRAJECTORY = settings.iterationsPerTrajectory();
        this.colorPalette = colorPalette;
    }

    @Override
    public String getName() {
        return "SINGLE_THREAD — последовательно рисует фрактал в одном потоке";
    }

    @Override
    public FractalImage render(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int userIterationCount,
            long randomSeed)
    {

        SplittableRandom random = new SplittableRandom(randomSeed);
        int[] transformationColors = colorPalette.setColorForTransformations(transformations.size(), random.split());

        double cosOfRotation = Math.cos(transformationParameters.rotationAngleInRadians());
        double sinOfRotation = Math.sin(transformationParameters.rotationAngleInRadians());

        drawAllTrajectories(
                canvas,
                space,
                transformationParameters,
                transformations,
                transformationColors,
                userIterationCount,
                cosOfRotation,
                sinOfRotation,
                random);

        validateAtLeastOnePointWasDrawn(canvas);
        return canvas;
    }



    private void drawAllTrajectories(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int[] transformationColors,
            int userIterationCount,
            double cosOfRotation,
            double sinOfRotation,
            SplittableRandom random)
    {

        for (int iterationSum = 0; iterationSum < userIterationCount; iterationSum += ITERATIONS_PER_TRAJECTORY) {
            int iterationsInCurrentTrajectory = Math.min(ITERATIONS_PER_TRAJECTORY, userIterationCount - iterationSum);
            drawSingleTrajectory(
                    canvas,
                    space,
                    transformationParameters,
                    transformations,
                    transformationColors,
                    iterationsInCurrentTrajectory,
                    cosOfRotation,
                    sinOfRotation,
                    random);
        }

    }


    private void drawSingleTrajectory(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int[] transformationColors,
            int iterationsInCurrentTrajectory,
            double cosOfRotation,
            double sinOfRotation,
            SplittableRandom random)
    {
        TrajectoryState trajectory = warmUpTrajectory(
                createRandomPoint(space, random),
                transformations,
                transformationColors,
                transformationParameters,
                cosOfRotation,
                sinOfRotation,
                random);

        Point currentPoint = trajectory.point();
        int currentColor = trajectory.color();

        for (int trajectorySum = 0; trajectorySum < iterationsInCurrentTrajectory; trajectorySum++) {
            int transformationIndex = random.nextInt(transformations.size());
            Transformation selectedTransformation = transformations.get(transformationIndex);
            Point transformedPoint = applyTransformation(currentPoint, selectedTransformation, transformationParameters, cosOfRotation, sinOfRotation);

            boolean pointLocationIsCorrect =
                       Double.isNaN(transformedPoint.x())
                    || Double.isInfinite(transformedPoint.x())
                    || Double.isNaN(transformedPoint.y())
                    || Double.isInfinite(transformedPoint.y());

            if (pointLocationIsCorrect) {
                trajectory = warmUpTrajectory(
                        createRandomPoint(space, random),
                        transformations,
                        transformationColors,
                        transformationParameters,
                        cosOfRotation,
                        sinOfRotation,
                        random);

                currentPoint = trajectory.point();
                currentColor = trajectory.color();
                continue;
            }

            currentPoint = transformedPoint;
            currentColor = colorPalette.mixColors(currentColor, transformationColors[transformationIndex]);
            recordPointHit(currentPoint, currentColor, space, canvas);
        }
    }

    private Point createRandomPoint(Space space, SplittableRandom random) {
        double leftBorder = space.focusPoint().x() - space.visibleWorldWidth() / 2;
        double bottomBorder = space.focusPoint().y() - space.visibleWorldHeight() / 2;

        double randomX = random.nextDouble(leftBorder, leftBorder + space.visibleWorldWidth());
        double randomY = random.nextDouble(bottomBorder, bottomBorder + space.visibleWorldHeight());

        return new Point(randomX, randomY);
    }

    private TrajectoryState warmUpTrajectory(
            Point startingPoint,
            List<Transformation> transformations,
            int[] transformationColors,
            TransformationParameters transformationParameters,
            double cosOfRotation,
            double sinOfRotation,
            SplittableRandom random)
    {
        Point currentPoint = startingPoint;
        int currentColor = 0;

        for (int warmUpIteration = 0; warmUpIteration < BURN_IN; warmUpIteration++) {
            int transformationIndex = random.nextInt(transformations.size());
            Transformation selectedTransformation = transformations.get(transformationIndex);
            currentPoint = applyTransformation(currentPoint, selectedTransformation, transformationParameters, cosOfRotation, sinOfRotation);

            if (warmUpIteration == 0)currentColor = transformationColors[transformationIndex];
            else currentColor = colorPalette.mixColors(currentColor, transformationColors[transformationIndex]);
        }

        return new TrajectoryState(currentPoint, currentColor);
    }

    private Point applyTransformation(Point point, Transformation transformation, TransformationParameters transformationParameters, double cosOfRotation, double sinOfRotation) {
        double scaledX = point.x() * transformationParameters.scale();
        double scaledY = point.y() * transformationParameters.scale();

        double rotatedX = scaledX * cosOfRotation - scaledY * sinOfRotation;
        double rotatedY = scaledX * sinOfRotation + scaledY * cosOfRotation;

        Point transformedByCommonParameters =
                new Point(
                rotatedX + transformationParameters.shiftX(),
                rotatedY + transformationParameters.shiftY()
                );

        return transformation.apply(transformedByCommonParameters);
    }

    private void validateAtLeastOnePointWasDrawn(FractalImage canvas) {
        for (int pixelIndex = 0; pixelIndex < canvas.data().length; pixelIndex++) {
            if (canvas.data()[pixelIndex].hitCount() > 0)return;
        }

        throw new IllegalArgumentException("Ни одна точка не попала в видимую область. " + "Измените параметры преобразований или масштаб мира.");
    }

    private void recordPointHit(Point point, int color, Space space, FractalImage canvas) {
        if (!space.contains(point))return;

        double leftBorder = space.focusPoint().x() - space.visibleWorldWidth() / 2;
        double bottomBorder = space.focusPoint().y() - space.visibleWorldHeight() / 2;

        double relativeHorizontalPosition = (point.x() - leftBorder) / space.visibleWorldWidth();
        double relativeVerticalPosition = (point.y() - bottomBorder) / space.visibleWorldHeight();

        int pixelX = (int) (relativeHorizontalPosition * canvas.width());
        int pixelY = canvas.height() - 1 - (int) (relativeVerticalPosition * canvas.height());

        if (canvas.contains(pixelX, pixelY))canvas.addHit(pixelX, pixelY, color);
    }


}
