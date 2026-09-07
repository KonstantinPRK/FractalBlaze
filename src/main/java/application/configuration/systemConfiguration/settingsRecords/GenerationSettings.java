package application.configuration.systemConfiguration.settingsRecords;

import application.picture.Point;

public record GenerationSettings(
        Point defaultWorldFocus,
        double defaultVisibleWorldWidth,
        double defaultVisibleWorldHeight,
        int iterationsPerMillion) {}
