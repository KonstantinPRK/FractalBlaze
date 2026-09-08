package application.rendering.renderer;

import application.configuration.systemConfiguration.settingsRecords.TrajectorySettings;
import application.configuration.userConfiguration.userParameterRecords.TransformationParameters;
import application.picture.FractalImage;
import application.picture.Space;
import application.rendering.ColorPalette;
import application.rendering.TrajectoryDrawer;
import application.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SplittableRandom;

@Component
public class SingleThreadRenderer implements Renderer {
    private final int ITERATIONS_PER_TRAJECTORY;
    private final ColorPalette colorPalette;
    private final TrajectoryDrawer trajectoryDrawer;

    public SingleThreadRenderer(TrajectorySettings settings, ColorPalette colorPalette, TrajectoryDrawer trajectoryDrawer) {
        ITERATIONS_PER_TRAJECTORY = settings.iterationsPerTrajectory();
        this.colorPalette = colorPalette;
        this.trajectoryDrawer = trajectoryDrawer;
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

        boolean atLeastOnePointWasRecorded = drawAllTrajectories(
                canvas,
                space,
                transformationParameters,
                transformations,
                transformationColors,
                userIterationCount,
                cosOfRotation,
                sinOfRotation,
                random);

        if (!atLeastOnePointWasRecorded)throw new IllegalArgumentException("Ни одна точка не попала в видимую область. Измените параметры преобразований или масштаб мира.");

        return canvas;
    }

    private boolean drawAllTrajectories(
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
        boolean atLeastOnePointWasRecorded = false;

        for (int iterationSum = 0; iterationSum < userIterationCount; iterationSum += ITERATIONS_PER_TRAJECTORY) {
            int iterationsInCurrentTrajectory = Math.min(ITERATIONS_PER_TRAJECTORY, userIterationCount - iterationSum);

            boolean trajectoryRecordedAtLeastOnePoint = trajectoryDrawer.drawTrajectory(
                    canvas,
                    space,
                    transformationParameters,
                    transformations,
                    transformationColors,
                    iterationsInCurrentTrajectory,
                    cosOfRotation,
                    sinOfRotation,
                    random);

            if (trajectoryRecordedAtLeastOnePoint)atLeastOnePointWasRecorded = true;
        }

        return atLeastOnePointWasRecorded;
    }
}
