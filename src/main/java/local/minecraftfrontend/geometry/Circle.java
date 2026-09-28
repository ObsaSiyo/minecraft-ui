package local.minecraftfrontend.geometry;

import java.util.Objects;

/** Integer-centered circle; independent of rendering and resource values. */
public record Circle(Point center, int radius) {
    public Circle {
        Objects.requireNonNull(center, "center");
        if (radius < 0) throw new IllegalArgumentException("Negative radius");
    }
    public boolean contains(Point point) {
        double dx = (double) point.x() - center.x(), dy = (double) point.y() - center.y();
        return dx * dx + dy * dy <= (double) radius * radius;
    }
    /** Pixel bounds include both outer scanlines. Unrepresentable bounds throw on overflow. */
    public Rect bounds() {
        int diameter = Math.addExact(Math.multiplyExact(radius, 2), 1);
        return new Rect(Math.subtractExact(center.x(), radius), Math.subtractExact(center.y(), radius),
            diameter, diameter);
    }
    /** Zero degrees points right; positive angles move clockwise in screen coordinates. */
    public Point at(double degrees) {
        if (!Double.isFinite(degrees)) throw new IllegalArgumentException("Nonfinite angle");
        double radians = Math.toRadians(degrees % 360);
        return new Point(Math.toIntExact(Math.round(center.x() + radius * Math.cos(radians))),
            Math.toIntExact(Math.round(center.y() + radius * Math.sin(radians))));
    }
    /** Half-width of an integer scanline; retained for primitive Orb rendering. */
    public static int halfSpan(int radius, int row) {
        if (radius < 0 || Math.abs((long) row) > radius) return 0;
        return (int) Math.floor(Math.sqrt((double) radius * radius - (double) row * row));
    }
}
