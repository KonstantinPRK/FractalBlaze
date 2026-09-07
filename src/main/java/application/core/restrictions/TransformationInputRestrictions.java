package application.core.restrictions;

public record TransformationInputRestrictions(
        IntegerRange scalePercentage,
        IntegerRange rotationAngleInDegrees,
        IntegerRange horizontalShiftInHundredths,
        IntegerRange verticalShiftInHundredths) {}
