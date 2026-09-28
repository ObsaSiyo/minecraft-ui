package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.core.Color;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.locale.Language;
import net.minecraft.util.FormattedCharSequence;

public final class Label {
    private Label() {}
    public static LabelComponent create(Component text) {
        return Components.label(text).color(Color.ofArgb(Tokens.COLORS.text())).shadow(false);
    }
    public static LabelComponent muted(Component text) {
        return create(text).color(Color.ofArgb(Tokens.COLORS.textMuted()));
    }
    /** Single-line clipping for custom control renderers; retains fonts, styles and RTL order. */
    public static void render(GuiGraphics graphics, Font font, Component text, int x, int y, int width, int color) {
        if (width > 0) graphics.drawString(font, fitted(font, text, width), x, y, color, false);
    }
    public static void centered(GuiGraphics graphics, Font font, Component text, int x, int y, int width, int color) {
        if (width <= 0) return;
        var fitted = fitted(font, text, width);
        graphics.drawString(font, fitted, x + (width - font.width(fitted)) / 2, y, color, false);
    }
    private static FormattedCharSequence fitted(Font font, Component text, int width) {
        if (font.width(text) <= width) return text.getVisualOrderText();
        var ellipsis = Component.literal("…").setStyle(text.getStyle());
        if (width < font.width(ellipsis)) return FormattedCharSequence.EMPTY;
        return Language.getInstance().getVisualOrder(FormattedText.composite(
            font.substrByWidth(text, width - font.width(ellipsis)), ellipsis));
    }
}
