package application.configuration.catalog;

import application.rendering.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransformationCatalog implements Catalog<Transformation> {
    private final List<String> nameList;
    private final Map<String, Transformation> transformationCatalog;

    public TransformationCatalog(List<Transformation> transformations) {
        transformationCatalog = transformations.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Transformation::getName,
                        transformation -> transformation
                ));

        nameList = transformations.stream()
                .map(Transformation::getName)
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public Transformation getAlgorithm(String algorithmName) {
        return transformationCatalog.get(algorithmName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
