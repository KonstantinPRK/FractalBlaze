package application.imageCorrector;

import application.world.FractalImage;
import application.world.Pixel;
import org.springframework.stereotype.Component;

@Component
public final class LogDensityProcessor implements ImageCorrector {
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

        for (int pixelY = 0; pixelY < image.height(); pixelY++) {
            for (int pixelX = 0; pixelX < image.width(); pixelX++) {
                Pixel currentPixel = image.pixel(pixelX, pixelY);

                if (currentPixel.hitCount() == 0) {
                    continue;
                }

                int brightness = (int) Math.round(
                        255.0 * Math.log1p(currentPixel.hitCount()) / maxDensity
                );

                image.setPixel(
                        pixelX,
                        pixelY,
                        new Pixel(
                                brightness,
                                brightness,
                                brightness,
                                currentPixel.hitCount()
                        )
                );
            }
        }
    }
}
