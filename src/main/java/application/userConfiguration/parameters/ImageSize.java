package application.userConfiguration.parameters;

public record ImageSize(int width, int height) {
    public static int maxSize(){
        return 8000;
    }

    public static int minSize(){
        return 800;
    }
}
