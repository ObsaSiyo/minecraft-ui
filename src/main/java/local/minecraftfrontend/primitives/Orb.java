package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.geometry.Circle;
import local.minecraftfrontend.geometry.Fill;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.Sizing;
import java.util.function.DoubleSupplier;

public final class Orb {
    private Orb() {}
    public static BaseComponent create(int radius, DoubleSupplier fraction, int mappedFill) {
        if (radius < 3) throw new IllegalArgumentException("Orb radius must be at least 3");
        int diameter = Math.addExact(Math.multiplyExact(radius, 2), 1);
        java.util.Objects.requireNonNull(fraction);
        return new Paint(Sizing.fixed(diameter), Sizing.fixed(diameter), (graphics, x, y, w, h) -> {
            int r = (Math.min(w, h) - 1) / 2;
            if (r < 3) return;
            int cx = x + w / 2, cy = y + h / 2;
            for (int row = -r; row <= r; row++) {
                int span = Circle.halfSpan(r, row);
                graphics.fill(cx - span, cy + row, cx + span + 1, cy + row + 1, Tokens.COLORS.border());
            }
            int inner = r - 2, fill = Fill.size(inner * 2 + 1, fraction.getAsDouble());
            for (int row = -inner; row <= inner; row++) {
                int span = Circle.halfSpan(inner, row);
                graphics.fill(cx - span, cy + row, cx + span + 1, cy + row + 1,
                    row >= inner + 1 - fill ? mappedFill : Tokens.COLORS.surfaceRaised());
            }
        });
    }
}
