package application.userInterface;



import application.parameters.ImageFormat;
import application.world.FractalImage;
import application.world.Pixel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

@Component
public class ImageFileWriter {
    public ImageFileWriter() {
    }

    public Path write(FractalImage image, Path path, ImageFormat imageFormat) {
        Path absoluteOutputPath = path.toAbsolutePath();
        save(image, absoluteOutputPath, imageFormat);
        return absoluteOutputPath;
    }

    public static void save(FractalImage image, Path filename, ImageFormat format) {
        BufferedImage output = new BufferedImage(
                image.width(),
                image.height(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                int rgb = (pixel.red() << 16) | (pixel.green() << 8) | pixel.blue();
                output.setRGB(x, y, rgb);
            }
        }

        try {
            boolean written = ImageIO.write(output, format.name().toLowerCase(), filename.toFile());
            if (!written) {
                throw new IllegalArgumentException("Unsupported image format: " + format);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to save image to " + filename, exception);
        }
    }
}
