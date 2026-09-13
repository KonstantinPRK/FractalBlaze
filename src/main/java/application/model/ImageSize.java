package application.model;

public record ImageSize(int width, int height) {
    public static ImageSize calculate(ImageQuality quality, ImageShape shape, AspectRatio aspectRatio) {
        int commonDivisor = greatestCommonDivisor(aspectRatio.horizontalParts(), aspectRatio.verticalParts());
        int horizontalUnits = aspectRatio.horizontalParts() / commonDivisor;
        int verticalUnits = aspectRatio.verticalParts() / commonDivisor;

        long unitCount = (long) Math.floor(
                Math.sqrt((double) quality.pixelBudget() / (horizontalUnits * verticalUnits)));
        int landscapeWidth = Math.toIntExact(unitCount * horizontalUnits);
        int landscapeHeight = Math.toIntExact(unitCount * verticalUnits);

        if (shape == ImageShape.PORTRAIT) return new ImageSize(landscapeHeight, landscapeWidth);

        return new ImageSize(landscapeWidth, landscapeHeight);
    }

    public long pixelCount() {
        return (long) width * height;
    }

    private static int greatestCommonDivisor(int firstNumber, int secondNumber) {
        int left = firstNumber;
        int right = secondNumber;

        while (right != 0) {
            int remainder = left % right;
            left = right;
            right = remainder;
        }

        return left;
    }
}
