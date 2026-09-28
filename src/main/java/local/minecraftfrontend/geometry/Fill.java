package local.minecraftfrontend.geometry;

/** Converts a bounded fraction to a pixel count without knowing the meter's shape. */
public final class Fill {
    private Fill() {}

    public static int size(int size, double fraction) {
        if (size <= 0 || !Double.isFinite(fraction)) return 0;
        return (int) Math.round(size * Math.max(0, Math.min(1, fraction)));
    }
}
