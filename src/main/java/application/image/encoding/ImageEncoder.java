package application.image.encoding;

import application.model.FractalImage;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Objects;

@Component
public final class ImageEncoder {
    public void encode(FractalImage fractalImage, ImageWriter selectedImageWriter, Path outputFile) {
        BufferedImage bufferedImage = convertToBufferedImage(fractalImage);
        encodeImage(bufferedImage, selectedImageWriter, outputFile);
    }

    private BufferedImage convertToBufferedImage(FractalImage fractalImage) {
        BufferedImage bufferedImage = new BufferedImage(fractalImage.width(), fractalImage.height(), BufferedImage.TYPE_INT_RGB);
        int[] bufferedImageRgb = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        fractalImage.copyRgbTo(bufferedImageRgb);

        return bufferedImage;
    }

    private void encodeImage(BufferedImage bufferedImage, ImageWriter selectedImageWriter, Path outputFile) {
        try (ImageOutputStream imageOutputStream = new FileImageOutputStream(outputFile.toFile())) {
            selectedImageWriter.setOutput(imageOutputStream);
            selectedImageWriter.write(bufferedImage);
            imageOutputStream.flush();

        } catch (IOException exception) {
            throw new UncheckedIOException("Не удалось создать файл изображения: " + outputFile, exception);

        } finally {
            selectedImageWriter.reset();

        }
    }
}
