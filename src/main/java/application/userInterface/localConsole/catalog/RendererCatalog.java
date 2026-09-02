package application.userInterface.localConsole.catalog;

import application.renderer.Renderer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RendererCatalog implements Catalog<Renderer> {
    private List<String> nameList;
    private Map<String, Renderer> rendererCatalog;

    public RendererCatalog(List<Renderer> renderers){
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
