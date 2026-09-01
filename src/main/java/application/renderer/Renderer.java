package application.renderer;

import application.transformation.Transformation;
import application.world.FractalImage;
import application.world.Point;
import application.world.Rect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SplittableRandom;

@Component
public final class Renderer {
    private static final int BURN_IN = 20;

    //просто в зависимости от потоков
    public FractalImage render(
            FractalImage canvas,
            Rect world,
            List<Transformation> transformations,
            int iterationCount,
            long seed
    ) {
        if (transformations.isEmpty()) {
            throw new IllegalArgumentException("At least one transformation is required");
        }
        if (iterationCount <= 0) {
            throw new IllegalArgumentException("Iteration count must be positive");
        }

        SplittableRandom random = new SplittableRandom(seed);
        Point point = randomPoint(world, random);

        for (int step = 0; step < BURN_IN + iterationCount; step++) {
            Transformation selectedTransformation = transformations.get(
                    random.nextInt(transformations.size())
            );
            point = selectedTransformation.apply(point);

            if (step >= BURN_IN) {
                recordPointHit(point, world, canvas);
            }
        }

        return canvas;
    }

    private Point randomPoint(Rect world, SplittableRandom random) {
        return new Point(
                world.x() + random.nextDouble() * world.width(),
                world.y() + random.nextDouble() * world.height()
        );
    }

    private void recordPointHit(Point point, Rect world, FractalImage canvas) {
        if (!world.contains(point)) {
            return;
        }

        double relativeX = (point.x() - world.x()) / world.width();
        double relativeY = (point.y() - world.y()) / world.height();

        int pixelX = (int) (relativeX * canvas.width());
        int pixelY = canvas.height() - 1 - (int) (relativeY * canvas.height());

        if (canvas.contains(pixelX, pixelY)) {
            canvas.addHit(pixelX, pixelY);
        }
    }
}
