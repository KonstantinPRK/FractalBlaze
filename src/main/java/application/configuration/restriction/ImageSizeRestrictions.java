package application.configuration.restriction;

public record ImageSizeRestrictions(IntegerRange width, IntegerRange height) {}
