package application.rendering;

import application.configuration.setting.ColorSettings;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.util.SplittableRandom;

@Component
public class ColorPalette {
    private final float COLOR_SATURATION;
    private final float COLOR_BRIGHTNESS;

    public ColorPalette(ColorSettings settings) {
        COLOR_SATURATION = settings.saturation();
        COLOR_BRIGHTNESS = settings.brightness();
    }

    public int[] setColorForTransformations(int transformationCount, SplittableRandom random) {
        int[] transformationColors = new int[transformationCount];
        double startingHue = random.nextDouble();

        for (int transformationIndex = 0; transformationIndex < transformationCount; transformationIndex++) {
            float hue = (float) (startingHue + (double) transformationIndex / transformationCount);
            Color color = Color.getHSBColor(hue % 1.0f, COLOR_SATURATION, COLOR_BRIGHTNESS);
            transformationColors[transformationIndex] = color.getRGB() & 0x00FFFFFF;
        }

        return transformationColors;
    }

    public int mixColors(int firstColor, int secondColor) {
        int mixedRed = (red(firstColor) + red(secondColor)) / 2;
        int mixedGreen = (green(firstColor) + green(secondColor)) / 2;
        int mixedBlue = (blue(firstColor) + blue(secondColor)) / 2;

        return mixedRed << 16 | mixedGreen << 8 | mixedBlue;
    }

    private int red(int color) {
        return color >> 16 & 0xFF;
    }

    private int green(int color) {
        return color >> 8 & 0xFF;
    }

    private int blue(int color) {
        return color & 0xFF;
    }
}
