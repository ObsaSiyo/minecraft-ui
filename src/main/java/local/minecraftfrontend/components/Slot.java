package local.minecraftfrontend.components;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.primitives.Stack;

import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.Sizing;
import java.util.function.Supplier;

/** Frame/container only. DISABLED changes appearance, not child input eligibility. */
public final class Slot {
    public enum State { NORMAL, SELECTED, DISABLED }
    private Slot() {}
    public static StackLayout create(Sizing width, Sizing height, Supplier<State> state) {
        java.util.Objects.requireNonNull(state);
        var slot = Stack.create(width, height);
        slot.padding(io.wispforest.owo.ui.core.Insets.of(1));
        slot.surface((graphics, parent) -> {
            var value = java.util.Objects.requireNonNull(state.get());
            var colors = Tokens.COLORS;
            int border = switch (value) {
                case NORMAL -> colors.border();
                case SELECTED -> colors.focus();
                case DISABLED -> colors.surfaceRaised();
            };
            int x = parent.x(), y = parent.y(), w = parent.width(), h = parent.height();
            if (w < 2 || h < 2) return;
            graphics.fill(x, y, x + w, y + h, border);
            if (w > 2 && h > 2) graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1,
                value == State.DISABLED ? colors.surface() : colors.surfaceRaised());
        });
        return slot;
    }
}
