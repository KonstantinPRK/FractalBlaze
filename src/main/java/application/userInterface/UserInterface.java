package application.userInterface;

import application.parameters.Configuration;

import java.nio.file.Path;

public interface UserInterface {
    Configuration requestFractalImageConfiguration();
    void showFractalImage(Path file);
}
