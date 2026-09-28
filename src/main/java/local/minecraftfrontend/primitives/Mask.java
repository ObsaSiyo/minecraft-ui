package local.minecraftfrontend.primitives;

import local.minecraftfrontend.geometry.Fill;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.Sizing;
import java.util.function.DoubleSupplier;

/** Bottom-up visual cover, not a clipping mask. Add after the icon in a Stack. */
public final class Mask {
    private Mask() {}
    public static BaseComponent create(Sizing width, Sizing height, DoubleSupplier fraction, int mappedColor) {
        java.util.Objects.requireNonNull(fraction);
        return new Paint(width, height, (graphics, x, y, w, h) -> {
            int covered = Fill.size(h, fraction.getAsDouble());
            if (covered > 0) graphics.fill(x, y + h - covered, x + w, y + h, mappedColor);
        });
    }
}
