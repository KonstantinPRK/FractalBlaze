package application.rendering.trajectory;

import application.configuration.setting.TrajectorySettings;
import application.model.Space;
import application.rendering.ColorPalette;
import application.rendering.Layer;
import application.rendering.MutablePoint;
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

    public TrajectoryDrawer(TrajectorySettings settings, ColorPalette colorPalette, PointTransformer pointTransformer, WorldToPixelMapper worldToPixelMapper) {
        this.BURN_IN = settings.burnIn();
        this.colorPalette = colorPalette;
        this.pointTransformer = pointTransformer;
        this.worldToPixelMapper = worldToPixelMapper;
    }

    public void drawTrajectory(Layer layer, RenderingContext context, int trajectoryIterationCount, SplittableRandom random) {
        MutablePoint currentPoint = new MutablePoint();
        int currentColor = warmUpTrajectory(currentPoint, context, random);

        for (int trajectorySum = 0; trajectorySum < trajectoryIterationCount; trajectorySum++) {
            int transformationIndex = random.nextInt(context.transformations().size());
            Transformation selectedTransformation = context.transformations().get(transformationIndex);
            pointTransformer.applyTransformation(currentPoint, selectedTransformation, context.transformationParameters(), context.cosOfRotation(), context.sinOfRotation());

            if (!currentPoint.hasFiniteCoordinates()) {
                currentColor = warmUpTrajectory(currentPoint, context, random);
                continue;
            }

            currentColor = colorPalette.mixColors(currentColor, context.transformationColors()[transformationIndex]);

            recordPointHit(currentPoint.x(), currentPoint.y(), currentColor, layer, context.space());
        }
    }

    private int warmUpTrajectory(MutablePoint currentPoint, RenderingContext context, SplittableRandom random) {
        resetRandomPoint(currentPoint, context.space(), random);
        int currentColor = 0;

        for (int warmUpIteration = 0; warmUpIteration < BURN_IN; warmUpIteration++) {
            int transformationIndex = random.nextInt(context.transformations().size());
            Transformation selectedTransformation = context.transformations().get(transformationIndex);

            pointTransformer.applyTransformation(currentPoint, selectedTransformation, context.transformationParameters(), context.cosOfRotation(), context.sinOfRotation());

            if (warmUpIteration == 0) currentColor = context.transformationColors()[transformationIndex];
            else currentColor = colorPalette.mixColors(currentColor, context.transformationColors()[transformationIndex]);
        }

        return currentColor;
    }


    private void resetRandomPoint(MutablePoint point, Space space, SplittableRandom random) {
        double leftBorder = space.focusPoint().x() - space.visibleWorldWidth() / 2;
        double bottomBorder = space.focusPoint().y() - space.visibleWorldHeight() / 2;

        double randomX = random.nextDouble(leftBorder, leftBorder + space.visibleWorldWidth());
        double randomY = random.nextDouble(bottomBorder, bottomBorder + space.visibleWorldHeight());

        point.set(randomX, randomY);
    }


    private void recordPointHit(double pointX, double pointY, int color, Layer layer, Space space) {
        if (!space.contains(pointX, pointY)) return;

        int pixelX = worldToPixelMapper.mapHorizontalCoordinate(pointX, space, layer.width());
        int pixelY = worldToPixelMapper.mapVerticalCoordinate(pointY, space, layer.height());

        if (!layer.contains(pixelX, pixelY)) return;

        layer.addHit(pixelX, pixelY, color);
    }
}
