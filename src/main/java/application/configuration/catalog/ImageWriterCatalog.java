package application.configuration.catalog;

import application.image.encoding.ImageFormat;
import org.springframework.stereotype.Component;

import javax.imageio.ImageWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ImageWriterCatalog implements Catalog<ImageWriter> {
    private final List<String> nameList;
    private final Map<String, ImageWriter> imageWriterCatalog;

    public ImageWriterCatalog(Map<String, ImageWriter> imageWriters) {
        imageWriterCatalog = Map.copyOf(imageWriters);
        nameList = Arrays.stream(ImageFormat.values())
                .map(ImageFormat::name)
                .toList();
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public ImageWriter getAlgorithm(String imageFormatName) {
        return imageWriterCatalog.get(imageFormatName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
