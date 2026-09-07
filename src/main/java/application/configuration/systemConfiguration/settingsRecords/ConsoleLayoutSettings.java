package application.configuration.systemConfiguration.settingsRecords;

public record ConsoleLayoutSettings(
        int emptyLinesBeforeRequest,
        String examplePrefix,
        String restrictionsPrefix,
        String inputPrompt) {}
