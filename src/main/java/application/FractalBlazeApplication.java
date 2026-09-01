package application;

import application.imageCorrector.ImageCorrector;
import application.parameters.Configuration;
import application.renderer.Renderer;
import application.userInterface.ImageFileWriter;
import application.userInterface.UserInterface;
import application.world.FractalImage;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
public final class FractalBlazeApplication {
    private final UserInterface userInterface;
    private final List<ImageCorrector> imageCorrectors;
    private final ImageFileWriter imageFileWriter;

    public FractalBlazeApplication(
            UserInterface userInterface,
            List<Renderer> availableRenderers,
            List<ImageCorrector> imageCorrectors,
            ImageFileWriter imageFileWriter
    ) {
        this.userInterface = userInterface;
        this.imageCorrectors = List.copyOf(imageCorrectors);
        this.imageFileWriter = imageFileWriter;
    }


    @PostConstruct
    public void start() {
        // 1. Получаем все параметры будущего изображения.
        Configuration configuration = userInterface.requestConfiguration();

        // 2. Выбираем однопоточный или многопоточный renderer. // проще просто дать ему количество потоков а дальше он сам, то есть пока в едином экземпляре без интерфейса
        Renderer selectedRenderer = availableRenderers.stream()
                .filter(renderer -> renderer.supports(configuration.threadCount()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Renderer not found for thread count: " + configuration.threadCount()
                ));

        // 3. Создаём пустой холст требуемого размера.
        FractalImage emptyCanvas = FractalImage.create(configuration.imageSize());

        // 4. Генерируем попадания точек в пиксели холста.
        FractalImage renderedImage = selectedRenderer.render(
                emptyCanvas,
                configuration.world(),
                configuration.transformations(),
                configuration.iterationCount(),
                configuration.seed()
        );

        // 5. Последовательно выполняем подключённые коррекции изображения.
        for (ImageCorrector imageCorrector : imageCorrectors) {
            imageCorrector.process(renderedImage);
        }

        // 6. Сохраняем готовое изображение в выбранном формате.
        Path savedImagePath = imageFileWriter.write(
                renderedImage,
                configuration.outputPath(),
                configuration.imageFormat()
        );

        // 7. Передаём сохранённый файл пользовательскому интерфейсу.
        userInterface.showImage(savedImagePath);
    }
}

/*
выбор из единичных главных  сначала, а потом по спискам

1. ввод данных руками
2. Выбор одного варианта
3. выбор нескольких вариантов

путь куда загрузить изображение - ввод
размер изображения - ввод
размер приближения зум лупа - ввод в процентах (от 0 до 200)
количество итераций - ввод
стартовое число генератора случайных чисел - ввод
тип изображения - выбор одного варианта
тип отрисовщика - выбор одного варианта (однопоточка, многопоточка)
список трансформационных фукнций - выбор нескольких вариантов, ввод данных внутри параметров
 */