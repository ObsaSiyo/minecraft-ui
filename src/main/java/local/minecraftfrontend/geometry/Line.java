package local.minecraftfrontend.geometry;

import java.util.Objects;

/** A finite segment between two points; does not draw a separator or border. */
public record Line(Point start, Point end) {
    public Line {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
    }
    public double length() { return start.distanceTo(end); }

    /** Interpolates within the segment, rounding to the nearest GUI-space integer. */
    public Point at(double fraction) {
        if (!Double.isFinite(fraction) || fraction < 0 || fraction > 1)
            throw new IllegalArgumentException("Fraction must be between zero and one");
        return new Point(Math.toIntExact(Math.round(start.x() + ((double) end.x() - start.x()) * fraction)),
            Math.toIntExact(Math.round(start.y() + ((double) end.y() - start.y()) * fraction)));
    }
}
