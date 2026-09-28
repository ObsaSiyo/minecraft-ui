package local.minecraftfrontend.geometry;

/** Nonnegative GUI-space dimensions, independent of position. */
public record Size(int width, int height) {
    public Size {
        if (width < 0 || height < 0) throw new IllegalArgumentException("Negative size");
    }
    public boolean isEmpty() { return width == 0 || height == 0; }
}
