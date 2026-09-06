package application.localConsoleUI.readerRecorder;

import application.userConfiguration.Configuration;
import application.userConfiguration.parameters.ImageSize;
import application.renderer.Renderer;
import application.transformation.Transformation;
import application.userConfiguration.parameters.TransformationParameters;
import application.userConfiguration.algorithmsCatalog.Catalog;
import application.picture.Point;
import application.picture.Space;
import application.localConsoleUI.terminal.ConsolePanel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConfigurationReader {
    private final Catalog<Renderer> rendererCatalog;
    private final Catalog<ImageWriter> imageWriterCatalog;
    private final Catalog<Transformation> transformationCatalog;
    private final ConsolePanel consolePanel;

    public ConfigurationReader(Catalog<Renderer> rendererCatalog, Catalog<ImageWriter> imageWriterCatalog, Catalog<Transformation> transformationCatalog, ConsolePanel consolePanel) {
        this.rendererCatalog = rendererCatalog;
        this.imageWriterCatalog = imageWriterCatalog;
        this.transformationCatalog = transformationCatalog;
        this.consolePanel = consolePanel;
    }

    public Configuration requestConfiguration() {
        Path outputPath = requestOutputPath();
        ImageSize imageSize = requestImageSize();
        ImageWriter imageWriter = requestImageWriter();
        Space space = requestSpace();
        int iterationCount = requestIterationCount();
        int randomSeed = requestRandomSeed();
        Renderer renderer = requestRenderer();
        TransformationParameters transformationParameters = requestTransformationParameters();
        List<Transformation> transformations = requestTransformationsList();

        return new Configuration(outputPath, imageSize, imageWriter, space, iterationCount, randomSeed, renderer, transformationParameters, transformations);
    }


    private Path requestOutputPath() {
        while (true) {
            try {
                consolePanel.printText("Введите путь получения файла: ");
                consolePanel.printText("пример: " + Path.of(System.getProperty("user.home"), "Downloads"));

                String outputPath = consolePanel.getUserString();
                return Path.of(outputPath).toAbsolutePath().normalize();
            } catch (InvalidPathException exception) {
                consolePanel.printText("Не удалось распознать путь, попробуйте снова. ");
            }
        }
    }



    private ImageSize requestImageSize() {
        consolePanel.printText("Определите размер изображения. ");

        consolePanel.printText("Введите ширину: ");
        consolePanel.printText("пример: 1920");
        int width = consolePanel.getUserInt(ImageSize.minSize(), ImageSize.maxSize());

        consolePanel.printText("Введите высоту: ");
        consolePanel.printText("пример: 1280");
        int height = consolePanel.getUserInt(ImageSize.minSize(), ImageSize.maxSize());

        return new ImageSize(width, height);
    }


    private ImageWriter requestImageWriter() {
        consolePanel.printText("Выберите формат изображения: ");

        List<String> formatNames = imageWriterCatalog.showCatalog();
        consolePanel.printNumberedOptions(formatNames);

        int selectedOption = consolePanel.getUserInt(1, formatNames.size());
        String selectedFormatName = formatNames.get(selectedOption - 1);

        return imageWriterCatalog.getAlgorithm(selectedFormatName);
    }


    private Space requestSpace() {
        consolePanel.printText("Выберите масштаб мира в %");
        int zoomPercentage = consolePanel.getUserInt(50, 200);

        double defaultVisibleWorldSize = 2.0;
        double zoomMultiplier = zoomPercentage / 100.0;
        double visibleWorldWidth = defaultVisibleWorldSize / zoomMultiplier;
        double visibleWorldHeight = defaultVisibleWorldSize / zoomMultiplier;

        return new Space(new Point(0, 0), visibleWorldWidth, visibleWorldHeight);
    }



    private Integer requestIterationCount() {
        consolePanel.printText("Введите количество миллионов итераций: ");
        int iterationCountInMillions = consolePanel.getUserInt(10, 50);
        return iterationCountInMillions * 1_000_000;
    }

    private Integer requestRandomSeed() {
        consolePanel.printText("Введите начальное значение генератора случайных чисел: ");
        return consolePanel.getUserInt(0, 100);
    }

    private Renderer requestRenderer() {
        consolePanel.printText("Выберите способ генерации изображения: ");

        List<String> rendererNames = rendererCatalog.showCatalog();
        consolePanel.printNumberedOptions(rendererNames);

        int selectedOption = consolePanel.getUserInt(1, rendererNames.size());
        String selectedRendererName = rendererNames.get(selectedOption - 1);

        return rendererCatalog.getAlgorithm(selectedRendererName);
    }


    private TransformationParameters requestTransformationParameters() {
        consolePanel.printText("Введите общие параметры для всех преобразований. ");

        consolePanel.printText("Введите масштаб координат преобразований в процентах (100 — без изменения): ");
        int scalePercentage = consolePanel.getUserInt(10, 200);

        consolePanel.printText("Введите угол поворота в градусах: ");
        int rotationAngleInDegrees = consolePanel.getUserInt(-180, 180);

        consolePanel.printText("Введите горизонтальный сдвиг в сотых долях: ");
        int horizontalShiftInHundredths = consolePanel.getUserInt(-200, 200);

        consolePanel.printText("Введите вертикальный сдвиг в сотых долях: ");
        int verticalShiftInHundredths = consolePanel.getUserInt(-200, 200);

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

        consolePanel.printText("Выберите одно или несколько преобразований: ");
        consolePanel.printNumberedOptions(availableTransformationNames);
        int[] selectedOptionNumbers = consolePanel.getUserIntArray(1, availableTransformationNames.size());

        List<String> selectedTransformationNames = new ArrayList<>(selectedOptionNumbers.length);
        for (int selectedOptionNumber : selectedOptionNumbers) selectedTransformationNames.add(availableTransformationNames.get(selectedOptionNumber - 1));

        return List.copyOf(selectedTransformationNames);
    }

}
