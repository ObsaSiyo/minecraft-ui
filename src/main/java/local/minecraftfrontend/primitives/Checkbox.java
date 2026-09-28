package local.minecraftfrontend.primitives;

import local.minecraftfrontend.Ui;
import local.minecraftfrontend.tokens.Tokens;

import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** Controlled checkbox: pressing emits a callback; never changes checked state optimistically. */
public final class Checkbox extends Button {
    private final BooleanSupplier checked;
    private final Supplier<Component> label;
    private final boolean labeled;
    public Checkbox(Supplier<Component> label, BooleanSupplier checked, Runnable action, boolean labeled) {
        super(label.get(), action, Variant.ROW);
        this.label = Objects.requireNonNull(label);
        this.checked = Objects.requireNonNull(checked);
        this.labeled = labeled;
        if (!labeled) Ui.element(this).horizontalSizing(Sizing.fixed(Tokens.CONTROL_HEIGHT));
        accessibleName(() -> label.get().copy().append(checked.getAsBoolean() ? ". Checked." : ". Unchecked."));
    }
    public static Checkbox row(Supplier<Component> label, BooleanSupplier checked, Runnable action) {
        return new Checkbox(label, checked, action, true);
    }
    @Override public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var colors = Tokens.COLORS;
        int size = Math.min(10, Math.min(getWidth(), getHeight()));
        if (size < 2) return;
        int left = getX() + (labeled ? 3 : (getWidth() - size) / 2), top = getY() + (getHeight() - size) / 2;
        boolean highlighted = active && isHoveredOrFocused();
        if (labeled) {
            if (highlighted) graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), colors.surfaceRaised());
            Label.render(graphics, Minecraft.getInstance().font, label.get(), getX() + 21,
                getY() + (getHeight() - 9) / 2, getWidth() - 26, active ? colors.text() : colors.textMuted());
        }
        graphics.fill(left, top, left + size, top + size, highlighted ? colors.focus() : colors.border());
        graphics.fill(left + 1, top + 1, left + size - 1, top + size - 1, colors.surface());
        if (checked.getAsBoolean() && size >= 8) {
            int cx = left + (size - 6) / 2, cy = top + (size - 5) / 2;
            graphics.fill(cx, cy + 3, cx + 1, cy + 4, colors.text());
            for (int i = 0; i < 5; i++) graphics.fill(cx + 1 + i, cy + 4 - i, cx + 2 + i, cy + 5 - i, colors.text());
        }
    }
}
