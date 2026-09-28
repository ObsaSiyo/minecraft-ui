package local.minecraftfrontend.components;

import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import local.minecraftfrontend.Ui;
import local.minecraftfrontend.primitives.Button;
import local.minecraftfrontend.primitives.Label;
import local.minecraftfrontend.tokens.Tokens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/** Controlled row: full-texture icon (optional), label, supporting text and selection marker. */
public final class ListRow extends Button {
    private final Component supporting;
    private final ResourceLocation icon;
    private final BooleanSupplier selected, enabled;

    public ListRow(Component label, Component supporting, ResourceLocation icon,
                   BooleanSupplier selected, BooleanSupplier enabled, Runnable onPress) {
        super(label, () -> { if (enabled.getAsBoolean()) onPress.run(); }, Variant.ROW_LEFT);
        this.supporting = Objects.requireNonNull(supporting);
        this.icon = icon;
        this.selected = Objects.requireNonNull(selected);
        this.enabled = Objects.requireNonNull(enabled);
        Objects.requireNonNull(onPress);
        Ui.element(this).verticalSizing(Sizing.fixed(supporting.getString().isBlank()
            ? Tokens.CONTROL_HEIGHT : Tokens.CONTROL_HEIGHT * 2));
        active = enabled.getAsBoolean();
        accessibleName(() -> getMessage().copy().append(". ").append(this.supporting)
            .append(selected.getAsBoolean() ? ". Selected." : ""));
    }
    @Override public void renderWidget(GuiGraphics graphics, int mx, int my, float delta) {
        active = enabled.getAsBoolean();
        var colors = Tokens.COLORS;
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        boolean selection = selected.getAsBoolean();
        if (selection || (active && isHoveredOrFocused())) graphics.fill(x, y, x + w, y + h, colors.surfaceRaised());
        if (selection) graphics.fill(x, y, x + 2, y + h, colors.focus());
        // A separate outline keeps keyboard focus distinguishable from selection.
        if (active && isFocused()) {
            graphics.fill(x, y, x + w, y + 1, colors.focus());
            graphics.fill(x, y + h - 1, x + w, y + h, colors.focus());
        }
        int left = x + Tokens.GAP;
        if (icon != null) {
            graphics.blit(icon, left, y + (h - 16) / 2, 0, 0, 16, 16, 16, 16);
            left += 16 + Tokens.GAP;
        }
        var font = Minecraft.getInstance().font;
        boolean secondary = !supporting.getString().isBlank();
        int top = secondary ? y + (h - 2 * font.lineHeight - Tokens.GAP) / 2 : y + (h - font.lineHeight) / 2;
        Label.render(graphics, font, getMessage(), left, top, x + w - left - Tokens.GAP,
            active ? colors.text() : colors.textMuted());
        if (secondary) Label.render(graphics, font, supporting, left, top + font.lineHeight + Tokens.GAP,
            x + w - left - Tokens.GAP, colors.textMuted());
    }
}
