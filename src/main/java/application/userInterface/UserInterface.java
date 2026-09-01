package application.userInterface;

import application.parameters.Configuration;

import java.nio.file.Path;

public interface UserInterface {
    Configuration requestConfiguration();
    void returnFractalImage(Path file);
}
