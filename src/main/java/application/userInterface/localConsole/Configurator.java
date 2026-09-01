package application.userInterface.localConsole;

import application.parameters.ImageFormat;
import application.parameters.ImageSize;
import application.renderer.Renderer;
import application.transformation.Transformation;
import application.userInterface.localConsole.terminal.Terminal;
import application.world.Magnifier;

import java.nio.file.Path;
import java.util.List;

public class Configurator {
    Terminal terminal;

    public Path requestOutputPath() {

    }

    public ImageSize requestImageSize() {
    }

    public ImageFormat requestImageFormat() {
    }

    public Magnifier requestMagnifierZoom() {
    }

    public Integer requestIterationCount() {
    }

    public Long requestRandomSeed() {
    }

    public Renderer requestRenderer() {
    }

    public List<Transformation> requestTransformationsList() {
        return null;
    }
}
