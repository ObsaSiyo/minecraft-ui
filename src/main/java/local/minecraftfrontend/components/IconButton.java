package local.minecraftfrontend.components;

import local.minecraftfrontend.Ui;
import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.primitives.Button;
import local.minecraftfrontend.primitives.Tooltip;

import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** Ciel's pixel glyphs, with a textual accessible name and owo tooltip. */
public final class IconButton extends Button {
    public enum Glyph {
        BOOK(0x7e, 0x92, 0x9a, 0x92, 0x9a, 0xfe, 0x82, 0x7e),
        PRESETS(0x7e, 0x42, 0x7e, 0x42, 0x7e, 0x42, 0x7e, 0x00),
        PERSON(0x18, 0x24, 0x24, 0x18, 0x00, 0x7e, 0x81, 0xff),
        TREE(0x18, 0x18, 0x18, 0x7e, 0x42, 0x42, 0xe7, 0xe7),
        GEAR(0x18, 0x5a, 0xe7, 0xa5, 0xa5, 0xe7, 0x5a, 0x18),
        SLIME(0x000, 0x078, 0x084, 0x102, 0x201, 0x249, 0x249, 0x231, 0x102, 0x0fc);
        private final int[] pixels;
        Glyph(int... pixels) {
            if (pixels.length != 8 && pixels.length != 10) throw new IllegalArgumentException("Expected 8px or 10px icon");
            int mask = (1 << pixels.length) - 1;
            for (int row : pixels) if ((row & ~mask) != 0) throw new IllegalArgumentException("Invalid icon row");
            this.pixels = pixels.clone();
        }
    }
    private final Glyph glyph;
    private final boolean dropdown;
    public IconButton(Component name, Glyph glyph, boolean dropdown, Runnable action) {
        super(name, action, Variant.ROW);
        this.glyph = java.util.Objects.requireNonNull(glyph);
        this.dropdown = dropdown;
        Ui.element(this).horizontalSizing(Sizing.fixed(dropdown ? 32 : 24));
        Tooltip.attach(Ui.element(this), name);
    }
    @Override public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var colors = Tokens.COLORS;
        if (active && isHoveredOrFocused())
            graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), colors.surfaceRaised());
        int color = active ? colors.text() : colors.textMuted();
        int size = glyph.pixels.length;
        int left = getX() + (getWidth() - size - (dropdown ? 8 : 0)) / 2, top = getY() + (getHeight() - size) / 2;
        for (int row = 0; row < size; row++) for (int col = 0; col < size; col++)
            if ((glyph.pixels[row] & (1 << (size - 1 - col))) != 0)
                graphics.fill(left + col, top + row, left + col + 1, top + row + 1, color);
        if (dropdown) {
            int cx = left + size + 3, cy = top + size / 2 - 1;
            graphics.fill(cx, cy, cx + 5, cy + 1, color);
            graphics.fill(cx + 1, cy + 1, cx + 4, cy + 2, color);
            graphics.fill(cx + 2, cy + 2, cx + 3, cy + 3, color);
        }
    }
}
