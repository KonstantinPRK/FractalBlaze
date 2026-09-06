package application.userConfiguration.algorithmsCatalog;

import java.util.List;


public interface Catalog<AlgorithmType> {


    List<String> showCatalog();


    AlgorithmType getAlgorithm(String algorithmName);

    int size();
}
