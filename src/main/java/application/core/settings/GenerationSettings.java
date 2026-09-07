package application.core.settings;

import application.picture.Point;

public record GenerationSettings(
        Point defaultWorldFocus,
        double defaultVisibleWorldWidth,
        double defaultVisibleWorldHeight,
        int iterationsPerMillion) {}
