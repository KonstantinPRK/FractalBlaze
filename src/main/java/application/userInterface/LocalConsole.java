package application.userInterface;

import application.parameters.Configuration;

import java.nio.file.Path;

public class LocalConsole implements UserInterface {
    private static final Path OUTPUT = Path.of("fractal.png");


    @Override
    public Configuration requestFractalImageConfiguration() {
        /*
        запросить качество изображения высота ширина пикселей - дать ограничения
        запросить формат - показать список
        запросить количество итераций ввести ограничение
        запросить сеед рандомайзера
        запросить путь и сразу проверить
        запросить количество трансформаций или все - показать список
         */
        return null;
    }

    @Override
    public void showFractalImage(Path file) {

    }
}
