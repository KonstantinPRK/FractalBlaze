package application.rendering.trajectory;

import application.configuration.setting.TrajectorySettings;
import application.model.Point;
import application.model.Space;
import application.rendering.ColorPalette;
import application.rendering.Layer;
import application.rendering.RenderingContext;
import application.rendering.WorldToPixelMapper;
import application.rendering.transformation.PointTransformer;
import application.rendering.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.SplittableRandom;

@Component
public class TrajectoryDrawer {
    private final int BURN_IN;
    private final ColorPalette colorPalette;
    private final PointTransformer pointTransformer;
    private final WorldToPixelMapper worldToPixelMapper;

    private record TrajectoryState(Point point, int color) {}

    public TrajectoryDrawer(TrajectorySettings settings, ColorPalette colorPalette, PointTransformer pointTransformer, WorldToPixelMapper worldToPixelMapper) {
        this.BURN_IN = settings.burnIn();
        this.colorPalette = colorPalette;
        this.pointTransformer = pointTransformer;
        this.worldToPixelMapper = worldToPixelMapper;
    }

    public void drawTrajectory(Layer layer, RenderingContext context, int trajectoryIterationCount, SplittableRandom random) {
        TrajectoryState trajectory = warmUpTrajectory(context, random);
        Point currentPoint = trajectory.point();
        int currentColor = trajectory.color();

        for (int trajectorySum = 0; trajectorySum < trajectoryIterationCount; trajectorySum++) {
            int transformationIndex = random.nextInt(context.transformations().size());
            Transformation selectedTransformation = context.transformations().get(transformationIndex);
            Point transformedPoint = pointTransformer.applyTransformation(currentPoint, selectedTransformation, context.transformationParameters(), context.cosOfRotation(), context.sinOfRotation());

            boolean pointHasInvalidCoordinates =
                       Double.isNaN(transformedPoint.x())
                    || Double.isInfinite(transformedPoint.x())
                    || Double.isNaN(transformedPoint.y())
                    || Double.isInfinite(transformedPoint.y());

            if (pointHasInvalidCoordinates) {
                trajectory = warmUpTrajectory(context, random);
                currentPoint = trajectory.point();
                currentColor = trajectory.color();
                continue;
            }

            currentPoint = transformedPoint;
            currentColor = colorPalette.mixColors(currentColor, context.transformationColors()[transformationIndex]);

            recordPointHit(currentPoint, currentColor, layer, context);
        }
    }

    private TrajectoryState warmUpTrajectory(RenderingContext context, SplittableRandom random) {
        Point currentPoint = createRandomPoint(context.space(), random);
        int currentColor = 0;

        for (int warmUpIteration = 0; warmUpIteration < BURN_IN; warmUpIteration++) {
            int transformationIndex = random.nextInt(context.transformations().size());
            Transformation selectedTransformation = context.transformations().get(transformationIndex);

            currentPoint = pointTransformer.applyTransformation(currentPoint, selectedTransformation, context.transformationParameters(), context.cosOfRotation(), context.sinOfRotation());

            if (warmUpIteration == 0) currentColor = context.transformationColors()[transformationIndex];
            else currentColor = colorPalette.mixColors(currentColor, context.transformationColors()[transformationIndex]);
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


    private void recordPointHit(Point point, int color, Layer layer, RenderingContext context) {
        boolean pointIsOutsideVisibleSpace = !context.space().contains(point);
        if (pointIsOutsideVisibleSpace)return;

        int pixelX = worldToPixelMapper.mapHorizontalCoordinate(point, context);
        int pixelY = worldToPixelMapper.mapVerticalCoordinate(point, context);

        boolean pixelIsOutsideCanvas = !layer.contains(pixelX, pixelY);
        if (pixelIsOutsideCanvas)return;

        layer.addHit(pixelX, pixelY, color);
    }
}
