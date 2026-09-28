package local.minecraftfrontend.primitives;

import local.minecraftfrontend.tokens.Tokens;
import local.minecraftfrontend.Ui;

import io.wispforest.owo.ui.core.Sizing;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/** Input affordance only: caller owns bounds, constraints, cancellation and persistence. */
public final class ResizeHandle extends AbstractWidget {
    private final BiConsumer<Double, Double> resize;
    public ResizeHandle(Component name, BiConsumer<Double, Double> resize) {
        super(0, 0, 12, 12, name);
        this.resize = Objects.requireNonNull(resize);
        Ui.element(this).sizing(Sizing.fixed(12), Sizing.fixed(12));
        Tooltip.attach(Ui.element(this), Component.literal("Drag to resize. Arrow keys adjust size; Shift uses larger steps."));
    }
    @Override protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var colors = Tokens.COLORS;
        boolean highlighted = active && isHoveredOrFocused();
        if (highlighted) graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), colors.surfaceRaised());
        for (int band = 0; band < 3; band++) for (int pixel = 0; pixel <= band * 2; pixel++) {
            int x = getX() + getWidth() - 3 - pixel, y = getY() + getHeight() - 3 - band * 2 + pixel;
            graphics.fill(x, y, x + 1, y + 1, highlighted ? colors.focus() : colors.textMuted());
        }
    }
    @Override protected void onDrag(double x, double y, double dx, double dy) {
        if (active && visible) resize.accept(dx, dy);
    }
    @Override public boolean keyPressed(int key, int scan, int modifiers) {
        if (!active || !visible) return false;
        double step = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0 ? 32 : 8;
        switch (key) {
            case GLFW.GLFW_KEY_LEFT -> resize.accept(-step, 0.0);
            case GLFW.GLFW_KEY_RIGHT -> resize.accept(step, 0.0);
            case GLFW.GLFW_KEY_UP -> resize.accept(0.0, -step);
            case GLFW.GLFW_KEY_DOWN -> resize.accept(0.0, step);
            default -> { return false; }
        }
        return true;
    }
    @Override protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, getMessage());
        output.add(NarratedElementType.USAGE, Component.literal("Drag or use arrow keys to resize. Shift uses larger steps."));
    }
}
