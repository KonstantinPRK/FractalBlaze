package application.configuration.systemConfiguration.springConfig;

import application.configuration.userConfiguration.userParameterRecords.ImageFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.io.PrintStream;
import java.util.Scanner;

@Configuration
public class InitializationConfig {
    @Bean(destroyMethod = "")
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean(destroyMethod = "")
    public PrintStream printer() {
        return System.out;
    }

    @Bean(name = "JPEG", destroyMethod = "dispose")
    public ImageWriter jpegImageWriter() {
        return getImageWriter(ImageFormat.JPEG);
    }

    @Bean(name = "BMP", destroyMethod = "dispose")
    public ImageWriter bmpImageWriter() {
        return getImageWriter(ImageFormat.BMP);
    }

    @Bean(name = "PNG", destroyMethod = "dispose")
    public ImageWriter pngImageWriter() {
        return getImageWriter(ImageFormat.PNG);
    }

    private ImageWriter getImageWriter(ImageFormat imageFormat) {
        return ImageIO.getImageWritersByFormatName(imageFormat.name()).next();
    }
}
