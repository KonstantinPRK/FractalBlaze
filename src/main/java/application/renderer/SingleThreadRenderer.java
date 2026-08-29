package application.renderer;

import application.*;
import application.transformation.Transformation;

import java.util.List;
import java.util.SplittableRandom;

public final class SingleThreadRenderer implements Renderer {
    private static final int BURN_IN = 20;

    @Override
    public FractalImage render(
            FractalImage canvas,
            Rect world,
            List<Transformation> variations,
            int samples,
            short iterPerSample,
            long seed
    ) {
        if (variations.isEmpty()) {
            throw new IllegalArgumentException("At least one transformation is required");
        }
        if (samples <= 0 || iterPerSample <= 0) {
            throw new IllegalArgumentException("Iteration counts must be positive");
        }

        SplittableRandom random = new SplittableRandom(seed);

        for (int sample = 0; sample < samples; sample++) {
            Point point = randomPoint(world, random);

            for (int step = 0; step < BURN_IN + iterPerSample; step++) {
                Transformation transformation = variations.get(random.nextInt(variations.size()));
                point = transformation.apply(point);

                if (step >= BURN_IN) {
                    plot(point, world, canvas);
                }
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

    private void plot(Point point, Rect world, FractalImage canvas) {
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
