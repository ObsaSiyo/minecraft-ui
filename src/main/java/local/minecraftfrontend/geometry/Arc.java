package local.minecraftfrontend.geometry;

import java.util.Objects;

/** A circular segment for radial displays. Angles are degrees, positive clockwise on screen. */
public record Arc(Circle circle, double start, double sweep) {
    public Arc {
        Objects.requireNonNull(circle, "circle");
        if (!Double.isFinite(start) || !Double.isFinite(sweep) || Math.abs(sweep) > 360)
            throw new IllegalArgumentException("Arc requires finite angles and at most one full turn");
        start %= 360;
        if (start < 0) start += 360;
    }
    public double length() { return Math.toRadians(Math.abs(sweep)) * circle.radius(); }
    /** Interpolates along the arc; a negative sweep moves counterclockwise. */
    public Point at(double fraction) {
        if (!Double.isFinite(fraction) || fraction < 0 || fraction > 1)
            throw new IllegalArgumentException("Fraction must be between zero and one");
        return circle.at(start + sweep * fraction);
    }
}
