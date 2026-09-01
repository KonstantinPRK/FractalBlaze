package application.userInterface.localConsole;

import application.parameters.Configuration;
import application.parameters.ImageFormat;
import application.parameters.ImageSize;
import application.renderer.Renderer;
import application.transformation.Transformation;
import application.userInterface.UserInterface;
import application.world.Magnifier;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class LocalConsole implements UserInterface {
    Configurator configurator;
    Responder responder;

    @Override
    public Configuration requestConfiguration() {
        Path outputPath = configurator.requestOutputPath();
        ImageSize imageSize = configurator.requestImageSize();
        ImageFormat imageFormat = configurator.requestImageFormat();
        Magnifier magnifier = configurator.requestMagnifierZoom();
        Integer iterationCount = configurator.requestIterationCount();
        Long randomSeed = configurator.requestRandomSeed();
        Renderer renderer = configurator.requestRenderer();
        List<Transformation> transformations = configurator.requestTransformationsList();

        return new Configuration(
                outputPath,
                imageSize,
                imageFormat,
                magnifier,
                iterationCount,
                randomSeed,
                renderer,
                transformations
        );
    }

    @Override
    public void returnFractalImage(Path file) {
        System.out.println("Fractal image saved to: " + file);

        if (!Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            return;
        }

        try {
            Desktop.getDesktop().open(file.toFile());
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to open image: " + file, exception);
        }
    }
}
