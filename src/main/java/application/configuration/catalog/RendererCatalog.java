package application.configuration.catalog;

import application.rendering.renderer.Renderer;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class RendererCatalog implements Catalog<Renderer> {
    private final List<String> nameList;
    private final Map<String, Renderer> rendererCatalog;

    public RendererCatalog(List<Renderer> renderers) {
        rendererCatalog = renderers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Renderer::getName,
                        renderer -> renderer
                ));

        nameList = renderers.stream()
                .map(Renderer::getName)
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    @Override
    public List<String> showCatalog() {
        return nameList;
    }

    @Override
    public Renderer getAlgorithm(String rendererName) {
        return rendererCatalog.get(rendererName);
    }

    @Override
    public int size() {
        return nameList.size();
    }
}
