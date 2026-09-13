package application.configuration.catalog;

import application.model.AspectRatio;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AspectRatioCatalog implements Catalog<AspectRatio> {
    private final List<String> nameList;
    private final Map<String, AspectRatio> aspectRatioCatalog;

    public AspectRatioCatalog() {
        aspectRatioCatalog = Arrays.stream(AspectRatio.values())
                .collect(Collectors.toUnmodifiableMap(
                        AspectRatio::getDisplayName,
                        aspectRatio -> aspectRatio
                ));

        nameList = Arrays.stream(AspectRatio.values())
                .map(AspectRatio::getDisplayName)
                .toList();
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public AspectRatio getAlgorithm(String aspectRatioName) {
        return aspectRatioCatalog.get(aspectRatioName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
