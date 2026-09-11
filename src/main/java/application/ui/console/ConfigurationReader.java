package application.ui.console;

import application.configuration.restriction.GenerationInputRestrictions;
import application.configuration.restriction.ImageSizeRestrictions;
import application.configuration.restriction.TransformationInputRestrictions;
import application.configuration.setting.GenerationSettings;
import application.ui.console.io.ConsolePanel;
import application.model.Space;
import application.rendering.renderer.Renderer;
import application.rendering.transformation.Transformation;
import application.configuration.GenerationConfiguration;
import application.configuration.catalog.Catalog;
import application.model.ImageSize;
import application.model.TransformationParameters;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import java.nio.file.Path;
import java.util.List;

@Component
public class ConfigurationReader {
    private final Catalog<Renderer> rendererCatalog;
    private final Catalog<ImageWriter> imageWriterCatalog;
    private final Catalog<Transformation> transformationCatalog;
    private final ConsolePanel consolePanel;
    private final ImageSizeRestrictions imageSizeRestrictions;
    private final GenerationInputRestrictions generationInputRestrictions;
    private final TransformationInputRestrictions transformationInputRestrictions;
    private final GenerationSettings generationSettings;

    public ConfigurationReader(Catalog<Renderer> rendererCatalog, Catalog<ImageWriter> imageWriterCatalog, Catalog<Transformation> transformationCatalog, ConsolePanel consolePanel, ImageSizeRestrictions imageSizeRestrictions, GenerationInputRestrictions generationInputRestrictions, TransformationInputRestrictions transformationInputRestrictions, GenerationSettings generationSettings) {
        this.rendererCatalog = rendererCatalog;
        this.imageWriterCatalog = imageWriterCatalog;
        this.transformationCatalog = transformationCatalog;
        this.consolePanel = consolePanel;
        this.imageSizeRestrictions = imageSizeRestrictions;
        this.generationInputRestrictions = generationInputRestrictions;
        this.transformationInputRestrictions = transformationInputRestrictions;
        this.generationSettings = generationSettings;
    }

    public GenerationConfiguration requestConfiguration() {
        Path outputPath = requestOutputPath();
        ImageSize imageSize = requestImageSize();
        ImageWriter imageWriter = requestImageWriter();
        Space space = requestSpace();
        int iterationCount = requestIterationCount();
        int randomSeed = requestRandomSeed();
        Renderer renderer = requestRenderer();
        TransformationParameters transformationParameters = requestTransformationParameters();
        List<Transformation> transformations = requestTransformationsList();

        return new GenerationConfiguration(outputPath, imageSize, imageWriter, space, iterationCount, randomSeed, renderer, transformationParameters, transformations);
    }

    private Path requestOutputPath() {
        String examplePath = Path.of(System.getProperty("user.home"), "Downloads").toString();
        return consolePanel.requestPath("Введите путь сохранения изображения", examplePath);
    }

    private ImageSize requestImageSize() {
        int width = consolePanel.requestInt("Введите ширину изображения", "1920", imageSizeRestrictions.width());
        int height = consolePanel.requestInt("Введите высоту изображения", "1280", imageSizeRestrictions.height());

        return new ImageSize(width, height);
    }

    private ImageWriter requestImageWriter() {
        List<String> formatNames = imageWriterCatalog.showCatalog();
        String selectedFormatName = consolePanel.requestOption("Выберите формат изображения", formatNames);

        return imageWriterCatalog.getAlgorithm(selectedFormatName);
    }

    private Space requestSpace() {
        int zoomPercentage = consolePanel.requestInt("Введите масштаб видимой области мира в процентах", "100", generationInputRestrictions.worldZoomPercentage());

        double zoomMultiplier = zoomPercentage / 100.0;
        double visibleWorldWidth = generationSettings.defaultVisibleWorldWidth() / zoomMultiplier;
        double visibleWorldHeight = generationSettings.defaultVisibleWorldHeight() / zoomMultiplier;

        return new Space(generationSettings.defaultWorldFocus(), visibleWorldWidth, visibleWorldHeight);
    }

    private Integer requestIterationCount() {
        int iterationCountInMillions = consolePanel.requestInt("Введите количество миллионов итераций", "20", generationInputRestrictions.iterationCountInMillions());
        return Math.multiplyExact(iterationCountInMillions, generationSettings.iterationsPerMillion());
    }

    private Integer requestRandomSeed() {
        return consolePanel.requestInt("Введите начальное значение генератора случайных чисел", "42", generationInputRestrictions.randomSeed());
    }

    private Renderer requestRenderer() {
        List<String> rendererNames = rendererCatalog.showCatalog();
        String selectedRendererName = consolePanel.requestOption("Выберите способ генерации изображения", rendererNames);

        return rendererCatalog.getAlgorithm(selectedRendererName);
    }

    private TransformationParameters requestTransformationParameters() {
        int scalePercentage = consolePanel.requestInt("Введите масштаб координат преобразований в процентах", "100 — без изменения", transformationInputRestrictions.scalePercentage());
        int rotationAngleInDegrees = consolePanel.requestInt("Введите угол поворота в градусах", "0", transformationInputRestrictions.rotationAngleInDegrees());
        int horizontalShiftInHundredths = consolePanel.requestInt("Введите горизонтальный сдвиг в сотых долях", "0", transformationInputRestrictions.horizontalShiftInHundredths());
        int verticalShiftInHundredths = consolePanel.requestInt("Введите вертикальный сдвиг в сотых долях", "0", transformationInputRestrictions.verticalShiftInHundredths());

        return new TransformationParameters(scalePercentage / 100.0, Math.toRadians(rotationAngleInDegrees), horizontalShiftInHundredths / 100.0, verticalShiftInHundredths / 100.0);
    }

    private List<Transformation> requestTransformationsList() {
        List<String> selectedTransformationNames = requestTransformationNames();

        return selectedTransformationNames.stream()
                .map(transformationCatalog::getAlgorithm)
                .toList();
    }

    private List<String> requestTransformationNames() {
        List<String> availableTransformationNames = transformationCatalog.showCatalog();
        String example = "1-" + availableTransformationNames.size() + " или 1 3 5 или 1, 3, 5";
        return consolePanel.requestOptions("Выберите одно или несколько преобразований", example, availableTransformationNames);
    }
}
