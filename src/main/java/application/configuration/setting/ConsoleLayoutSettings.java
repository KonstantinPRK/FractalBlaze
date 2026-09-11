package application.configuration.setting;

public record ConsoleLayoutSettings(
        int emptyLinesBeforeRequest,
        String examplePrefix,
        String restrictionsPrefix,
        String inputPrompt) {}
