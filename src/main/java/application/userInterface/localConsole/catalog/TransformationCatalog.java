package application.userInterface.localConsole.catalog;

import application.transformation.factory.TransformationFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransformationCatalog implements Catalog<TransformationFactory> {
    private final List<String> nameList;
    private final Map<String, TransformationFactory> transformationCatalog;

    public TransformationCatalog(List<TransformationFactory> transformationFactories) {
        transformationCatalog = transformationFactories.stream()
                .collect(Collectors.toUnmodifiableMap(
                        TransformationFactory::getName,
                        transformationFactory -> transformationFactory
                ));

        nameList = transformationFactories.stream()
                .map(TransformationFactory::getName)
                .toList();
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public TransformationFactory getAlgorithm(String algorithmName) {
        return transformationCatalog.get(algorithmName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
