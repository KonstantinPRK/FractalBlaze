package application;



import application.parameters.ImageFormat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class ImageFileWriter {
    private ImageFileWriter() {
    }

    public Path write(FractalImage image, Path path, ImageFormat imageFormat){

        return path;
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
                int rgb = (pixel.r() << 16) | (pixel.g() << 8) | pixel.b();
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
