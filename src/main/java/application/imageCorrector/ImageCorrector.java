package application.imageCorrector;

import application.world.FractalImage;

@FunctionalInterface
public interface ImageCorrector {
    void process(FractalImage image);
}
