package application.userInterface.localConsole;

import application.parameters.ImageFormat;
import application.parameters.ImageSize;
import application.renderer.Renderer;
import application.transformation.Transformation;
import application.transformation.factory.ParameterDefinition;
import application.transformation.factory.TransformationFactory;
import application.userInterface.localConsole.catalog.Catalog;
import application.userInterface.localConsole.terminal.Terminal;
import application.world.Magnifier;
import application.world.Point;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Configurator {
    private final Catalog<Renderer> rendererCatalog;
    private final Catalog<ImageWriter> imageWriterCatalog;
    private final Catalog<TransformationFactory> transformationCatalog;
    private final Terminal terminal;

    public Configurator(Catalog<Renderer> rendererCatalog, Catalog<ImageWriter> imageWriterCatalog, Catalog<TransformationFactory> transformationCatalog, Terminal terminal) {
        this.rendererCatalog = rendererCatalog;
        this.imageWriterCatalog = imageWriterCatalog;
        this.transformationCatalog = transformationCatalog;
        this.terminal = terminal;
    }

    public Path requestOutputPath() {
        terminal.unformattedPrint("введи путь выгрузки");
        return terminal.getPath();
    }

    public ImageSize requestImageSize() {
        terminal.unformattedPrint("выберите размер изображения");
        int width = terminal.getUserInt(800, 8000);
        int height = terminal.getUserInt(800, 8000);
        return new ImageSize(width, height);
    }

    public ImageWriter requestImageWriter() {
        terminal.unformattedPrint("выберите формат изображения");
        terminal.printNumberedOptions(imageWriterCatalog.showCatalog());
        int userChoice = terminal.getUserInt(1, imageWriterCatalog.size());
        String imageWriterName = imageWriterCatalog.showCatalog().get(userChoice);
        return imageWriterCatalog.getAlgorithm(imageWriterName);
    }

    public Magnifier requestMagnifierZoom() {
        terminal.unformattedPrint("выберите масштаб мира");
        int zoomPercentage = terminal.getUserInt(50, 200);

        double defaultVisibleWorldSize = 2.0;
        double zoomMultiplier = zoomPercentage / 100.0;
        double visibleWorldWidth = defaultVisibleWorldSize / zoomMultiplier;
        double visibleWorldHeight = defaultVisibleWorldSize / zoomMultiplier;

        return new Magnifier(
                new Point(0, 0),
                visibleWorldWidth,
                visibleWorldHeight
        );
    }

    public Integer requestIterationCount() {
        terminal.unformattedPrint("Введите количество итераций");
        return terminal.getUserInt(100_000, 10_000_000);
    }

    public Integer requestRandomSeed() {
        terminal.unformattedPrint("Введите начальное значение генератора случайных чисел");
        return terminal.getUserInt(0, Integer.MAX_VALUE);
    }

    public Renderer requestRenderer() {
        terminal.unformattedPrint("Выберите способ генерации изображения");
        terminal.printNumberedOptions(rendererCatalog.showCatalog());

        int userChoice = terminal.getUserInt(1, rendererCatalog.size());
        String rendererName = rendererCatalog.showCatalog().get(userChoice - 1);

        return rendererCatalog.getAlgorithm(rendererName);
    }

    public List<Transformation> requestTransformationsList() {
        terminal.unformattedPrint("Введите количество трансформаций");

        int maximumTransformationCount = 10;
        int transformationCount = terminal.getUserInt(1, maximumTransformationCount);
        List<Transformation> selectedTransformations = new ArrayList<>(transformationCount);

        for (int transformationNumber = 1;
             transformationNumber <= transformationCount;
             transformationNumber++) {
            terminal.unformattedPrint(
                    "Выберите трансформацию №" + transformationNumber
            );
            terminal.printNumberedOptions(transformationCatalog.showCatalog());

            int userChoice = terminal.getUserInt(1, transformationCatalog.size());
            String transformationName = transformationCatalog
                    .showCatalog()
                    .get(userChoice - 1);

            TransformationFactory transformationFactory = transformationCatalog
                    .getAlgorithm(transformationName);

            Map<String, Double> parameterValues = new LinkedHashMap<>();

            for (ParameterDefinition parameter : transformationFactory.getRequiredParameters()) {
                terminal.unformattedPrint(parameter.requestMessage());

                int inputValue = terminal.getUserInt(
                        parameter.minimumInputValue(),
                        parameter.maximumInputValue()
                );

                parameterValues.put(
                        parameter.name(),
                        parameter.convertInputValue(inputValue)
                );
            }

            selectedTransformations.add(
                    transformationFactory.create(parameterValues)
            );
        }

        return List.copyOf(selectedTransformations);
    }
}
