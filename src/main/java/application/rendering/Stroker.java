package application.rendering;

import application.configuration.systemConfiguration.settingsRecords.TrajectorySettings;
import org.springframework.stereotype.Component;

import java.util.SplittableRandom;
import java.util.concurrent.CancellationException;

@Component
public class Stroker {
    private final int ITERATIONS_PER_TRAJECTORY;
    private final TrajectoryDrawer trajectoryDrawer;

    public Stroker(TrajectorySettings settings, TrajectoryDrawer trajectoryDrawer) {
        ITERATIONS_PER_TRAJECTORY = settings.iterationsPerTrajectory();
        this.trajectoryDrawer = trajectoryDrawer;
    }

    public int calculateTrajectoryCount(RenderingContext context) {
        int completedTrajectoryCount = context.iterationCount() / ITERATIONS_PER_TRAJECTORY;
        boolean hasIncompleteTrajectory = context.iterationCount() % ITERATIONS_PER_TRAJECTORY != 0;

        return completedTrajectoryCount + (hasIncompleteTrajectory ? 1 : 0);
    }

    public Layer spray(RenderingContext context, int firstTrajectoryIndex, int endTrajectoryIndex) {
        Layer layer = new Layer(context.imageWidth(), context.imageHeight());

        for (int trajectoryIndex = firstTrajectoryIndex; trajectoryIndex < endTrajectoryIndex; trajectoryIndex++) {
            if (Thread.currentThread().isInterrupted())throw new CancellationException("Отрисовка прервана");

            int firstIteration = trajectoryIndex * ITERATIONS_PER_TRAJECTORY;
            int remainingIterationCount = context.iterationCount() - firstIteration;
            int currentTrajectoryIterationCount = Math.min(ITERATIONS_PER_TRAJECTORY, remainingIterationCount);

            SplittableRandom trajectoryRandom = new SplittableRandom(context.randomSeed() + trajectoryIndex);
            trajectoryDrawer.drawTrajectory(layer, context, currentTrajectoryIterationCount, trajectoryRandom);
        }

        return layer;
    }
}
