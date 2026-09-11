package application.configuration.setting;

import application.model.Point;

public record GenerationSettings(
        Point defaultWorldFocus,
        double defaultVisibleWorldWidth,
        double defaultVisibleWorldHeight,
        int iterationsPerMillion) {}
