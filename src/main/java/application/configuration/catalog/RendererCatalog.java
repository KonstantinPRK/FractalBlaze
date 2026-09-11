package application.configuration.catalog;

import application.rendering.renderer.Renderer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RendererCatalog implements Catalog<Renderer> {
    private final List<String> nameList;
    private final Map<String, Renderer> rendererCatalog;

    public RendererCatalog(List<Renderer> renderers) {
        rendererCatalog = renderers.stream()
                .collect(
                        Collectors.toMap(
                                renderer -> renderer.getName(),
                                renderer -> renderer)
                );

        nameList = List.copyOf(rendererCatalog.keySet());
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public Renderer getAlgorithm(String algorithmName) {
        return rendererCatalog.get(algorithmName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
