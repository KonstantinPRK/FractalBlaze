package application.rendering;

import application.configuration.systemConfiguration.settingsRecords.TrajectorySettings;
import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;
import application.picture.FractalImage;
import application.picture.Point;
import application.picture.Space;
import application.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SplittableRandom;

@Component
public class TrajectoryDrawer {
    private final int BURN_IN;
    private final ColorPalette colorPalette;
    private final PointTransformer pointTransformer;
    private final WorldToPixelMapper worldToPixelMapper;

    private record TrajectoryState(Point point, int color) {}

    public TrajectoryDrawer(TrajectorySettings settings, ColorPalette colorPalette, PointTransformer pointTransformer, WorldToPixelMapper worldToPixelMapper) {
        BURN_IN = settings.burnIn();
        this.colorPalette = colorPalette;
        this.pointTransformer = pointTransformer;
        this.worldToPixelMapper = worldToPixelMapper;
    }

    public boolean drawTrajectory(
            FractalImage canvas,
            Space space,
            TransformationParameters transformationParameters,
            List<Transformation> transformations,
            int[] transformationColors,
            int trajectoryIterationCount,
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
        boolean atLeastOnePointWasRecorded = false;

        for (int trajectorySum = 0; trajectorySum < trajectoryIterationCount; trajectorySum++) {
            int transformationIndex = random.nextInt(transformations.size());
            Transformation selectedTransformation = transformations.get(transformationIndex);
            Point transformedPoint = pointTransformer.applyTransformation(currentPoint, selectedTransformation, transformationParameters, cosOfRotation, sinOfRotation);

            boolean pointHasInvalidCoordinates =
                       Double.isNaN(transformedPoint.x())
                    || Double.isInfinite(transformedPoint.x())
                    || Double.isNaN(transformedPoint.y())
                    || Double.isInfinite(transformedPoint.y());

            if (pointHasInvalidCoordinates) {
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

            if (recordPointHit(currentPoint, currentColor, space, canvas))atLeastOnePointWasRecorded = true;
        }

        return atLeastOnePointWasRecorded;
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
            currentPoint = pointTransformer.applyTransformation(currentPoint, selectedTransformation, transformationParameters, cosOfRotation, sinOfRotation);

            if (warmUpIteration == 0)currentColor = transformationColors[transformationIndex];
            else currentColor = colorPalette.mixColors(currentColor, transformationColors[transformationIndex]);
        }

        return new TrajectoryState(currentPoint, currentColor);
    }

    private Point createRandomPoint(Space space, SplittableRandom random) {
        double leftBorder = space.focusPoint().x() - space.visibleWorldWidth() / 2;
        double bottomBorder = space.focusPoint().y() - space.visibleWorldHeight() / 2;

        double randomX = random.nextDouble(leftBorder, leftBorder + space.visibleWorldWidth());
        double randomY = random.nextDouble(bottomBorder, bottomBorder + space.visibleWorldHeight());

        return new Point(randomX, randomY);
    }

    private boolean recordPointHit(Point point, int color, Space space, FractalImage canvas) {
        boolean pointIsOutsideVisibleSpace = !space.contains(point);
        if (pointIsOutsideVisibleSpace)return false;

        int pixelX = worldToPixelMapper.mapHorizontalCoordinate(point, space, canvas);
        int pixelY = worldToPixelMapper.mapVerticalCoordinate(point, space, canvas);

        boolean pixelIsOutsideCanvas = !canvas.contains(pixelX, pixelY);
        if (pixelIsOutsideCanvas)return false;

        canvas.addHit(pixelX, pixelY, color);
        return true;
    }
}
