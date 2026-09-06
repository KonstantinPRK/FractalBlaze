package application.imageGeneration.imageCorrector;

import application.picture.FractalImage;
import application.picture.Pixel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public final class LogDensityProcessor implements ImageCorrector {
    @Override
    public void process(FractalImage image) {
        int maxHitCount = 0;

        for (Pixel pixel : image.data()) {
            maxHitCount = Math.max(maxHitCount, pixel.hitCount());
        }

        if (maxHitCount == 0)return;

        double maxDensity = Math.log1p(maxHitCount);

        for (int pixelY = 0; pixelY < image.height(); pixelY++) {
            for (int pixelX = 0; pixelX < image.width(); pixelX++) {
                Pixel currentPixel = image.pixel(pixelX, pixelY);

                if (currentPixel.hitCount() == 0)continue;

                double relativeDensity = Math.log1p(currentPixel.hitCount()) / maxDensity;

                int correctedRed;
                int correctedGreen;
                int correctedBlue;

                if (hasNoColor(currentPixel)) {
                    int brightness = scaleColorComponent(255, relativeDensity);
                    correctedRed = brightness;
                    correctedGreen = brightness;
                    correctedBlue = brightness;
                } else {
                    correctedRed = scaleColorComponent(currentPixel.red(), relativeDensity);
                    correctedGreen = scaleColorComponent(currentPixel.green(), relativeDensity);
                    correctedBlue = scaleColorComponent(currentPixel.blue(), relativeDensity);
                }

                image.setPixel(pixelX, pixelY, new Pixel(correctedRed, correctedGreen, correctedBlue, currentPixel.hitCount()));
            }
        }
    }

    private boolean hasNoColor(Pixel pixel) {
        return pixel.red() == 0 && pixel.green() == 0 && pixel.blue() == 0;
    }

    private int scaleColorComponent(int colorComponent, double relativeDensity) {
        return (int) Math.round(colorComponent * relativeDensity);
    }
}
