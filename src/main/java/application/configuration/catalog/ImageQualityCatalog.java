package application.configuration.catalog;

import application.model.ImageQuality;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ImageQualityCatalog implements Catalog<ImageQuality> {
    private final List<String> nameList;
    private final Map<String, ImageQuality> imageQualityCatalog;

    public ImageQualityCatalog() {
        imageQualityCatalog = Arrays.stream(ImageQuality.values())
                .collect(Collectors.toUnmodifiableMap(
                        ImageQuality::getDisplayName,
                        imageQuality -> imageQuality
                ));

        nameList = Arrays.stream(ImageQuality.values())
                .map(ImageQuality::getDisplayName)
                .toList();
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public ImageQuality getAlgorithm(String imageQualityName) {
        return imageQualityCatalog.get(imageQualityName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
