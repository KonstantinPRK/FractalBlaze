package application.ui.console;

import application.configuration.restriction.GenerationInputRestrictions;
import application.configuration.restriction.TransformationInputRestrictions;
import application.configuration.setting.GenerationSettings;
import application.configuration.GenerationConfiguration;
import application.configuration.catalog.Catalog;
import application.execution.ExecutionMode;
import application.model.AspectRatio;
import application.model.ImageQuality;
import application.model.ImageShape;
import application.model.ImageSize;
import application.model.Space;
import application.model.TransformationParameters;
import application.rendering.renderer.Renderer;
import application.rendering.transformation.Transformation;
import application.ui.console.io.ConsolePanel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@Component
public class ConfigurationReader {
    private final ConsolePanel consolePanel;

    private final Catalog<ImageQuality> imageQualityCatalog;
    private final Catalog<ImageShape> imageShapeCatalog;
    private final Catalog<AspectRatio> aspectRatioCatalog;
    private final Catalog<ImageWriter> imageWriterCatalog;
    private final Catalog<Renderer> rendererCatalog;
    private final Catalog<Transformation> transformationCatalog;

    private final GenerationInputRestrictions generationInputRestrictions;
    private final TransformationInputRestrictions transformationInputRestrictions;
    private final GenerationSettings generationSettings;


    public ConfigurationReader(
            Catalog<ImageQuality> imageQualityCatalog,
            Catalog<ImageShape> imageShapeCatalog,
            Catalog<AspectRatio> aspectRatioCatalog,
            Catalog<ImageWriter> imageWriterCatalog,
            Catalog<Renderer> rendererCatalog,
            Catalog<Transformation> transformationCatalog,

            ConsolePanel consolePanel,

            GenerationInputRestrictions generationInputRestrictions,
            TransformationInputRestrictions transformationInputRestrictions,
            GenerationSettings generationSettings)
    {
        this.imageQualityCatalog = imageQualityCatalog;
        this.imageShapeCatalog = imageShapeCatalog;
        this.aspectRatioCatalog = aspectRatioCatalog;
        this.imageWriterCatalog = imageWriterCatalog;
        this.rendererCatalog = rendererCatalog;
        this.transformationCatalog = transformationCatalog;

        this.consolePanel = consolePanel;

        this.generationInputRestrictions = generationInputRestrictions;
        this.transformationInputRestrictions = transformationInputRestrictions;
        this.generationSettings = generationSettings;
    }

    public GenerationConfiguration requestConfiguration() {
        ImageSize imageSize = requestImageSize();
        ImageWriter imageWriter = requestImageWriter();
        Path outputPath = requestOutputPath();
        Renderer renderer = requestRenderer();
        ExecutionMode executionMode = requestExecutionMode();
        Space space = requestSpace();
        int iterationCount = requestIterationCount();
        int randomSeed = requestRandomSeed();
        TransformationParameters transformationParameters = requestTransformationParameters();
        List<Transformation> transformations = requestTransformationsList();

        return new GenerationConfiguration(
                outputPath,
                imageSize,
                imageWriter,
                renderer,
                executionMode,
                space,
                iterationCount,
                randomSeed,
                transformationParameters,
                transformations
        );
    }

    private Path requestOutputPath() {
        String examplePath = Path.of(System.getProperty("user.home"), "Downloads").toString();
        return consolePanel.requestPath("Введите путь сохранения изображения", examplePath);
    }

    private ImageSize requestImageSize() {
        ImageQuality quality = imageQualityCatalog.getAlgorithm(
                consolePanel.requestOption(
                        "Выберите качество изображения",
                        imageQualityCatalog.showCatalog()));

        ImageShape shape = imageShapeCatalog.getAlgorithm(
                consolePanel.requestOption(
                        "Выберите ориентацию изображения",
                        imageShapeCatalog.showCatalog()));

        AspectRatio aspectRatio = aspectRatioCatalog.getAlgorithm(
                consolePanel.requestOption(
                        "Выберите соотношение сторон",
                        aspectRatioCatalog.showCatalog()));

        return ImageSize.calculate(quality, shape, aspectRatio);
    }

    private ImageWriter requestImageWriter() {
        List<String> formatNames = imageWriterCatalog.showCatalog();
        String selectedFormatName = consolePanel.requestOption("Выберите формат файла", formatNames);

        return imageWriterCatalog.getAlgorithm(selectedFormatName);
    }

    private Renderer requestRenderer() {
        List<String> rendererNames = rendererCatalog.showCatalog();
        String selectedRendererName = consolePanel.requestOption("Выберите алгоритм генерации изображения", rendererNames);

        return rendererCatalog.getAlgorithm(selectedRendererName);
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

    private ExecutionMode requestExecutionMode() {
        List<ExecutionMode> executionModes = List.of(ExecutionMode.values());
        List<String> modeNames = executionModes.stream().map(ExecutionMode::getDisplayName).toList();
        String selectedModeName = consolePanel.requestOption("Выберите режим выполнения", modeNames);

        return executionModes.stream()
                .filter(executionMode -> executionMode.getDisplayName().equals(selectedModeName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Не удалось определить выбранный режим выполнения"));
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
