package application.configuration.systemConfiguration.restrictionsRecords;

public record GenerationInputRestrictions(
        IntegerRange worldZoomPercentage,
        IntegerRange iterationCountInMillions,
        IntegerRange randomSeed) {}
