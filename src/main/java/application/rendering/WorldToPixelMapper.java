package application.rendering;

import application.picture.FractalImage;
import application.picture.Point;
import application.picture.Space;
import org.springframework.stereotype.Component;

@Component
public class WorldToPixelMapper {

    public int mapHorizontalCoordinate(Point point, Space space, FractalImage canvas) {
        double leftBorder = space.focusPoint().x() - space.visibleWorldWidth() / 2;
        double relativeHorizontalPosition = (point.x() - leftBorder) / space.visibleWorldWidth();

        return (int) (relativeHorizontalPosition * canvas.width());
    }

    public int mapVerticalCoordinate(Point point, Space space, FractalImage canvas) {
        double bottomBorder = space.focusPoint().y() - space.visibleWorldHeight() / 2;
        double relativeVerticalPosition = (point.y() - bottomBorder) / space.visibleWorldHeight();

        return canvas.height() - 1 - (int) (relativeVerticalPosition * canvas.height());
    }
}
