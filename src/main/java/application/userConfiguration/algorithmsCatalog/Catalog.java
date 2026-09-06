package application.userConfiguration.algorithmsCatalog;

import java.util.List;

/**
 * Интерфейс каталога алгоритмов определённого типа.
 * Предоставляет методы для получения списка имён алгоритмов,
 * получения экземпляра алгоритма по имени и получения количества
 * доступных алгоритмов.
 *
 * @param <AlgorithmType> тип алгоритма (например, {@code Generator},
 *                       {@code Renderer}, {@code Solver})
 * @author unknown
 * @version 1.0
 */
public interface Catalog<AlgorithmType> {

    /**
     * Возвращает неизменяемый список имён всех алгоритмов, доступных в каталоге.
     *
     * @return список строк с именами алгоритмов
     */
    List<String> showCatalog();

    /**
     * Возвращает алгоритм по его имени.
     *
     * @param algorithmName имя алгоритма
     * @return экземпляр алгоритма, соответствующий указанному имени,
     *         или {@code null}, если алгоритм с таким именем не найден
     */
    AlgorithmType getAlgorithm(String algorithmName);

    /**
     * Возвращает количество алгоритмов в каталоге.
     *
     * @return число доступных алгоритмов
     */
    int size();
}
