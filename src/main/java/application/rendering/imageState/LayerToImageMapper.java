package application.rendering.imageState;

import application.model.FractalImage;
import application.model.Pixel;
import application.rendering.Layer;
import org.springframework.stereotype.Component;

@Component
public final class LayerToImageMapper {
    public void mapLine(Layer layer, FractalImage image, int lineIndex) {
        int firstPixelIndex = lineIndex * layer.width();
        int endPixelIndex = firstPixelIndex + layer.width();

        for (int pixelIndex = firstPixelIndex; pixelIndex < endPixelIndex; pixelIndex++) mapPixel(layer, image, pixelIndex);
    }

    private void mapPixel(Layer layer, FractalImage image, int pixelIndex) {
        int hitCount = layer.hitCount(pixelIndex);
        if (hitCount == 0)return;

        int red = (int) (layer.redSum(pixelIndex) / hitCount);
        int green = (int) (layer.greenSum(pixelIndex) / hitCount);
        int blue = (int) (layer.blueSum(pixelIndex) / hitCount);
        image.data()[pixelIndex] = new Pixel(red, green, blue, hitCount);
    }
}
