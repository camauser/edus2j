package edus2.domain;

public class VideoDimensions {
    private final int width;
    private final int height;

    public VideoDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
