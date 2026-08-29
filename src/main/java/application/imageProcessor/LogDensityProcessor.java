package application.imageProcessor;

import application.FractalImage;
import application.Pixel;

public final class LogDensityProcessor implements ImageProcessor {
    @Override
    public void process(FractalImage image) {
        int maxHitCount = 0;

        for (Pixel pixel : image.data()) {
            maxHitCount = Math.max(maxHitCount, pixel.hitCount());
        }

        if (maxHitCount == 0) {
            return;
        }

        double maxDensity = Math.log1p(maxHitCount);

        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                int brightness = (int) Math.round(
                        255.0 * Math.log1p(pixel.hitCount()) / maxDensity
                );

                image.setPixel(
                        x,
                        y,
                        new Pixel(brightness, brightness, brightness, pixel.hitCount())
                );
            }
        }
    }
}
