package application.rendering;

import application.picture.Point;
import application.picture.Space;
import org.springframework.stereotype.Component;

@Component
public class WorldToPixelMapper {

    public int mapHorizontalCoordinate(Point point, RenderingContext context) {
        Space space = context.space();
        double leftBorder = space.focusPoint().x() - space.visibleWorldWidth() / 2;
        double relativeHorizontalPosition = (point.x() - leftBorder) / space.visibleWorldWidth();

        return (int) (relativeHorizontalPosition * context.imageWidth());
    }

    public int mapVerticalCoordinate(Point point, RenderingContext context) {
        Space space = context.space();
        double bottomBorder = space.focusPoint().y() - space.visibleWorldHeight() / 2;
        double relativeVerticalPosition = (point.y() - bottomBorder) / space.visibleWorldHeight();

        return context.imageHeight() - 1 - (int) (relativeVerticalPosition * context.imageHeight());
    }
}
