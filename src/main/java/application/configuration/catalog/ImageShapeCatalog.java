package application.configuration.catalog;

import application.model.ImageShape;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ImageShapeCatalog implements Catalog<ImageShape> {
    private final List<String> nameList;
    private final Map<String, ImageShape> imageShapeCatalog;

    public ImageShapeCatalog() {
        imageShapeCatalog = Arrays.stream(ImageShape.values())
                .collect(Collectors.toUnmodifiableMap(
                        ImageShape::getDisplayName,
                        imageShape -> imageShape
                ));

        nameList = Arrays.stream(ImageShape.values())
                .map(ImageShape::getDisplayName)
                .toList();
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public ImageShape getAlgorithm(String imageShapeName) {
        return imageShapeCatalog.get(imageShapeName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
