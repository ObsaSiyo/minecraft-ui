package local.minecraftfrontend.geometry;

/** An integer GUI-space position. Translation rejects coordinate overflow. */
public record Point(int x, int y) {
    public Point translate(int dx, int dy) {
        return new Point(Math.addExact(x, dx), Math.addExact(y, dy));
    }
    public double distanceTo(Point other) {
        return Math.hypot((double) other.x - x, (double) other.y - y);
    }
}
