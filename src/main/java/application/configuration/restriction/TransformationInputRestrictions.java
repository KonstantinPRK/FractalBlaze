package application.configuration.restriction;

public record TransformationInputRestrictions(
        IntegerRange scalePercentage,
        IntegerRange rotationAngleInDegrees,
        IntegerRange horizontalShiftInHundredths,
        IntegerRange verticalShiftInHundredths) {}
