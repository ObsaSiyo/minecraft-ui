package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.geometry.Fill;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.Sizing;
import java.util.function.DoubleSupplier;

/** Display-only fraction; no resource, clock or gameplay state. */
public final class Progress {
    private Progress() {}
    public static BaseComponent create(Sizing width, DoubleSupplier fraction) {
        java.util.Objects.requireNonNull(fraction);
        return new Paint(width, Sizing.fixed(3), (graphics, x, y, w, h) -> {
            graphics.fill(x, y, x + w, y + h, Tokens.COLORS.surfaceRaised());
            graphics.fill(x, y, x + Fill.size(w, fraction.getAsDouble()), y + h, Tokens.COLORS.focus());
        });
    }
}
