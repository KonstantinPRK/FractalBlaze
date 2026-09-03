package application.renderer;

import application.transformation.Transformation;
import application.world.FractalImage;
import application.world.Magnifier;
import application.world.Point;
import application.world.Space;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SplittableRandom;

@Component
public class SingleThreadRenderer implements Renderer {
    private static final int BURN_IN = 20;
    private static final int ITERATIONS_PER_TRAJECTORY = 10_000;

    @Override
    public FractalImage render(
            FractalImage emptyCanvas,
            Space magnifier,
            List<Transformation> transformations,
            int iterationCount,
            long randomSeed
    ) {
        SplittableRandom random = new SplittableRandom(randomSeed);

        for (int firstIteration = 0; firstIteration < iterationCount; firstIteration += ITERATIONS_PER_TRAJECTORY) {
            int iterationsInCurrentTrajectory = Math.min(
                    ITERATIONS_PER_TRAJECTORY,
                    iterationCount - firstIteration
            );

            Point currentPoint = createRandomPoint(magnifier, random);
            currentPoint = warmUpTrajectory(currentPoint, transformations, random);

            for (int trajectoryIteration = 0; trajectoryIteration < iterationsInCurrentTrajectory; trajectoryIteration++) {
                Transformation selectedTransformation = selectRandomTransformation(transformations, random);

                Point transformedPoint = selectedTransformation.apply(currentPoint);

                if (!hasFiniteCoordinates(transformedPoint)) {
                    currentPoint = warmUpTrajectory(createRandomPoint(magnifier, random), transformations, random);
                    continue;
                }

                currentPoint = transformedPoint;
                recordPointHit(currentPoint, magnifier, emptyCanvas);
            }
        }

        ensureAtLeastOnePointWasDrawn(emptyCanvas);
        return emptyCanvas;
    }

    @Override
    public String getName() {
        return "SINGLE_THREAD";
    }

    private Point createRandomPoint(
            Magnifier magnifier,
            SplittableRandom random
    ) {
        double leftBorder = magnifier.focusPoint().x() - magnifier.visibleWorldWidth() / 2;
        double bottomBorder = magnifier.focusPoint().y() - magnifier.visibleWorldHeight() / 2;

        double randomX = random.nextDouble(
                leftBorder,
                leftBorder + magnifier.visibleWorldWidth()
        );

        double randomY = random.nextDouble(
                bottomBorder,
                bottomBorder + magnifier.visibleWorldHeight()
        );

        return new Point(randomX, randomY);
    }

    private Point warmUpTrajectory(
            Point startingPoint,
            List<Transformation> transformations,
            SplittableRandom random
    ) {
        Point currentPoint = startingPoint;

        for (int warmUpIteration = 0; warmUpIteration < BURN_IN; warmUpIteration++) {
            Transformation selectedTransformation = selectRandomTransformation(transformations, random);
            currentPoint = selectedTransformation.apply(currentPoint);
        }

        return currentPoint;
    }

    private Transformation selectRandomTransformation(List<Transformation> transformations, SplittableRandom random) {
        int transformationIndex = random.nextInt(transformations.size());
        return transformations.get(transformationIndex);
    }

    private boolean hasFiniteCoordinates(Point point) {
        return Double.isFinite(point.x()) && Double.isFinite(point.y());
    }

    private void ensureAtLeastOnePointWasDrawn(FractalImage canvas) {
        for (int pixelIndex = 0; pixelIndex < canvas.data().length; pixelIndex++) {
            if (canvas.data()[pixelIndex].hitCount() > 0) {
                return;
            }
        }

        throw new IllegalArgumentException(
                "Ни одна точка не попала в видимую область. "
                        + "Измените параметры преобразований или масштаб мира."
        );
    }

    private void recordPointHit(Point point, Magnifier magnifier, FractalImage canvas) {
        if (!magnifier.contains(point)) {
            return;
        }

        double leftBorder = magnifier.focusPoint().x() - magnifier.visibleWorldWidth() / 2;
        double bottomBorder = magnifier.focusPoint().y() - magnifier.visibleWorldHeight() / 2;

        double relativeHorizontalPosition = (point.x() - leftBorder) / magnifier.visibleWorldWidth();
        double relativeVerticalPosition = (point.y() - bottomBorder) / magnifier.visibleWorldHeight();

        int pixelX = (int) (relativeHorizontalPosition * canvas.width());
        int pixelY = canvas.height() - 1 - (int) (relativeVerticalPosition * canvas.height());

        if (canvas.contains(pixelX, pixelY)) {
            canvas.addHit(pixelX, pixelY);
        }
    }
}
