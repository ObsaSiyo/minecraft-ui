package local.minecraftfrontend.primitives;

import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.gui.GuiGraphics;

public final class Clip {
    private Clip() {}
    /** owo clips parent content by default; this is a non-scrolling viewport. */
    public static FlowLayout create(Sizing width, Sizing height, Component content) {
        var viewport = Column.create(width, height);
        viewport.allowOverflow(false);
        viewport.child(content);
        return viewport;
    }
    /** Balanced scope for custom primitive drawing, including exceptional exits. */
    public static void render(GuiGraphics graphics, int x, int y, int width, int height, Runnable content) {
        if (width <= 0 || height <= 0) return;
        graphics.enableScissor(x, y, x + width, y + height);
        try { content.run(); }
        finally { graphics.disableScissor(); }
    }
}
