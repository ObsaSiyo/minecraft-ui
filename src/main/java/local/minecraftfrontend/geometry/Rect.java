package local.minecraftfrontend.geometry;

import java.util.Optional;

/** Immutable integer bounds; right and bottom edges are exclusive. */
public record Rect(int x, int y, int width, int height) {
    public Rect {
        if (width < 0 || height < 0) throw new IllegalArgumentException("Negative rectangle size");
        // Keep derived edges representable rather than silently wrapping coordinates.
        Math.addExact(x, width);
        Math.addExact(y, height);
    }
    public int right() { return x + width; }
    public int bottom() { return y + height; }
    public Point position() { return new Point(x, y); }
    public Size size() { return new Size(width, height); }
    public boolean isEmpty() { return width == 0 || height == 0; }
    public boolean contains(Point point) {
        return point.x() >= x && point.x() < right() && point.y() >= y && point.y() < bottom();
    }
    public boolean contains(Rect other) {
        return other.x >= x && other.y >= y && other.right() <= right() && other.bottom() <= bottom();
    }
    public boolean intersects(Rect other) {
        return !isEmpty() && !other.isEmpty() && x < other.right() && right() > other.x
            && y < other.bottom() && bottom() > other.y;
    }
    /** Touching edges have no positive-area intersection. */
    public Optional<Rect> intersection(Rect other) {
        int left = Math.max(x, other.x), top = Math.max(y, other.y);
        int right = Math.min(right(), other.right()), bottom = Math.min(bottom(), other.bottom());
        return right <= left || bottom <= top ? Optional.empty()
            : Optional.of(new Rect(left, top, right - left, bottom - top));
    }
    public Rect translate(int dx, int dy) {
        return new Rect(Math.addExact(x, dx), Math.addExact(y, dy), width, height);
    }
    public Rect inset(Insets insets) {
        if (insets.horizontal() > width || insets.vertical() > height)
            throw new IllegalArgumentException("Insets exceed rectangle size");
        return new Rect(Math.addExact(x, insets.left()), Math.addExact(y, insets.top()),
            width - insets.horizontal(), height - insets.vertical());
    }
}
