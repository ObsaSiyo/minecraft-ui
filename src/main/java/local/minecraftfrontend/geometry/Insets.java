package local.minecraftfrontend.geometry;

/** Nonnegative edge distances, ordered top/right/bottom/left. */
public record Insets(int top, int right, int bottom, int left) {
    public Insets {
        if (top < 0 || right < 0 || bottom < 0 || left < 0)
            throw new IllegalArgumentException("Negative inset");
        Math.addExact(left, right);
        Math.addExact(top, bottom);
    }
    public static Insets all(int amount) { return new Insets(amount, amount, amount, amount); }
    public static Insets symmetric(int horizontal, int vertical) {
        return new Insets(vertical, horizontal, vertical, horizontal);
    }
    public int horizontal() { return left + right; }
    public int vertical() { return top + bottom; }
}
