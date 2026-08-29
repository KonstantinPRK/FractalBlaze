package application;

import application.parameters.Configuration;
import application.userInterface.UserInterface;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FractalBlazeApplication {
    private final UserInterface UI;
    private final FireSource fireSource;
    private final ImageFileWriter imageWriter;

    public FractalBlazeApplication(UserInterface ui, FireSource fireSource, ImageFileWriter imageWriter) {
        this.UI = ui;
        this.fireSource = fireSource;
        this.imageWriter = imageWriter;
    }


    @PostConstruct
    public void start(){
        Configuration configuration = UI.requestFractalImageConfiguration();
        Flame flame = fireSource.kindleFlame(configuration);
        FractalImage image = flame.takePicture();
        Path file = imageWriter.write(image, configuration.outputPath(), configuration.imageFormat());

        UI.showFractalImage(file);
    }


}
