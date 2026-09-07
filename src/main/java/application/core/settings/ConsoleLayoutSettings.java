package application.core.settings;

public record ConsoleLayoutSettings(
        int emptyLinesBeforeRequest,
        String examplePrefix,
        String restrictionsPrefix,
        String inputPrompt) {}
