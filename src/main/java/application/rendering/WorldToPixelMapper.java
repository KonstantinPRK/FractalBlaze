package application.rendering;

import application.model.Space;
import org.springframework.stereotype.Component;

@Component
public class WorldToPixelMapper {

    public int mapHorizontalCoordinate(double pointX, Space space, int imageWidth) {
        double leftBorder = space.focusPoint().x() - space.visibleWorldWidth() / 2;
        double relativeHorizontalPosition = (pointX - leftBorder) / space.visibleWorldWidth();

        return (int) (relativeHorizontalPosition * imageWidth);
    }

    public int mapVerticalCoordinate(double pointY, Space space, int imageHeight) {
        double bottomBorder = space.focusPoint().y() - space.visibleWorldHeight() / 2;
        double relativeVerticalPosition = (pointY - bottomBorder) / space.visibleWorldHeight();

        return imageHeight - 1 - (int) (relativeVerticalPosition * imageHeight);
    }
}
