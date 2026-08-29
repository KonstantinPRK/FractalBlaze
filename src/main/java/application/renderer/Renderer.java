package application.renderer;

import application.FractalImage;
import application.Rect;
import application.transformation.Transformation;

import java.util.List;

public interface Renderer {
    FractalImage render(
            FractalImage canvas,
            Rect world,
            List<Transformation> variations,
            int samples,
            short iterPerSample,
            long seed
    );
}
