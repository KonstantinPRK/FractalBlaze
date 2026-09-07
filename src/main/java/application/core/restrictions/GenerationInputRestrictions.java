package application.core.restrictions;

public record GenerationInputRestrictions(
        IntegerRange worldZoomPercentage,
        IntegerRange iterationCountInMillions,
        IntegerRange randomSeed) {}
