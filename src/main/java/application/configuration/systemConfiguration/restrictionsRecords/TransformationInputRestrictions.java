package application.configuration.systemConfiguration.restrictionsRecords;

public record TransformationInputRestrictions(
        IntegerRange scalePercentage,
        IntegerRange rotationAngleInDegrees,
        IntegerRange horizontalShiftInHundredths,
        IntegerRange verticalShiftInHundredths) {}
