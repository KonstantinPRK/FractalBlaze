package application.configuration.restriction;

public record GenerationInputRestrictions(
        IntegerRange worldZoomPercentage,
        IntegerRange iterationCountInMillions,
        IntegerRange randomSeed) {}
