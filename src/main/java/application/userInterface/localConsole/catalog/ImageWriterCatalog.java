package application.userInterface.localConsole.catalog;

import application.parameters.ImageFormat;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ImageWriterCatalog implements Catalog<ImageWriter> {
    private final List<String> nameList;
    private final Map<String, ImageWriter> imageWriterCatalog;

    public ImageWriterCatalog() {
        this(List.of(ImageFormat.values()));
    }

    public ImageWriterCatalog(List<ImageFormat> formats) {
        imageWriterCatalog = formats.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ImageFormat::name,
                        imageFormat -> ImageIO
                                .getImageWritersByFormatName(imageFormat.name())
                                .next()
                ));

        nameList = formats.stream()
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
