package application.renderer;

import application.transformation.Transformation;
import application.world.FractalImage;
import application.world.Magnifier;

import java.util.List;

public interface Renderer {
    FractalImage render(FractalImage emptyCanvas, Magnifier magnifier, List<Transformation> transformations, Integer integer, Integer randomSeed);
    String getName();
}
