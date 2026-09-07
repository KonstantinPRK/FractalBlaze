package application.core;

import application.core.restrictions.GenerationInputRestrictions;
import application.core.restrictions.ImageSizeRestrictions;
import application.core.restrictions.IntegerRange;
import application.core.restrictions.TransformationInputRestrictions;
import application.core.settings.ConsoleLayoutSettings;
import application.core.settings.GammaCorrectionSettings;
import application.core.settings.GenerationSettings;
import application.core.settings.OutputFileSettings;
import application.core.settings.SingleThreadRendererSettings;
import application.core.settings.SmoothingSettings;
import application.core.settings.TransformationCalculationSettings;
import application.picture.Point;
import application.userConfiguration.parameters.ImageFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.io.PrintStream;
import java.util.Scanner;

@Configuration
public class AppConfig {
    @Bean
    public ImageSizeRestrictions imageSizeRestrictions() {
        return new ImageSizeRestrictions(new IntegerRange(1280, 1920), new IntegerRange(1280, 1920));
    }

    @Bean
    public GenerationInputRestrictions generationInputRestrictions() {
        return new GenerationInputRestrictions(new IntegerRange(50, 200), new IntegerRange(10, 50), new IntegerRange(0, 100));
    }

    @Bean
    public TransformationInputRestrictions transformationInputRestrictions() {
        return new TransformationInputRestrictions(new IntegerRange(10, 200), new IntegerRange(-180, 180), new IntegerRange(-200, 200), new IntegerRange(-200, 200));
    }

    @Bean
    public ConsoleLayoutSettings consoleLayoutSettings() {
        return new ConsoleLayoutSettings(2, "Пример: ", "Допустимый диапазон: ", "> ");
    }

    @Bean
    public GenerationSettings generationSettings() {
        return new GenerationSettings(new Point(0.0, 0.0), 2.0, 2.0, 1_000_000);
    }

    @Bean
    public SingleThreadRendererSettings singleThreadRendererSettings() {
        return new SingleThreadRendererSettings(20, 10_000, 0.85f, 1.0f);
    }

    @Bean
    public TransformationCalculationSettings transformationCalculationSettings() {
        return new TransformationCalculationSettings(1.0e-6);
    }

    @Bean
    public GammaCorrectionSettings gammaCorrectionSettings() {
        return new GammaCorrectionSettings(2.2);
    }

    @Bean
    public SmoothingSettings smoothingSettings() {
        return new SmoothingSettings(1);
    }

    @Bean
    public OutputFileSettings outputFileSettings() {
        return new OutputFileSettings("fractal");
    }

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
