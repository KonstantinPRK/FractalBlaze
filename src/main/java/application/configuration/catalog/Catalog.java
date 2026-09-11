package application.configuration.catalog;

import java.util.List;


public interface Catalog<AlgorithmType> {


    List<String> showCatalog();


    AlgorithmType getAlgorithm(String algorithmName);

    int size();
}
