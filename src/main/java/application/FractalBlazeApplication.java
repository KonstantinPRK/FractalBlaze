package application;

import application.imageCorrector.ImageCorrector;
import application.parameters.Configuration;
import application.userInterface.ImageFileWriter;
import application.userInterface.UserInterface;
import application.userInterface.localConsole.LocalConsole;
import application.world.FractalImage;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public final class FractalBlazeApplication {
    UserInterface userInterface = new LocalConsole();
    List<ImageCorrector> imageCorrectors;
    ImageFileWriter imageFileWriter;


    public static void main(String[] args){
        SpringApplication.run(FractalBlazeApplication.class, args);
    }

    @PostConstruct
    public void start() {
        // 1. Получаем все параметры будущего изображения.
        Configuration configuration = userInterface.requestConfiguration();
        FractalImage emptyCanvas = FractalImage.create(configuration.imageSize());
        FractalImage renderedImage = configuration.renderer().render(
                emptyCanvas,
                configuration.magnifierZoom(),
                configuration.transformations(),
                configuration.iterationCount(),
                configuration.randomSeed()
        );
        /*
        // 3. Создаём пустой холст требуемого размера.


        // 4. Генерируем попадания точек в пиксели холста.


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
         */
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