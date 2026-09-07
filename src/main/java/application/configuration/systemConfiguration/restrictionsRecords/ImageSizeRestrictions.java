package application.configuration.systemConfiguration.restrictionsRecords;

public record ImageSizeRestrictions(IntegerRange width, IntegerRange height) {}
