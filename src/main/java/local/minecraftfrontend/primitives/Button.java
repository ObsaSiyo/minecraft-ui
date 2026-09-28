package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.Ui;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** Ciel button variants with owo component layout and native input/narration. */
public class Button extends ButtonComponent {
    public enum Variant { DEFAULT, ROW, ROW_LEFT, MENU }
    private final Variant variant;
    private Supplier<Component> accessibleName;

    protected Button(Component label, Runnable action, Variant variant) {
        super(label, ignored -> action.run());
        Objects.requireNonNull(action);
        this.variant = Objects.requireNonNull(variant);
        Ui.element(this).sizing(Sizing.fill(100), Sizing.fixed(Tokens.CONTROL_HEIGHT));
    }
    public static Button create(Component label, Runnable action) { return new Button(label, action, Variant.DEFAULT); }
    public static Button row(Component label, Runnable action) { return new Button(label, action, Variant.ROW); }
    public static Button rowLeft(Component label, Runnable action) { return new Button(label, action, Variant.ROW_LEFT); }
    public static Button menuItem(Component label, Runnable action) { return new Button(label, action, Variant.MENU); }
    public Button accessibleName(Supplier<Component> value) { accessibleName = Objects.requireNonNull(value); return this; }
    @Override protected MutableComponent createNarrationMessage() {
        return accessibleName == null ? super.createNarrationMessage() : accessibleName.get().copy();
    }
    @Override public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var colors = Tokens.COLORS;
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        boolean highlighted = active && isHoveredOrFocused();
        if (variant == Variant.DEFAULT) {
            graphics.fill(x, y, x + w, y + h, highlighted ? colors.focus() : colors.border());
            graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, active ? colors.surfaceRaised() : colors.surface());
        } else if (variant == Variant.MENU) {
            graphics.fill(x, y, x + w, y + h, highlighted ? colors.surface() : colors.surfaceRaised());
            if (highlighted) graphics.fill(x, y, x + 2, y + h, colors.focus());
        } else if (highlighted) graphics.fill(x, y, x + w, y + h, colors.surfaceRaised());
        int color = active ? colors.text() : colors.textMuted();
        var font = Minecraft.getInstance().font;
        if (variant == Variant.ROW_LEFT || variant == Variant.MENU)
            Label.render(graphics, font, getMessage(), x + 5, y + (h - 9) / 2, w - 10, color);
        else Label.centered(graphics, font, getMessage(), x + 5, y + (h - 9) / 2, w - 10, color);
    }
}
