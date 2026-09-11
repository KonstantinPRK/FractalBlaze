package application.image.encoding;

import application.model.FractalImage;
import application.model.Pixel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Objects;

@Component
public final class ImageEncoder {
    public ImageFile encode(FractalImage fractalImage, ImageWriter selectedImageWriter) {
        Objects.requireNonNull(fractalImage, "Фрактальное изображение не должно быть null");
        Objects.requireNonNull(selectedImageWriter, "Средство записи изображения не должно быть null");

        BufferedImage bufferedImage = convertToBufferedImage(fractalImage);
        byte[] imageFileContent = encodeImage(bufferedImage, selectedImageWriter);
        String fileExtension = selectedImageWriter.getOriginatingProvider().getFileSuffixes()[0].toLowerCase(Locale.ROOT);

        return new ImageFile(imageFileContent, fileExtension);
    }

    private BufferedImage convertToBufferedImage(FractalImage fractalImage) {
        BufferedImage bufferedImage = new BufferedImage(fractalImage.width(), fractalImage.height(), BufferedImage.TYPE_INT_RGB);

        for (int pixelY = 0; pixelY < fractalImage.height(); pixelY++) {
            for (int pixelX = 0; pixelX < fractalImage.width(); pixelX++) {
                Pixel pixel = fractalImage.pixel(pixelX, pixelY);
                bufferedImage.setRGB(pixelX, pixelY, convertToRgb(pixel));
            }
        }

        return bufferedImage;
    }

    private int convertToRgb(Pixel pixel) {
        int red = limitColorValue(pixel.red());
        int green = limitColorValue(pixel.green());
        int blue = limitColorValue(pixel.blue());

        return (red << 16) | (green << 8) | blue;
    }

    private int limitColorValue(int colorValue) {
        return Math.max(0, Math.min(255, colorValue));
    }

    private byte[] encodeImage(BufferedImage bufferedImage, ImageWriter selectedImageWriter) {
        try (ByteArrayOutputStream imageBytes = new ByteArrayOutputStream(); ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(imageBytes)) {
            selectedImageWriter.setOutput(imageOutputStream);
            selectedImageWriter.write(bufferedImage);
            imageOutputStream.flush();

            return imageBytes.toByteArray();
        } catch (IOException exception) {
            throw new UncheckedIOException("Не удалось создать файл изображения", exception);
        } finally {
            selectedImageWriter.reset();
        }
    }
}
